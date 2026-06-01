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

    
}
