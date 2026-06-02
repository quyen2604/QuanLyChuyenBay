package dao;

import model.LoaiVeDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiVeDAO {

	public List<LoaiVeDTO> getAllLoaiVe() {
	    List<LoaiVeDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM LOAI_VE";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            LoaiVeDTO loaiVe = new LoaiVeDTO(
	                rs.getString("MaLoaiVe"),
	                rs.getString("TenLoai")
	            );
	            list.add(loaiVe);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	public boolean update(LoaiVeDTO dto) {
        String sql = "UPDATE LOAI_VE SET TenLoai=? WHERE MaLoaiVe=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getTenLoai());
            ps.setString(2, dto.getMaLoaiVe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maLoaiVe) {
        String sql = "DELETE FROM LOAI_VE WHERE MaLoaiVe=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoaiVe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
