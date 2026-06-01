package dao;

import model.MayBayDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MayBayDAO {
	public List<MayBayDTO> getAllMayBay() {
	    List<MayBayDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM MAY_BAY";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            MayBayDTO mayBay = new MayBayDTO(
	                rs.getString("MaMB"),
	                rs.getString("TenMayBay"),
	                rs.getInt("TongSoGhe"),
	                rs.getString("MaLoai")
	            );
	            list.add(mayBay);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
  
}
