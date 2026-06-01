package dao;

import model.VeDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeDAO {

	public List<VeDTO> getAllVe() {
	    List<VeDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM VE";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            VeDTO ve = new VeDTO(
	                rs.getString("MaVe"),
	                rs.getString("MaCB"),
	                rs.getDate("NgayDatVe"),
	                rs.getString("MaHoaDon"),
	                rs.getDate("NgayNhanVe"),
	                rs.getString("MaGhe")
	            );
	            list.add(ve);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
}
