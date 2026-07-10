package util;

import dao.ConfigDatabaseDAO;
import model.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionManager {

    // Bộ nhớ lưu trữ Connection cho từng database
    private static final Map<String, Connection> connectionPool = new HashMap<>();
    // Lưu trữ cấu hình để tự động kết nối lại khi bị đóng
    private static final Map<String, DatabaseConfig> configMap = new HashMap<>();

    // Không cho phép khởi tạo trực tiếp
    private ConnectionManager() {
    }

    /**
     * Khởi tạo các kết nối dựa trên cấu hình từ Control DB.
     * Hàm này NÊN được gọi 1 lần duy nhất khi ứng dụng mới khởi động.
     */
    public static void init() {
        System.out.println("[ConnectionManager] Bắt đầu khởi tạo cấu hình từ Control DB...");
        ConfigDatabaseDAO dao = new ConfigDatabaseDAO();
        List<DatabaseConfig> configs = dao.getActiveConfigs();

        if (configs.isEmpty()) {
            System.err.println("[ConnectionManager] Không tìm thấy cấu hình database nào (IsActive = 1).");
            return;
        }

        try {
            // Load driver dùng chung
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            for (DatabaseConfig config : configs) {
                String dbName = config.getDatabaseName();
                configMap.put(dbName, config); // Lưu cấu hình
                
                try {
                    Connection conn = createConnection(config);
                    connectionPool.put(dbName, conn);
                    System.out.println("[ConnectionManager] ✅ Kết nối thành công tới Database: " + dbName);
                } catch (SQLException ex) {
                    System.err.println("[ConnectionManager] ❌ Lỗi kết nối tới Database: " + dbName + " - " + ex.getMessage());
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy Driver SQL Server JDBC!");
        }
        System.out.println("[ConnectionManager] Hoàn tất khởi tạo " + connectionPool.size() + " kết nối.");
    }
    
    private static Connection createConnection(DatabaseConfig config) throws SQLException {
        String url = "jdbc:sqlserver://" + config.getServerName() + ":1433"
                + ";databaseName=" + config.getDatabaseName()
                + ";encrypt=false;trustServerCertificate=true";
        return DriverManager.getConnection(url, config.getDatabaseUser(), config.getDatabasePassword());
    }

    /**
     * Lấy kết nối theo tên Database.
     * Nếu kết nối bị đóng (do DAOs dùng try-with-resources), sẽ tự động kết nối lại.
     */
    public static Connection getConnection(String dbName) {
        Connection conn = connectionPool.get(dbName);
        try {
            // Kiểm tra kết nối có bị null hoặc bị đóng không
            if (conn == null || conn.isClosed()) {
                DatabaseConfig config = configMap.get(dbName);
                if (config != null) {
                    // System.out.println("[ConnectionManager] 🔄 Tự động kết nối lại tới: " + dbName);
                    conn = createConnection(config);
                    connectionPool.put(dbName, conn);
                } else {
                    System.err.println("[ConnectionManager] ⚠️ Không tìm thấy cấu hình cho " + dbName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Đóng tất cả kết nối khi dừng chương trình
     */
    public static void closeAllConnections() {
        System.out.println("[ConnectionManager] Đang đóng tất cả kết nối...");
        for (Map.Entry<String, Connection> entry : connectionPool.entrySet()) {
            Connection conn = entry.getValue();
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("[ConnectionManager] Đã đóng kết nối: " + entry.getKey());
                }
            } catch (SQLException e) {
                System.err.println("[ConnectionManager] Lỗi khi đóng kết nối " + entry.getKey() + ": " + e.getMessage());
            }
        }
        connectionPool.clear();
    }
}
