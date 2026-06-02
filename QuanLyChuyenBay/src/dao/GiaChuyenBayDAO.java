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
	public boolean update(GiaChuyenBayDTO dto) {
        String sql = "UPDATE GIA_CHUYEN_BAY SET GiaTien=? WHERE MaCB=? AND MaLoaiVe=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, dto.getGiaTien());
            ps.setString(2, dto.getMaCB());
            ps.setString(3, dto.getMaLoaiVe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maCB, String maLoaiVe) {
        String sql = "DELETE FROM GIA_CHUYEN_BAY WHERE MaCB=? AND MaLoaiVe=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maCB);
            ps.setString(2, maLoaiVe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
