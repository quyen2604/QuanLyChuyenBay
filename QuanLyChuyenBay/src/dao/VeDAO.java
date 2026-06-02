package dao;

import model.VeDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeDAO {

	public List<VeDTO> getAllVe() {
	    List<VeDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM VE";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            VeDTO ve = new VeDTO(
	                rs.getString("MaVe"),
	                rs.getString("MaCB"),
	                rs.getDate("NgayDatVe"),
	                rs.getString("MaHoaDon"),
	                rs.getDate("NgayNhanVe"),
	                rs.getString("MaGhe")
	            );
	            list.add(ve);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	 public boolean update(VeDTO dto) {
	        String sql = "UPDATE VE SET MaCB=?, NgayDatVe=?, MaHoaDon=?, NgayNhanVe=?, MaGhe=? WHERE MaVe=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, dto.getMaCB());
	            ps.setDate(2, new java.sql.Date(dto.getNgayDatVe().getTime()));
	            ps.setString(3, dto.getMaHoaDon());
	            ps.setDate(4, dto.getNgayNhanVe() != null ? new java.sql.Date(dto.getNgayNhanVe().getTime()) : null);
	            ps.setString(5, dto.getMaGhe());
	            ps.setString(6, dto.getMaVe());
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public boolean delete(String maVe) {
	        String sql = "DELETE FROM VE WHERE MaVe=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, maVe);
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

}
