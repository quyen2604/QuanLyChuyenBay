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

	public boolean update(ChuyenBayDTO dto) {
        String sql = "UPDATE CHUYEN_BAY SET GioKH=?, NgayKH=?, SBDen=?, SBDi=?, MaMB=? WHERE MaCB=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTime(1, dto.getGioKH());
            ps.setDate(2, new java.sql.Date(dto.getNgayKH().getTime()));
            ps.setString(3, dto.getSBDen());
            ps.setString(4, dto.getSBDi());
            ps.setString(5, dto.getMaMB());
            ps.setString(6, dto.getMaCB());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maCB) {
        String sql = "DELETE FROM CHUYEN_BAY WHERE MaCB=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maCB);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
