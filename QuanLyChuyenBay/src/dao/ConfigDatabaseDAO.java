package dao;

import model.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfigDatabaseDAO {

    // Hardcode thông tin kết nối đến Control DB (chứa bảng CONFIG_DATABASE)
    private static final String CONTROL_DB_SERVER = "localhost";
    private static final String CONTROL_DB_PORT = "1433";
    private static final String CONTROL_DB_NAME = "controlDB"; // Tên database Control
    private static final String CONTROL_DB_USER = "sa";
    private static final String CONTROL_DB_PASS = "123456";

    /**
     * Lấy kết nối duy nhất đến Control DB để đọc cấu hình.
     */
    private Connection getControlDBConnection() throws SQLException {
        String url = "jdbc:sqlserver://" + CONTROL_DB_SERVER + ":" + CONTROL_DB_PORT
                   + ";databaseName=" + CONTROL_DB_NAME
                   + ";encrypt=false;trustServerCertificate=true";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy Driver SQL Server JDBC cho Control DB!");
        }
        return DriverManager.getConnection(url, CONTROL_DB_USER, CONTROL_DB_PASS);
    }

    /**
     * Đọc danh sách các cấu hình database đang Active = 1
     */
    public List<DatabaseConfig> getActiveConfigs() {
        List<DatabaseConfig> configs = new ArrayList<>();
        String sql = "SELECT DatabaseID, DatabaseName, ServerName, DatabaseType, DatabaseUser, DatabasePassword, IsActive " +
                     "FROM CONFIG_DATABASE WHERE IsActive = 1";

        try (Connection conn = getControlDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                DatabaseConfig config = new DatabaseConfig(
                        rs.getInt("DatabaseID"),
                        rs.getString("DatabaseName"),
                        rs.getString("ServerName"),
                        rs.getString("DatabaseType"),
                        rs.getString("DatabaseUser"),
                        rs.getString("DatabasePassword"),
                        rs.getBoolean("IsActive")
                );
                configs.add(config);
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi đọc bảng CONFIG_DATABASE từ Control DB: " + e.getMessage());
            e.printStackTrace();
        }

        return configs;
    }
}
