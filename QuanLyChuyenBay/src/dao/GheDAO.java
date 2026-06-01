package dao;

import model.GheDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GheDAO {

	public List<GheDTO> getAllGhe() {
	    List<GheDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM GHE";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            GheDTO ghe = new GheDTO(
	                rs.getString("MaGhe"),
	                rs.getString("MaMB"),
	                rs.getString("SoGhe"),
	                rs.getString("MaLoaiVe")
	            );
	            list.add(ghe);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
}
