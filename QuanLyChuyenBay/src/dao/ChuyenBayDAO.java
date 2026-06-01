package dao;

import model.ChuyenBayDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChuyenBayDAO {

	public List<ChuyenBayDTO> getAllChuyenBay() {
	    List<ChuyenBayDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM CHUYEN_BAY";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            ChuyenBayDTO chuyenBay = new ChuyenBayDTO(
	                rs.getString("MaCB"),
	                rs.getTime("GioKH"),
	                rs.getDate("NgayKH"),
	                rs.getString("SBDen"),
	                rs.getString("SBDi"),
	                rs.getString("MaMB")
	            );
	            list.add(chuyenBay);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

    
}
