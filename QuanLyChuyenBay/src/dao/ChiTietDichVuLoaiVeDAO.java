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
}
