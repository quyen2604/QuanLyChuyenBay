package dao;

import model.LoaiMayBayDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiMayBayDAO {

	public List<LoaiMayBayDTO> getAllLoaiMayBay() {
	    List<LoaiMayBayDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM LOAI_MAY_BAY";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            LoaiMayBayDTO loaiMayBay = new LoaiMayBayDTO(
	                rs.getString("MaLoai"),
	                rs.getString("HangSX")
	            );
	            list.add(loaiMayBay);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}


	public boolean update(LoaiMayBayDTO dto) {
        String sql = "UPDATE LOAI_MAY_BAY SET HangSX=? WHERE MaLoai=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getHangSX());
            ps.setString(2, dto.getMaLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maLoai) {
        String sql = "DELETE FROM LOAI_MAY_BAY WHERE MaLoai=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
