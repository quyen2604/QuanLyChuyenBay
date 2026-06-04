package main;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseConnection;

public class MasterETL {

    // Đếm số dòng hiện tại trong bảng
    private static int countRows(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public static boolean importSingleTable(Connection conn, String tableName, String folderPath) {
        String filePath = folderPath + File.separator + tableName + ".csv";

        // 1. Kiểm tra file vật lý
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("❌ KHÔNG TÌM THẤY FILE: " + filePath + " -> Bỏ qua bảng này.");
            return false;
        }

        // 2. Kiểm tra CONFIG_IMPORT
        String checkSql = "SELECT ThuTuCot FROM CONFIG_IMPORT WHERE TenBang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, tableName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("⚠️  CẢNH BÁO: Bảng [" + tableName + "] chưa đăng ký trong CONFIG_IMPORT!");
                    return false;
                }
            }

            // 3. Đếm số dòng TRƯỚC khi nạp
            int rowsBefore = countRows(conn, tableName);

            // 4. Gọi SP với đúng 2 tham số
            String spCall = "{call sp_MasterBulkImport(?, ?)}";
            try (CallableStatement cstmt = conn.prepareCall(spCall)) {
                cstmt.setString(1, tableName);
                cstmt.setString(2, filePath);

                System.out.println("⏳ Đang nạp bảng [" + tableName + "]...");
                cstmt.execute();

                // 5. Đếm số dòng SAU khi nạp → tính ra dòng mới / dòng bỏ qua
                int rowsAfter  = countRows(conn, tableName);
                int inserted   = rowsAfter - rowsBefore;

                if (inserted == 0) {
                    System.out.println("⚠️  BỎ QUA TRÙNG LẶP bảng [" + tableName
                            + "]: 0 dòng mới được thêm — toàn bộ dữ liệu đã tồn tại.");
                } else {
                    System.out.println("✅ THÀNH CÔNG bảng [" + tableName
                            + "]: +" + inserted + " dòng mới"
                            + (rowsBefore > 0 ? " | " + rowsBefore + " dòng đã tồn tại trước đó." : "."));
                }
                return true;
            }

        } catch (SQLException e) {
            if (isDuplicateError(e)) {
                // SP không xử lý duplicate nội bộ → Java bắt được ở đây
                System.out.println("⚠️  BỎ QUA TRÙNG LẶP bảng [" + tableName
                        + "]: " + e.getMessage());
                return true;
            }
            System.err.println("❌ LỖI NGHIÊM TRỌNG bảng [" + tableName + "]: " + e.getMessage());
            return false;
        }
    }

    // SQL Server: 2627 = Unique constraint, 2601 = Duplicate key index
    private static boolean isDuplicateError(SQLException e) {
        int code = e.getErrorCode();
        return code == 2627 || code == 2601;
    }

    public static void main(String[] args) {
        String dataFolder = (args.length > 0)
                ? args[0]
                : "C:\\Users\\Sang\\OneDrive\\Máy tính\\on_tap_CSDL\\data";

        String[] allTables = {
            // Nhóm 1: độc lập (không có FK)
            "SAN_BAY", "LOAI_MAY_BAY", "KHACH_HANG", "LOAI_VE", "DICH_VU",
            // Nhóm 2: phụ thuộc FK — bắt buộc sau Nhóm 1
            "MAY_BAY",               // FK → LOAI_MAY_BAY
            "CHUYEN_BAY",            // FK → SAN_BAY, MAY_BAY
            "HOA_DON",               // FK → KHACH_HANG
            "GHE",                   // FK → MAY_BAY, LOAI_VE
            "VE",                    // FK → CHUYEN_BAY, HOA_DON, GHE
            "CHITIET_DICHVU_LOAIVE", // Bảng trung gian n-n
            "GIA_CHUYEN_BAY"         // Bảng trung gian n-n
        };

        System.out.println("🚀 BẮT ĐẦU TIẾN TRÌNH TỰ ĐỘNG LOAD TOÀN BỘ DATABASE...");
        long startTime = System.currentTimeMillis();

        List<String> failedTables  = new ArrayList<>();
        List<String> skippedTables = new ArrayList<>(); // Bảng 0 dòng mới (toàn trùng)
        int successCount = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            for (String tableName : allTables) {
                // Snapshot dòng trước để phát hiện "toàn trùng" ở cấp main
                int rowsBefore;
                try {
                    rowsBefore = countRows(conn, tableName);
                } catch (SQLException e) {
                    rowsBefore = -1; // Không đếm được, bỏ qua check này
                }

                boolean ok = importSingleTable(conn, tableName, dataFolder);

                if (ok) {
                    successCount++;
                    // Kiểm tra thêm xem có thực sự insert dòng nào không
                    try {
                        int rowsAfter = countRows(conn, tableName);
                        if (rowsBefore >= 0 && rowsAfter == rowsBefore) {
                            skippedTables.add(tableName);
                        }
                    } catch (SQLException ignored) {}
                } else {
                    failedTables.add(tableName);
                }
                System.out.println("------------------------------------------------------------------");
            }

            // Commit nếu không có lỗi nghiêm trọng, rollback nếu có
            if (failedTables.isEmpty()) {
                conn.commit();
                System.out.println("✅ COMMIT toàn bộ thành công.");
            } else {
                conn.rollback();
                System.err.println("⚠️  ROLLBACK toàn bộ do có bảng thất bại.");
            }

        } catch (SQLException e) {
            System.err.println("❌ LỖI KẾT NỐI DATABASE: " + e.getMessage());
        }

        // Báo cáo tổng kết
        long endTime = System.currentTimeMillis();
        System.out.println("\n📊 ═══════════ TỔNG KẾT TIẾN TRÌNH ═══════════");
        System.out.println("🔹 Nạp thành công : " + successCount + "/" + allTables.length + " bảng");
        System.out.println("🔹 Thời gian thực thi: " + (endTime - startTime) + " ms");

        if (!skippedTables.isEmpty()) {
            System.out.println("⚠️  Bảng toàn trùng (0 dòng mới): " + String.join(", ", skippedTables));
        }
        if (!failedTables.isEmpty()) {
            System.err.println("❌ Bảng thất bại: " + String.join(", ", failedTables));
        }
        if (failedTables.isEmpty() && skippedTables.isEmpty()) {
            System.out.println("🎉 Toàn bộ bảng đã được nạp thành công với dữ liệu mới!");
        }
    }
}