package dao;

import model.GiaChuyenBayDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiaChuyenBayDAO {

	public List<GiaChuyenBayDTO> getAllGiaChuyenBay() {
	    List<GiaChuyenBayDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM GIA_CHUYEN_BAY";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            GiaChuyenBayDTO giaChuyenBay = new GiaChuyenBayDTO(
	                rs.getString("MaCB"),
	                rs.getString("MaLoaiVe"),
	                rs.getDouble("GiaTien")
	            );
	            list.add(giaChuyenBay);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
}
