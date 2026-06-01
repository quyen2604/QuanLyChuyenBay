package dao;

import model.KhachHangDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

	public List<KhachHangDTO> getAllKhachHang() {
	    List<KhachHangDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM KHACH_HANG";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            KhachHangDTO khachHang = new KhachHangDTO(
	                rs.getString("MaKH"),
	                rs.getString("HoTen"),
	                rs.getString("SDT"),
	                rs.getString("CCCD"),
	                rs.getString("DiaChi")
	            );
	            list.add(khachHang);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
}
