package dao;

import model.DichVuDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAO {

	public List<DichVuDTO> getAllDichVu() {
	    List<DichVuDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM DICH_VU";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            DichVuDTO dichVu = new DichVuDTO(
	                rs.getString("MaDV"),
	                rs.getString("TenDichVu"),
	                rs.getDouble("GiaDichVu")
	            );
	            list.add(dichVu);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	 
	 

}
