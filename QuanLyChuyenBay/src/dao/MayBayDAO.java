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
  
	public boolean update(MayBayDTO dto) {
        String sql = "UPDATE MAY_BAY SET TenMayBay=?, TongSoGhe=?, MaLoai=? WHERE MaMB=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getTenMayBay());
            ps.setInt(2, dto.getTongSoGhe());
            ps.setString(3, dto.getMaLoai());
            ps.setString(4, dto.getMaMB());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maMB) {
        String sql = "DELETE FROM MAY_BAY WHERE MaMB=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maMB);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
