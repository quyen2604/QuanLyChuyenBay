package dao;

import model.DichVuDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAO {

	public List<DichVuDTO> getAllDichVu() {
	    List<DichVuDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM DICH_VU";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            DichVuDTO dichVu = new DichVuDTO(
	                rs.getString("MaDV"),
	                rs.getString("TenDichVu"),
	                rs.getDouble("GiaDichVu")
	            );
	            list.add(dichVu);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	 
	public boolean update(DichVuDTO dto) {
        String sql = "UPDATE DICH_VU SET TenDichVu=?, GiaDichVu=? WHERE MaDV=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getTenDichVu());
            ps.setDouble(2, dto.getGiaDichVu());
            ps.setString(3, dto.getMaDV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maDV) {
        String sql = "DELETE FROM DICH_VU WHERE MaDV=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maDV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 

}
