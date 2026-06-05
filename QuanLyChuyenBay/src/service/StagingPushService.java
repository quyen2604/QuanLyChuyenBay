package service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import util.DatabaseDWHConnection; // Sử dụng kết nối tới DB gốc vừa tạo ở trên

public class StagingPushService {

	/**
	 * Hàm gọi Stored Procedure nằm trong DB QuanLyChuyenBay_DWH_Snowflake nhằm xóa
	 * và đẩy dữ liệu mới sang bảng Staging của DWH
	 */
	public static void dayDuLieuSangStagingDWH() {
		System.out.println("⏳ [ETL] Bắt đầu kích hoạt Stored Procedure tại Database gốc...");

		// Mở kết nối tới database QuanLyChuyenBay_DWH_Snowflake
		try (Connection conn = DatabaseDWHConnection.getConnection();
				CallableStatement cstmt = conn.prepareCall("{call SP_Load_All_Staging_Tu_DB_Goc}")) {

			System.out.println("⏳ DB gốc đang thực hiện dọn dẹp và đẩy dữ liệu sang DWH Snowflake...");

			// Thực thi lệnh chạy ngầm trên SQL Server
			cstmt.execute();

			System.out.println("✅ Thành công: Dữ liệu đã được đẩy trọn vẹn sang tầng Staging!");

		} catch (SQLException e) {
			System.err.println("❌ LỖI khi thực thi đẩy dữ liệu từ DB gốc: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Chạy kiểm tra tính năng đẩy dữ liệu sang Staging
		StagingPushService.dayDuLieuSangStagingDWH();
	}
}
