package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SanBayDTO;
import util.DatabaseConnection; // Thay đổi package tương ứng với cấu trúc thực tế của em

public class SanBayDAO {

    // Method mẫu: Lấy toàn bộ danh sách Sân Bay (Read operation)
    public List<SanBayDTO> getAllSanBay() {
        List<SanBayDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM SAN_BAY"; // Hãy chắc chắn tên bảng trùng khớp với script SQL của em

        // Sử dụng try-with-resources để tự động đóng Connection, PreparedStatement và ResultSet
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanBayDTO sanBay = new SanBayDTO(
                    rs.getString("MaSB"),   // Kiểm tra chính xác tên cột trong DB
                    rs.getString("TenSB"),
                    rs.getString("TinhTP")
                );
                list.add(sanBay);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trong thực tế, nên dùng logger để catch exception
        }
        return list;
    }
}