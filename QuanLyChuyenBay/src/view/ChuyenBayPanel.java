package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.ChuyenBayDAO;
import dao.MayBayDAO;
import model.ChuyenBayDTO;

import java.awt.*;
import java.util.ArrayList;

public class ChuyenBayPanel extends JPanel {
	
	private JTable tableChuyenBay;
	private DefaultTableModel tableModel;
	
	private JTextField txtMaChuyenBay;
	private JTextField txtGioKH;
	private JTextField txtNgayKH;
	private JTextField txtSanBayDen;
	private JTextField txtSanBayDi;
	private JComboBox<String> cbMaMB; 
	
	
	
    public ChuyenBayPanel() {
        // Sử dụng BorderLayout làm Layout chính cho Panel này
        setLayout(new BorderLayout());
        
        initNorthForm();   // Vùng phía trên: Form nhập liệu
        initCenterTable(); // Vùng ở giữa: Bảng dữ liệu (Tạm thời để trống)
        initSouthButtons(); // Vùng phía dưới: Các nút bấm hành động
    }
    private void initNorthForm() {
        // Tạo lưới gồm 3 hàng, 4 cột để xếp cặp (Label - TextField) ngay ngắn
        // Khoảng cách ngang 15px, dọc 10px để giao diện thoáng đãng
        JPanel panelForm = new JPanel(new GridLayout(3, 4, 15, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), " THÔNG TIN CHUYẾN BAY (INPUT FORM) "));
        
        // Hàng 1: Mã chuyến bay & Mã máy bay
        panelForm.add(new JLabel("Mã Chuyến Bay (*):"));
        txtMaChuyenBay = new JTextField();
        panelForm.add(txtMaChuyenBay);
        
        panelForm.add(new JLabel("Giờ KH (HH:mm:ss):"));
        txtGioKH = new JTextField();
        panelForm.add(txtGioKH);
        
        // Hàng 2: Sân bay đi & Sân bay đến
        panelForm.add(new JLabel("Ngày KH (YYYY-MM-DD):"));
        txtNgayKH = new JTextField();
        panelForm.add(txtNgayKH);
        
        panelForm.add(new JLabel("Sân Bay Đi:"));
        txtSanBayDi = new JTextField();
        panelForm.add(txtSanBayDi);
        
        panelForm.add(new JLabel("Sân Bay Đến:"));
        txtSanBayDen = new JTextField();
        panelForm.add(txtSanBayDen);
        
        // Hàng 3: Ngày khởi hành & Giờ khởi hành
       
        panelForm.add(new JLabel("Mã Máy Bay (*):"));
        cbMaMB = new JComboBox<>();
        MayBayDAO mbDAO = new MayBayDAO();
        java.util.List<String> dsMaMB = mbDAO.getAllMayBay().stream().map(mb -> mb.getMaMB()).toList(); // Lấy danh sách mã máy bay từ DAO
        for (String ma : dsMaMB) {
            cbMaMB.addItem(ma); // Đẩy từng mã máy bay vào hộp chọn
        }
        panelForm.add(cbMaMB);
        
        // Đặt panelForm vào vùng NORTH của ChuyenBayPanel
        add(panelForm, BorderLayout.NORTH);
    }
    
    /**
     * 2. VÙNG CENTER: Nơi sẽ chứa bảng danh sách chuyến bay sau này
     */
    private void initCenterTable() {
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), " DANH SÁCH CHUYẾN BAY (DATA VIEW) "));
        
        // Định nghĩa tên các cột hiển thị Tiếng Việt theo đúng thứ tự mảng dữ liệu
        String[] columnNames = {"Mã Chuyến Bay", "Giờ Khởi Hành", "Ngày Khởi Hành", "Sân Bay Đến", "Sân Bay Đi", "Mã Máy Bay"};
        
        // Khởi tạo TableModel với 0 hàng ban đầu
        tableModel = new DefaultTableModel(columnNames, 0);
        tableChuyenBay = new JTable(tableModel);
        
        // Bọc JTable vào JScrollPane để tự động xuất hiện thanh cuộn khi dữ liệu nhiều
        JScrollPane scrollPane = new JScrollPane(tableChuyenBay);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        
        add(panelTable, BorderLayout.CENTER);
        
        // Gọi hàm load dữ liệu ngay khi panel được tạo ra
        loadDataToTable();
    }
    public void loadDataToTable() {
        // Xóa sạch dữ liệu cũ đang hiển thị trên bảng (nếu có) để tránh bị lặp đè
        tableModel.setRowCount(0);
        
        // Gọi layer DAO lấy danh sách mới nhất từ Database
        ChuyenBayDAO dao = new ChuyenBayDAO();
        java.util.List<ChuyenBayDTO> list = dao.getAllChuyenBay();
        
        // Duyệt qua danh sách DTO và nạp từng hàng vào TableModel
        for (ChuyenBayDTO cb : list) {
            Object[] rowData = {
                cb.getMaCB(),
                cb.getGioKH(),
                cb.getNgayKH(),
                cb.getSBDen(),
                cb.getSBDi(),
                cb.getMaMB()
            };
            tableModel.addRow(rowData); // Đẩy hàng dữ liệu vào bảng hiển thị
        }
        
        
     // Bổ sung sự kiện click chuột vào JTable bên trong hàm initCenterTable()
        tableChuyenBay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = tableChuyenBay.getSelectedRow();
                if (selectedRow != -1) {
                    // Đổ ngược dữ liệu từ các cột của dòng được chọn lên Form
                    txtMaChuyenBay.setText(tableChuyenBay.getValueAt(selectedRow, 0).toString());
                    txtGioKH.setText(tableChuyenBay.getValueAt(selectedRow, 1).toString());
                    txtNgayKH.setText(tableChuyenBay.getValueAt(selectedRow, 2).toString());
                    txtSanBayDen.setText(tableChuyenBay.getValueAt(selectedRow, 3).toString());
                    txtSanBayDi.setText(tableChuyenBay.getValueAt(selectedRow, 4).toString());
                    cbMaMB.setSelectedItem(tableChuyenBay.getValueAt(selectedRow, 5).toString()); // Tự động nhảy hộp chọn về đúng mã của dòng đó
                    
                    // Khóa ô nhập Mã chuyến bay lại (Vì mã chuyến bay là Khóa chính, không được phép sửa)
                    txtMaChuyenBay.setEditable(false);
                }
            }
        });
        
    }
    
    /**
     * 3. VÙNG SOUTH: Khởi tạo các nút bấm và gán sự kiện lắng nghe (Action Listener)
     */
    private void initSouthButtons() {
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnAdd = new JButton("Thêm Chuyến Bay");
        JButton btnEdit = new JButton("Sửa Thông Tin");
        JButton btnDelete = new JButton("Xóa Chuyến Bay");
        
        // --- BẮT ĐẦU ĐOẠN CODE LẮNG NGHE SỰ KIỆN ---
        
        // 1. Lắng nghe nút Thêm
        btnAdd.addActionListener(e -> {
            // Gọi hàm xử lý logic khi Click
            handleCreateFlight();
        });
        
        // 2. Lắng nghe nút Sửa
        btnEdit.addActionListener(e -> {
            handleUpdateFlight();
        });
        
        // 3. Lắng nghe nút Xóa
        btnDelete.addActionListener(e -> {
            handleDeleteFlight();
        });
        
        // ------------------------------------------
        
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        
     // Đặt panelButtons vào vùng SOUTH của ChuyenBayPanel
        add(panelButtons, BorderLayout.SOUTH);
    }

    // Các hàm xử lý logic (Tạm thời dùng JOptionPane để test hiển thị)
    private void handleCreateFlight() {
        try {
            // 1. Trích xuất dữ liệu dạng chuỗi (String) từ các ô JTextField trên giao diện UI
            String maCB = txtMaChuyenBay.getText().trim();
            String gioKHStr = txtGioKH.getText().trim();
            String ngayKHStr = txtNgayKH.getText().trim();
            String sbDen = txtSanBayDen.getText().trim();
            String sbDi = txtSanBayDi.getText().trim();
            String maMB = cbMaMB.getSelectedItem().toString();
            
            // 2. Validation: Kiểm tra không được để trống dữ liệu cốt lõi
            if (maCB.isEmpty() || gioKHStr.isEmpty() || ngayKHStr.isEmpty() || sbDen.isEmpty() || sbDi.isEmpty() || maMB.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả các trường thông tin!", "Cảnh báo dữ liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 3. Ép kiểu dữ liệu (Data Parsing) từ String sang java.sql.Time và java.sql.Date
            // Định dạng yêu cầu: Giờ (HH:mm:ss) - Ngày (YYYY-MM-DD)
            java.sql.Time gioKH = java.sql.Time.valueOf(gioKHStr);
            java.sql.Date ngayKH = java.sql.Date.valueOf(ngayKHStr);
            
            // 4. Khởi tạo DTO - ĐẢM BẢO ĐÚNG THỨ TỰ BIẾN TRONG CONSTRUCTOR CỦA EM
            // Thứ tự: maCB -> gioKH -> ngayKH -> SBDen -> SBDi -> maMB
            ChuyenBayDTO cbDTO = new ChuyenBayDTO(maCB, gioKH, ngayKH, sbDen, sbDi, maMB);
            
            // 5. Thông báo kết quả đóng gói thành công trước khi đẩy xuống tầng DAO
            String message = String.format(
                "Thêm ChuyenBayDTO thành công!\n" +
                "- Mã CB: %s\n" +
                "- Giờ KH: %s\n" +
                "- Ngày KH: %s\n" +
                "- Sân bay đến: %s\n" +
                "- Sân bay đi: %s\n" +
                "- Mã máy bay: %s\n\n" +
                "Hệ thống chuẩn bị gọi ChuyenBayDAO để thực thi SQL...",
                cbDTO.getMaCB(), cbDTO.getGioKH(), cbDTO.getNgayKH(), cbDTO.getSBDen(), cbDTO.getSBDi(), cbDTO.getMaMB()
            );
            
            JOptionPane.showMessageDialog(this, message, "Kiểm tra Luồng Dữ Liệu (Data Flow)", JOptionPane.INFORMATION_MESSAGE);
            
            // 6. Gọi Layer DAO để thực thi (Khi em đã code xong ChuyenBayDAO)
            
            ChuyenBayDAO cbDAO = new ChuyenBayDAO();
            boolean isSuccess = cbDAO.insertChuyenBay(cbDTO);
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Thêm mới vào Database thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng kiểm tra lại trùng khóa chính.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
            
            
        } catch (IllegalArgumentException ex) {
            // Bắt lỗi nếu người dùng nhập sai định dạng chuỗi Ngày/Giờ (ví dụ nhập chữ thay vì số)
            JOptionPane.showMessageDialog(this, 
                "Sai định dạng Ngày hoặc Giờ!\n" +
                "Vui lòng nhập đúng format:\n" +
                "- Ngày khởi hành: YYYY-MM-DD (Ví dụ: 2026-06-02)\n" +
                "- Giờ khởi hành: HH:mm:ss (Ví dụ: 07:55:00)", 
                "Lỗi Định Dạng Dữ Liệu", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearForm() {
        txtMaChuyenBay.setText("");
        txtGioKH.setText("");
        txtNgayKH.setText("");
        txtSanBayDen.setText("");
        txtSanBayDi.setText("");
        if (cbMaMB.getItemCount() > 0) {
            cbMaMB.setSelectedIndex(0); 
        }
        txtMaChuyenBay.requestFocus(); // Đặt con trỏ chuột quay lại ô đầu tiên
    }

    private void handleUpdateFlight() {
        try {
            // 1. Kiểm tra xem người dùng có đang chọn dòng nào để sửa không
            int selectedRow = tableChuyenBay.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một chuyến bay trên bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 2. Gom dữ liệu mới từ các ô Form (tương tự như hàm Thêm)
            String maCB = txtMaChuyenBay.getText().trim();
            String gioKHStr = txtGioKH.getText().trim();
            String ngayKHStr = txtNgayKH.getText().trim();
            String sbDen = txtSanBayDen.getText().trim();
            String sbDi = txtSanBayDi.getText().trim();
            
            String maMB = cbMaMB.getSelectedItem().toString();
            
            if (maCB.isEmpty() || gioKHStr.isEmpty() || ngayKHStr.isEmpty() || sbDen.isEmpty() || sbDi.isEmpty() || maMB.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả các trường thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 3. Ép kiểu dữ liệu ngày giờ
            java.sql.Time gioKH = java.sql.Time.valueOf(gioKHStr);
            java.sql.Date ngayKH = java.sql.Date.valueOf(ngayKHStr);
            
            // 4. Đóng gói vào DTO
            ChuyenBayDTO cbDTO = new ChuyenBayDTO(maCB, gioKH, ngayKH, sbDen, sbDi, maMB);
            
            // 5. Gọi DAO thực thi lệnh cập nhật xuống SQL Server
            ChuyenBayDAO cbDAO = new ChuyenBayDAO();
            boolean isSuccess = cbDAO.updateChuyenBay(cbDTO);
            
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin chuyến bay thành công!");
                
                // 🔥 Tự động cập nhật bảng hiển thị mới
                loadDataToTable(); 
                
                // Mở lại ô nhập mã chuyến bay và làm sạch form
                txtMaChuyenBay.setEditable(true);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Vui lòng kiểm tra lại ràng buộc mã máy bay/sân bay.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Sai định dạng Ngày (YYYY-MM-DD) hoặc Giờ (HH:mm:ss)!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteFlight() {
        // 1. Kiểm tra xem người dùng đã chọn dòng nào trên JTable chưa
        int selectedRow = tableChuyenBay.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chuyến bay trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2. Lấy ra Mã chuyến bay (nằm ở dòng đang chọn, cột số 0)
        String maCBXoa = tableChuyenBay.getValueAt(selectedRow, 0).toString();
        
        // 3. Hỏi xác nhận trước khi xóa (Bảo vệ dữ liệu tránh người dùng ấn nhầm)
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn xóa chuyến bay: " + maCBXoa + " không?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 4. Gọi DAO để thực thi xóa dưới DB
            ChuyenBayDAO cbDAO = new ChuyenBayDAO();
            boolean isSuccess = cbDAO.deleteChuyenBay(maCBXoa); // Hàm này chúng ta sẽ viết ở DAO
            
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Xóa chuyến bay thành công!");
                
                // 🔥 Tự động cập nhật lại bảng sau khi xóa để dòng đó biến mất trên UI
                loadDataToTable(); 
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Chuyến bay này có thể đã phát sinh Vé hoặc Hóa đơn.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}