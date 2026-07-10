package main;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConnectionManager;
import util.DatabaseConnection;

public class MasterETL {

    /**
     * Hàm xử lý nạp dữ liệu từ File vào MỘT bảng bất kỳ thông qua Stored Procedure
     * @param conn: Kết nối database đang dùng
     * @param tableName: Tên bảng cần nạp (Ví dụ: "SAN_BAY")
     * @param folderPath: Thư mục chứa các file .csv trên máy tính
     * @return true nếu nạp thành công, false nếu thất bại hoặc bỏ qua
     */
    public static boolean importSingleTable(Connection conn, String tableName, String folderPath) {
        // Tự động ghép đường dẫn file dựa theo tên bảng (Ví dụ: C:\data\SAN_BAY.csv)
        String filePath = folderPath + File.separator + tableName + ".csv";
        
        // 1. Kiểm tra hiện trường: File vật lý có tồn tại trên ổ cứng không?
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("❌ KHÔNG TÌM THẤY FILE: " + filePath + " -> Bỏ qua không nạp bảng này.");
            return false;
        }

        // 2. Xin giấy phép kiểm tra "Trạm gác" CONFIG_IMPORT dưới SQL Server
        String checkSql = "SELECT ThuTuCot FROM CONFIG_IMPORT WHERE TenBang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, tableName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("⚠️ CẢNH BÁO: Bảng [" + tableName + "] chưa được đăng ký cấu hình trong CONFIG_IMPORT!");
                    return false;
                }
            }
            
            // 3. Gọi Stored Procedure tổng quản dưới SQL Server để xử lý
            String spCall = "{call sp_MasterBulkImport(?, ?)}";
            try (CallableStatement cstmt = conn.prepareCall(spCall)) {
                cstmt.setString(1, tableName); // Tham số thứ 1: Tên bảng chính
                cstmt.setString(2, filePath);  // Tham số thứ 2: Đường dẫn file vật lý
                
                System.out.println("⏳ Đang kích hoạt BULK INSERT tự động cho bảng: [" + tableName + "]...");
                
                // Lệnh này sẽ block luồng xử lý của Java để chờ SQL Server thực thi xong Giai đoạn 1->4
                cstmt.execute(); 
                
                System.out.println("🎉 THÀNH CÔNG: Đã sàng lọc và nạp dữ liệu xong cho bảng [" + tableName + "].");
                return true;
            }
            
        } catch (SQLException e) {
            // Chụp lại thông báo lỗi chi tiết do SQL Server ném ngược lên nếu có sự cố
            System.err.println("❌ LỖI HỆ THỐNG KHI IMPORT BẢNG [" + tableName + "]: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        // 🚨 QUY TẮC CHÍ MẠNG: Thứ tự sắp xếp các bảng trong mảng này bắt buộc phải đi từ 
        // các bảng Độc lập (Không có khóa ngoại) trước, sau đó mới đến các bảng Phụ thuộc (Có khóa ngoại).
        // Nếu xếp sai thứ tự, SQL Server sẽ chặn đứng và báo lỗi vi phạm ràng buộc Khóa ngoại (FK Check)!
        String[] listTables = {
            // -- Nhóm 1: Các bảng gốc độc lập (Không chứa khóa ngoại)
            "SAN_BAY", 
            "LOAI_MAY_BAY", 
            "KHACH_HANG", 
            "LOAI_VE", 
            "DICH_VU", 
            
            // -- Nhóm 2: Các bảng phụ thuộc (Cần tham chiếu đến các bảng nhóm 1)
            "MAY_BAY",       // Tham chiếu đến LOAI_MAY_BAY
            "CHUYEN_BAY",    // Tham chiếu đến SAN_BAY và MAY_BAY
            "HOA_DON",       // Tham chiếu đến KHACH_HANG
            "GHE",           // Tham chiếu đến MAY_BAY và LOAI_VE
            "VE",            // Tham chiếu đến CHUYEN_BAY, HOA_DON, GHE
            "CHITIET_DICHVU_LOAIVE", // Bảng trung gian n-n
            "GIA_CHUYEN_BAY"         // Bảng trung gian n-n
        };

        // 📂 Đường dẫn đến thư mục chứa toàn bộ các file .csv của em trên máy tính
        // Em có thể sửa lại đường dẫn này theo đúng thực tế máy của em (Ví dụ: "D:\\DoAn\\Data")
        String dataFolder = "D:\\\\NAM2_HK2\\\\CSDL\\\\Doc\\\\file_csv_CSDL\\\\"; 

        System.out.println("🚀 BẮT ĐẦU TIẾN TRÌNH TỰ ĐỘNG LOAD TOÀN BỘ DATABASE...");
        long startTime = System.currentTimeMillis();
        
        // Mở kết nối Database thông qua Class Utility thực tế của em
        ConnectionManager.init();
        Connection conn = null;

        try  { 
        	conn = DatabaseConnection.getConnection();
            
            int successCount = 0;
            
            // Chạy vòng lặp vạn năng tự động quét qua tất cả các bảng
            for (String tableName : listTables) {
                boolean isSuccess = importSingleTable(conn, tableName, dataFolder);
                if (isSuccess) {
                    successCount++;
                }
                System.out.println("------------------------------------------------------------------");
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("📊 TỔNG KẾT TIẾN TRÌNH:");
            System.out.println("🔹 Đã nạp thành công: " + successCount + "/" + listTables.length + " bảng.");
            System.out.println("🔹 Thời gian thực thi tổng cộng: " + (endTime - startTime) + " ms");
            
        } catch (SQLException e) {
            System.err.println("❌ LỖI KẾT NỐI DATABASE TỔNG THỂ: " + e.getMessage());
        } 
    }
}