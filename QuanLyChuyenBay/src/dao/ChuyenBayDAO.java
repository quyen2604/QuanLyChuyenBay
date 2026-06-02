package dao;

import model.ChuyenBayDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChuyenBayDAO {

	public List<ChuyenBayDTO> getAllChuyenBay() {
	    List<ChuyenBayDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM CHUYEN_BAY";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            ChuyenBayDTO chuyenBay = new ChuyenBayDTO(
	                rs.getString("MaCB"),
	                rs.getTime("GioKH"),
	                rs.getDate("NgayKH"),
	                rs.getString("SBDen"),
	                rs.getString("SBDi"),
	                rs.getString("MaMB")
	            );
	            list.add(chuyenBay);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	public boolean insertChuyenBay(ChuyenBayDTO cbDTO) {
	    // 1. Khai báo biến flag để kiểm tra trạng thái insert thành công hay thất bại
	    boolean isSuccess = false;
	    
	    // 2. Viết câu lệnh SQL với tên Bảng khớp 100% với DB của em (CHUYEN_BAY)
	    String sql = "INSERT INTO CHUYEN_BAY (MaCB, GioKH, NgayKH, SBDen, SBDi, MaMB) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    // 3. Sử dụng try-with-resources để mở và tự động giải phóng tài nguyên (Connection, PreparedStatement)
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        // 4. Set các giá trị từ đối tượng DTO vào các dấu hỏi chấm (?) theo đúng thứ tự
	        ps.setString(1, cbDTO.getMaCB());
	        ps.setTime(2, cbDTO.getGioKH());
//	        ps.setDate(3, cbDTO.getNgayKH());
	        
	     // Thay vì viết: ps.setDate(3, cbDTO.getNgayKH());
	     // Hãy viết cách chuyển đổi từ java.util.Date sang java.sql.Date thông qua Hệ thống Time Milliseconds:

	     if (cbDTO.getNgayKH() != null) {
	         ps.setDate(3, new java.sql.Date(cbDTO.getNgayKH().getTime()));
	     } else {
	         ps.setNull(3, java.sql.Types.DATE); // Phòng trường hợp ngày bị null không gây crash app
	     }
	        ps.setString(4, cbDTO.getSBDen());
	        ps.setString(5, cbDTO.getSBDi());
	        ps.setString(6, cbDTO.getMaMB());
	        
	        // 5. Thực thi câu lệnh SQL (executeUpdate trả về số dòng bị tác động trong DB)
	        int rowsAffected = ps.executeUpdate();
	        
	        // Nếu có ít nhất 1 dòng được chèn vào bảng thành công
	        if (rowsAffected > 0) {
	            isSuccess = true;
	        }
	        
	    } catch (SQLException e) {
	        // In ra lỗi chi tiết ở màn hình Console để chúng ta debug nếu có sự cố
	        e.printStackTrace();
	    }
	    
	    // 6. Trả về kết quả (true nếu thành công, false nếu thất bại)
	    return isSuccess;
	}
	public boolean deleteChuyenBay(String maCB) {
	    // 1. Khai báo biến flag để kiểm tra trạng thái xóa thành công hay thất bại
	    boolean isSuccess = false;
	    
	    // 2. Câu lệnh SQL định danh chính xác chuyến bay cần xóa qua Khóa chính
	    String sql = "DELETE FROM CHUYEN_BAY WHERE MaCB = ?";
	    
	    // 3. Sử dụng try-with-resources để tự động đóng kết nối tránh rò rỉ bộ nhớ
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        // 4. Nạp mã chuyến bay (String) vào dấu hỏi chấm (?) duy nhất
	        ps.setString(1, maCB);
	        
	        // 5. Thực thi câu lệnh xóa xuống Database
	        int rowsAffected = ps.executeUpdate();
	        
	        // Nếu số dòng bị xóa lớn hơn 0 nghĩa là đã xóa thành công trong DB
	        if (rowsAffected > 0) {
	            isSuccess = true;
	        }
	        
	    } catch (SQLException e) {
	        // In ra lỗi nếu có (Ví dụ: Lỗi liên kết khóa ngoại với bảng VÉ)
	        e.printStackTrace();
	    }
	    
	    // 6. Trả về kết quả
	    return isSuccess;
	}
	public boolean updateChuyenBay(ChuyenBayDTO cbDTO) {
	    boolean isSuccess = false;
	    
	    // Câu lệnh SQL cập nhật các trường thông tin dựa theo Mã chuyến bay (Khóa chính)
	    String sql = "UPDATE CHUYEN_BAY SET GioKH = ?, NgayKH = ?, SBDen = ?, SBDi = ?, MaMB = ? WHERE MaCB = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        // Nạp dữ liệu vào các dấu hỏi chấm theo đúng thứ tự câu lệnh trên
	        ps.setTime(1, cbDTO.getGioKH());
	        
	        // Chuyển đổi Date tương tự như hàm Insert
	        if (cbDTO.getNgayKH() != null) {
	            ps.setDate(2, new java.sql.Date(cbDTO.getNgayKH().getTime()));
	        } else {
	            ps.setNull(2, java.sql.Types.DATE);
	        }
	        
	        ps.setString(3, cbDTO.getSBDen());
	        ps.setString(4, cbDTO.getSBDi());
	        ps.setString(5, cbDTO.getMaMB());
	        ps.setString(6, cbDTO.getMaCB()); // Dấu hỏi số 6 chính là điều kiện WHERE
	        
	        int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
	            isSuccess = true;
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return isSuccess;
	}
    
}
