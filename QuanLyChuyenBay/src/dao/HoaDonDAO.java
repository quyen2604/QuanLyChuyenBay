package dao;

import model.HoaDonDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

	public List<HoaDonDTO> getAllHoaDon() {
	    List<HoaDonDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM HOA_DON";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            HoaDonDTO hoaDon = new HoaDonDTO(
	                rs.getString("MaHoaDon"),
	                rs.getString("MaKH"),
	                rs.getDate("NgayLapHD"),
	                rs.getDouble("ThanhTien")
	            );
	            list.add(hoaDon);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
}
