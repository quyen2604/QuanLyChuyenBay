
package dao;

import model.SanBayDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanBayDAO {

	public List<SanBayDTO> getAllSanBay() {
	    List<SanBayDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM SAN_BAY";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            SanBayDTO sanBay = new SanBayDTO(
	                rs.getString("MaSB"),
	                rs.getString("TenSB"),
	                rs.getString("TinhTP")
	            );
	            list.add(sanBay);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

    
}
