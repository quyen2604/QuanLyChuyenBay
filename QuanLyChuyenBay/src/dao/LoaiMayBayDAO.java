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

    
}
