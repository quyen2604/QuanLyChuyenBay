package util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseDWHConnection {

    private static final String DB_NAME = "QuanLyChuyenBay_DWH_Snowflake";

    public static Connection getConnection() throws SQLException {
        // Đã refactor dùng ConnectionManager
        Connection conn = ConnectionManager.getConnection(DB_NAME);
        if (conn == null) {
            throw new SQLException("Không lấy được kết nối tới " + DB_NAME + " từ ConnectionManager");
        }
        return conn;
    }
}