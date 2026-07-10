package main;

import util.ConnectionManager; // Giả định đây là package chứa Manager của bạn

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class EtlToStaging {

    // Tạo một class nội bộ để lưu kết quả trả về của mỗi luồng ETL
    static class EtlResult {
        boolean success;
        Timestamp newMocThoiGian;
        String errorMessage;

        public EtlResult(boolean success, Timestamp newMocThoiGian, String errorMessage) {
            this.success = success;
            this.newMocThoiGian = newMocThoiGian;
            this.errorMessage = errorMessage;
        }
    }

    public static void main(String[] args) {
        System.out.println("🚀 BẮT ĐẦU TIẾN TRÌNH ETL: OLTP ---> DWH STAGING");
        long startTime = System.currentTimeMillis();

        // 1. KHỞI TẠO METADATA MANAGER (BẮT BUỘC)
        ConnectionManager.init();

        // 2. LẤY KẾT NỐI TỪ MANAGER (Không còn code cứng)
        try (Connection connOLTP = ConnectionManager.getConnection("QuanLyChuyenBay");
             Connection connDWH  = ConnectionManager.getConnection("QuanLyChuyenBay_DWH_Snowflake")) {

            if (connOLTP == null || connDWH == null) {
                System.err.println("❌ LỖI: Không thể lấy kết nối từ ConnectionManager. Hãy kiểm tra lại ControlDB!");
                return;
            }

            // 3. Đọc cấu hình ETL (Bổ sung thêm KieuLoad, MocThoiGianCuoi, CotThoiGianNguon)
            String sqlConfig = "SELECT TenLuongETL, TenBangNguon, TenBangStaging, KieuLoad, KichThuocBatch, MocThoiGianCuoi, CotThoiGianNguon FROM CONFIG_ETL WHERE IsActive = 1";
            
            try (Statement stmtConfig = connDWH.createStatement();
                 ResultSet rsConfig = stmtConfig.executeQuery(sqlConfig)) {

                while (rsConfig.next()) {
                    String tenLuong = rsConfig.getString("TenLuongETL");
                    String bangNguon = rsConfig.getString("TenBangNguon");
                    String bangStaging = rsConfig.getString("TenBangStaging");
                    String kieuLoad = rsConfig.getString("KieuLoad");
                    int batchSize = rsConfig.getInt("KichThuocBatch");
                    Timestamp mocThoiGianCuoi = rsConfig.getTimestamp("MocThoiGianCuoi");
                    String cotThoiGianNguon = rsConfig.getString("CotThoiGianNguon");

                    System.out.println("\n⏳ Đang xử lý luồng: " + tenLuong + " [" + kieuLoad + "]");

                    // Thực thi hàm ETL lõi
                    EtlResult result = extractAndLoad(connOLTP, connDWH, bangNguon, bangStaging, kieuLoad, batchSize, mocThoiGianCuoi, cotThoiGianNguon);
                    
                    // Ghi nhận trạng thái vào CONFIG_ETL
                    updateConfigStatus(connDWH, tenLuong, result);
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("\n🎉 HOÀN TẤT TOÀN BỘ TIẾN TRÌNH STAGING TRONG: " + (endTime - startTime) + " ms");

            // 4. KÍCH HOẠT CHUỖI NẠP DIMENSION & FACT TỪ SP
            EtlToDimensionFact.runProcess(connDWH);

        } catch (SQLException e) {
            System.err.println("❌ LỖI HỆ THỐNG: " + e.getMessage());
        }
    }

    /**
     * Hàm lõi: Xử lý cả FULL và DELTA Load, có theo dõi thời gian
     */
    private static EtlResult extractAndLoad(Connection connOLTP, Connection connDWH, String bangNguon, 
                                            String bangStaging, String kieuLoad, int batchSize, 
                                            Timestamp mocThoiGianCuoi, String cotThoiGianNguon) {
        try {
            boolean isDelta = "DELTA".equalsIgnoreCase(kieuLoad);

            // BƯỚC 1: Xử lý Staging Table
            try (Statement stmtDWH = connDWH.createStatement()) {
                if (isDelta) {
                    // Nếu là DELTA: Không xóa bảng, chỉ xóa những dòng có thể bị trùng (Tùy logic nghiệp vụ)
                    // Tạm thời bỏ qua bước xóa đối với DELTA.
                } else {
                    // Nếu là FULL: Xóa trắng bảng Staging
                    stmtDWH.executeUpdate("TRUNCATE TABLE " + bangStaging);
                }
            }

            // BƯỚC 2: Tạo câu lệnh SELECT từ bảng nguồn
            String sqlSelect = "SELECT * FROM " + bangNguon;
            if (isDelta && !"none".equalsIgnoreCase(cotThoiGianNguon)) {
                sqlSelect += " WHERE " + cotThoiGianNguon + " > ?";
            }

            try (PreparedStatement pstmtSelect = connOLTP.prepareStatement(sqlSelect)) {
                // Truyền tham số thời gian nếu là DELTA
                if (isDelta && !"none".equalsIgnoreCase(cotThoiGianNguon)) {
                    pstmtSelect.setTimestamp(1, mocThoiGianCuoi);
                }

                try (ResultSet rsData = pstmtSelect.executeQuery()) {
                    ResultSetMetaData metaData = rsData.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Tìm vị trí của cột thời gian để track max time (dành cho DELTA)
                    int timeColumnIndex = -1;
                    if (isDelta && !"none".equalsIgnoreCase(cotThoiGianNguon)) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (metaData.getColumnName(i).equalsIgnoreCase(cotThoiGianNguon)) {
                                timeColumnIndex = i;
                                break;
                            }
                        }
                    }

                    // Sinh tự động câu lệnh INSERT
                    StringBuilder cols = new StringBuilder();
                    StringBuilder placeholders = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        cols.append(metaData.getColumnName(i));
                        placeholders.append("?");
                        if (i < columnCount) {
                            cols.append(", ");
                            placeholders.append(", ");
                        }
                    }
                    String sqlInsert = "INSERT INTO " + bangStaging + " (" + cols.toString() + ") VALUES (" + placeholders.toString() + ")";

                    // BƯỚC 3: Chép dữ liệu qua mạng bằng Batching và tìm MocThoiGian mới
                    Timestamp maxTimestamp = mocThoiGianCuoi;

                    try (PreparedStatement pstmtInsert = connDWH.prepareStatement(sqlInsert)) {
                        int count = 0;
                        while (rsData.next()) {
                            // Tracking thời gian lớn nhất trong mẻ dữ liệu
                            if (timeColumnIndex != -1) {
                                Timestamp currentRowTime = rsData.getTimestamp(timeColumnIndex);
                                if (currentRowTime != null && (maxTimestamp == null || currentRowTime.after(maxTimestamp))) {
                                    maxTimestamp = currentRowTime;
                                }
                            }

                            for (int i = 1; i <= columnCount; i++) {
                                pstmtInsert.setObject(i, rsData.getObject(i));
                            }
                            pstmtInsert.addBatch();

                            if (++count % batchSize == 0) {
                                pstmtInsert.executeBatch();
                            }
                        }
                        pstmtInsert.executeBatch(); // Bắn nốt mẻ cuối
                        
                        System.out.println("  -> Đã nạp thành công " + count + " dòng vào " + bangStaging);
                        return new EtlResult(true, isDelta ? maxTimestamp : null, null);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("  -> ❌ LỖI khi xử lý: " + e.getMessage());
            return new EtlResult(false, null, e.getMessage());
        }
    }

    /**
     * Hàm cập nhật cấu hình: Cập nhật cả trạng thái, thời gian chạy, lỗi và Mốc thời gian cuối (nếu có)
     */
    private static void updateConfigStatus(Connection connDWH, String tenLuong, EtlResult result) {
        StringBuilder sql = new StringBuilder("UPDATE CONFIG_ETL SET TrangThaiChayCuoi = ?, ThoiGianChayCuoi = GETDATE(), GhiChuLoi = ?");
        
        // Nếu có mốc thời gian mới (từ quá trình DELTA load), cập nhật nó
        if (result.success && result.newMocThoiGian != null) {
            sql.append(", MocThoiGianCuoi = ?");
        }
        sql.append(" WHERE TenLuongETL = ?");

        try (PreparedStatement pstmt = connDWH.prepareStatement(sql.toString())) {
            pstmt.setString(1, result.success ? "SUCCESS" : "FAILED");
            pstmt.setString(2, result.errorMessage);
            
            int paramIndex = 3;
            if (result.success && result.newMocThoiGian != null) {
                pstmt.setTimestamp(paramIndex++, result.newMocThoiGian);
            }
            pstmt.setString(paramIndex, tenLuong);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Không thể cập nhật trạng thái cho luồng " + tenLuong + " - " + e.getMessage());
        }
    }
}

