
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


	 public boolean update(SanBayDTO dto) {
	        String sql = "UPDATE SAN_BAY SET TenSB=?, TinhTP=? WHERE MaSB=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, dto.getTenSB());
	            ps.setString(2, dto.getTinhTP());
	            ps.setString(3, dto.getMaSB());
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public boolean delete(String maSB) {
	        String sql = "DELETE FROM SAN_BAY WHERE MaSB=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, maSB);
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


}
