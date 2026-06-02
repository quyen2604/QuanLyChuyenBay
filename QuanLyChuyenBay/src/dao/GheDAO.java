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

	
	public boolean update(GheDTO dto) {
        String sql = "UPDATE GHE SET MaMB=?, SoGhe=?, MaLoaiVe=? WHERE MaGhe=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getMaMB());
            ps.setString(2, dto.getSoGhe());
            ps.setString(3, dto.getMaLoaiVe());
            ps.setString(4, dto.getMaGhe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maGhe) {
        String sql = "DELETE FROM GHE WHERE MaGhe=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maGhe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
