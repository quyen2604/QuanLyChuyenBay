package main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class EtlToDimensionFact {

    /**
     * Hàm điều phối việc gọi Stored Procedure cho Dimension và Fact.
     * Được gọi sau khi tiến trình load Staging hoàn tất.
     * 
     * @param connDWH Kết nối đến Data Warehouse
     */
    public static void runProcess(Connection connDWH) {
        System.out.println("\n==================================================");
        System.out.println("🚀 TIẾP TỤC TIẾN TRÌNH ETL: STAGING ---> DIMENSION ---> FACT");
        System.out.println("==================================================");

        if (connDWH == null) {
            System.err.println("❌ LỖI: Không có kết nối đến Data Warehouse. Hủy bỏ tiến trình Dim & Fact.");
            return;
        }

        long startTime = System.currentTimeMillis();

        // Bước 1: Nạp dữ liệu vào các bảng Dimension
        boolean dimSuccess = executeStoredProcedure(connDWH, "dbo.SP_Load_All_Dimension_Tu_Staging", "DIMENSION");
        
        // Bước 2: Chỉ nạp Fact nếu Dimension nạp thành công
        if (dimSuccess) {
            executeStoredProcedure(connDWH, "dbo.SP_Load_Fact_Ve_Tu_Staging", "FACT");
        } else {
            System.err.println("⚠️ BỎ QUA NẠP FACT do tiến trình nạp DIMENSION bị lỗi.");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\n🎉 HOÀN TẤT CHUYỂN ĐỔI STAGING -> DIM/FACT TRONG: " + (endTime - startTime) + " ms");
        System.out.println("==================================================\n");
    }

    /**
     * Hàm dùng chung để thực thi bất kỳ Stored Procedure nào
     * 
     * @param conn Kết nối DB
     * @param spName Tên Stored Procedure cần chạy
     * @param phase Tên giai đoạn để in log (VD: "DIMENSION", "FACT")
     * @return true nếu thành công, false nếu có lỗi
     */
    private static boolean executeStoredProcedure(Connection conn, String spName, String phase) {
        System.out.println("⏳ Đang kích hoạt tiến trình nạp " + phase + " (Gọi SP: " + spName + ")...");
        
        String sql = "{call " + spName + "}";
        
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            // Lệnh execute() sẽ chờ SQL Server chạy xong toàn bộ SP
            cstmt.execute();
            System.out.println("✅ THÀNH CÔNG: Đã hoàn tất nạp dữ liệu cho " + phase + ".");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ LỖI KHI NẠP " + phase + " (" + spName + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
