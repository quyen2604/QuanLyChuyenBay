package dao;

import model.ChiTietDichVuLoaiVeDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDichVuLoaiVeDAO {

	public List<ChiTietDichVuLoaiVeDTO> getAllChiTietDichVuLoaiVe() {
	    List<ChiTietDichVuLoaiVeDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM CHITIET_DICHVU_LOAIVE";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            ChiTietDichVuLoaiVeDTO chiTiet = new ChiTietDichVuLoaiVeDTO(
	                rs.getString("MaLoaiVe"),
	                rs.getString("MaDV"),
	                rs.getInt("SoLuong")
	            );
	            list.add(chiTiet);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	public boolean update(ChiTietDichVuLoaiVeDTO dto) {
        String sql = "UPDATE CHITIET_DICHVU_LOAIVE SET SoLuong=? WHERE MaLoaiVe=? AND MaDV=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dto.getSoLuong());
            ps.setString(2, dto.getMaLoaiVe());
            ps.setString(3, dto.getMaDV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maLoaiVe, String maDV) {
        String sql = "DELETE FROM CHITIET_DICHVU_LOAIVE WHERE MaLoaiVe=? AND MaDV=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoaiVe);
            ps.setString(2, maDV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
