package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseDWHConnection {

    // Kết nối thẳng vào database gốc chạy ứng dụng
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=QuanLyChuyenBay_DWH_Snowflake;" // Kết nối vào DB QuanLyChuyenBay_DWH_Snowflake ở đây
            + "encrypt=true;"
            + "trustServerCertificate=true;";
            
    private static final String USER = "sa";       
    private static final String PASS = "123456789";   // Thay bằng mật khẩu của bạn

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy Driver SQL Server JDBC!");
            throw new SQLException(e);
        }
    }
}