package util;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
	 
    private static final String DB_NAME = "QuanLyChuyenBay";
 
    private DatabaseConnection() {}
 
    public static Connection getConnection() throws SQLException {
        // Đã refactor dùng ConnectionManager
        Connection conn = ConnectionManager.getConnection(DB_NAME);
        if (conn == null) {
            throw new SQLException("Không lấy được kết nối tới " + DB_NAME + " từ ConnectionManager");
        }
        return conn;
    }
 
    public static void closeConnection() {
        // Đóng thông qua ConnectionManager
        ConnectionManager.closeAllConnections();
    }
 
    // Chay thu de test
//    public static void main(String[] args) {
//        try {
//            ConnectionManager.init(); // Khởi tạo kết nối từ config
//            Connection conn = getConnection();
//            System.out.println(">>> OK! Database: " + conn.getCatalog());
//            closeConnection();
//        } catch (SQLException e) {
//            System.err.println(">>> LOI: " + e.getMessage());
//        }
//    }
}