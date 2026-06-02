package dao;

import model.HoaDonDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

	public List<HoaDonDTO> getAllHoaDon() {
	    List<HoaDonDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM HOA_DON";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            HoaDonDTO hoaDon = new HoaDonDTO(
	                rs.getString("MaHoaDon"),
	                rs.getString("MaKH"),
	                rs.getDate("NgayLapHD"),
	                rs.getDouble("ThanhTien")
	            );
	            list.add(hoaDon);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	 public boolean update(HoaDonDTO dto) {
	        String sql = "UPDATE HOA_DON SET MaKH=?, NgayLapHD=?, ThanhTien=? WHERE MaHD=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, dto.getMaKH());
	            ps.setDate(2, new java.sql.Date(dto.getNgayLapHD().getTime()));
	            ps.setDouble(3, dto.getThanhTien());
	            ps.setString(4, dto.getMaHD());
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public boolean delete(String maHD) {
	        String sql = "DELETE FROM HOA_DON WHERE MaHD=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, maHD);
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

}
