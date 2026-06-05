package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	 
    // SUA LAI theo may cua ban
    private static final String SERVER   = "localhost";
    private static final String PORT     = "1433";
    private static final String DB_DWH   = "QuanLyChuyenBay";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456789"; 
 
    private static Connection connection = null;
 
    private DatabaseConnection() {}
 
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:sqlserver://" + SERVER + ":" + PORT
                       + ";databaseName=" + DB_DWH
                       + ";encrypt=false;trustServerCertificate=true";
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            System.out.println("[DB] Ket noi thanh cong: " + DB_DWH);
        }
        return connection;
    }
 
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Da dong ket noi.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Loi dong ket noi: " + e.getMessage());
        }
    }
 
    // Chay thu de test - goi ham nay truoc khi lam gi khac
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println(">>> OK! Database: " + conn.getCatalog());
            closeConnection();
        } catch (SQLException e) {
            System.err.println(">>> LOI: " + e.getMessage());
        }
    }
}