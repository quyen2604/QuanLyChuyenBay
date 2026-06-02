package dao;

import model.KhachHangDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

	public List<KhachHangDTO> getAllKhachHang() {
	    List<KhachHangDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM KHACH_HANG";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            KhachHangDTO khachHang = new KhachHangDTO(
	                rs.getString("MaKH"),
	                rs.getString("HoTen"),
	                rs.getString("SDT"),
	                rs.getString("CCCD"),
	                rs.getString("DiaChi")
	            );
	            list.add(khachHang);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	 public boolean update(KhachHangDTO dto) {
	        String sql = "UPDATE KHACH_HANG SET HoTen=?, SDT=?, CCCD=?, DiaChi=? WHERE MaKH=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, dto.getHoTen());
	            ps.setString(2, dto.getSDT());
	            ps.setString(3, dto.getCCCD());
	            ps.setString(4, dto.getDiaChi());
	            ps.setString(5, dto.getMaKH());
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public boolean delete(String maKH) {
	        String sql = "DELETE FROM KHACH_HANG WHERE MaKH=?";
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, maKH);
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

}
