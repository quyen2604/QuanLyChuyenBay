package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.MayBayDAO;
import dao.LoaiMayBayDAO;
import model.MayBayDTO;
import model.LoaiMayBayDTO;
import java.awt.*;

public class MayBayPanel extends JPanel {

    private JTable tableMayBay;
    private DefaultTableModel tableModel;

    private JTextField txtMaMB;
    private JTextField txtTenMB;
    private JTextField txtTongSoGhe;
    private JComboBox<String> cbMaLoai;

    private JButton btnAdd, btnEdit, btnDelete, btnClear;

    public MayBayPanel() {
        // Sử dụng BorderLayout làm Layout chính cho toàn bộ Panel
        setLayout(new BorderLayout());

        initNorthForm();   // Vùng phía trên: Form nhập liệu
        initCenterTable(); // Vùng ở giữa: Bảng dữ liệu
        initSouthButtons(); // Vùng phía dưới: Các nút bấm hành động
    }

    /**
     * 1. VÙNG NORTH: Cấu hình Form nhập liệu trải ngang theo lưới 3x4
     */
    private void initNorthForm() {
        // Tạo lưới gồm 3 hàng, 4 cột để xếp cặp (Label - Component) giống ChuyenBayPanel
        JPanel panelForm = new JPanel(new GridLayout(3, 4, 15, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), " THÔNG TIN MÁY BAY (INPUT FORM) "));

        // Hàng 1: Mã máy bay & Tên máy bay
        panelForm.add(new JLabel("Mã Máy Bay (*):"));
        txtMaMB = new JTextField();
        panelForm.add(txtMaMB);

        panelForm.add(new JLabel("Tên Máy Bay:"));
        txtTenMB = new JTextField();
        panelForm.add(txtTenMB);

        // Hàng 2: Tổng số ghế & Mã loại máy bay
        panelForm.add(new JLabel("Tổng Số Ghế:"));
        txtTongSoGhe = new JTextField();
        panelForm.add(txtTongSoGhe);

        panelForm.add(new JLabel("Mã Loại Máy Bay (*):"));
        cbMaLoai = new JComboBox<>();
        
        // Đổ dữ liệu từ LoaiMayBayDAO lên ComboBox bằng Stream API
        LoaiMayBayDAO lmbDAO = new LoaiMayBayDAO();
        java.util.List<String> dsMaLoai = lmbDAO.getAllLoaiMayBay().stream().map(lmb -> lmb.getMaLoai()).toList();
        for (String ma : dsMaLoai) {
            cbMaLoai.addItem(ma);
        }
        panelForm.add(cbMaLoai);

        // Hàng 3: Để trống các ô để giữ nguyên cấu trúc lưới 3x4 thông thoáng
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));

        // Đặt panelForm vào vùng NORTH của MayBayPanel
        add(panelForm, BorderLayout.NORTH);
    }

    /**
     * 2. VÙNG CENTER: Nơi chứa bảng danh sách máy bay và sự kiện Click dòng
     */
    private void initCenterTable() {
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), " DANH SÁCH MÁY BAY (DATA VIEW) "));

        // Định nghĩa tên các cột hiển thị
        String[] columnNames = {"Mã Máy Bay", "Tên Máy Bay", "Tổng Số Ghế", "Mã Loại"};

        // Khởi tạo TableModel
        tableModel = new DefaultTableModel(columnNames, 0);
        tableMayBay = new JTable(tableModel);
        tableMayBay.setRowHeight(22); // Chiều cao hàng đồng bộ

        // Bọc JTable vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(tableMayBay);
        panelTable.add(scrollPane, BorderLayout.CENTER);

        add(panelTable, BorderLayout.CENTER);

        // Bổ sung sự kiện click chuột đổ dữ liệu ngược lên form
        tableMayBay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = tableMayBay.getSelectedRow();
                if (selectedRow != -1) {
                    txtMaMB.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtTenMB.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtTongSoGhe.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    cbMaLoai.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());

                    // Khóa ô nhập Mã máy bay (Khóa chính)
                    txtMaMB.setEditable(false);
                }
            }
        });

        // Load dữ liệu lên bảng
        loadDataToTable();
    }

    /**
     * 3. VÙNG SOUTH: Nơi chứa các nút bấm hành động trải ngang
     */
    private void initSouthButtons() {
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnAdd = new JButton("Thêm Máy Bay");
        btnEdit = new JButton("Sửa Thông Tin");
        btnDelete = new JButton("Xóa Máy Bay");
        btnClear = new JButton("Clear Form");

        // Gán sự kiện lắng nghe
        btnAdd.addActionListener(e -> handleCreateAirplane());
        btnEdit.addActionListener(e -> handleUpdateAirplane());
        btnDelete.addActionListener(e -> handleDeleteAirplane());
        btnClear.addActionListener(e -> clearForm());

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnClear);

        // Đặt panelButtons vào vùng SOUTH
        add(panelButtons, BorderLayout.SOUTH);
    }

    public void loadDataToTable() {
        tableModel.setRowCount(0);

        MayBayDAO dao = new MayBayDAO();
        java.util.List<MayBayDTO> list = dao.getAllMayBay();

        for (MayBayDTO mb : list) {
            Object[] rowData = {
                mb.getMaMB(),
                mb.getTenMayBay(),
                mb.getTongSoGhe(),
                mb.getMaLoai()
            };
            tableModel.addRow(rowData);
        }
    }

    private void clearForm() {
        txtMaMB.setText("");
        txtTenMB.setText("");
        txtTongSoGhe.setText("");
        if (cbMaLoai.getItemCount() > 0) {
            cbMaLoai.setSelectedIndex(0);
        }
        txtMaMB.setEditable(true);
        txtMaMB.requestFocus();
    }

    private void handleCreateAirplane() {
        try {
            String maMB = txtMaMB.getText().trim();
            String tenMB = txtTenMB.getText().trim();
            String tongSoGheStr = txtTongSoGhe.getText().trim();
            String maLoai = cbMaLoai.getSelectedItem().toString();

            if (maMB.isEmpty() || tenMB.isEmpty() || tongSoGheStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả các trường thông tin!", "Cảnh báo dữ liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int tongSoGhe = Integer.parseInt(tongSoGheStr);

            MayBayDTO mbDTO = new MayBayDTO(maMB, tenMB, tongSoGhe, maLoai);

            MayBayDAO mbDAO = new MayBayDAO();
            if (mbDAO.insert(mbDTO)) {
                JOptionPane.showMessageDialog(this, "Thêm mới máy bay vào Database thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng kiểm tra lại trùng khóa chính.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tổng số ghế phải nhập đúng định dạng số nguyên!", "Lỗi Định Dạng Dữ Liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateAirplane() {
        try {
            int selectedRow = tableMayBay.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một chiếc máy bay trên bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maMB = txtMaMB.getText().trim();
            String tenMB = txtTenMB.getText().trim();
            String tongSoGheStr = txtTongSoGhe.getText().trim();
            String maLoai = cbMaLoai.getSelectedItem().toString();

            if (maMB.isEmpty() || tenMB.isEmpty() || tongSoGheStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả các trường thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int tongSoGhe = Integer.parseInt(tongSoGheStr);

            MayBayDTO mbDTO = new MayBayDTO(maMB, tenMB, tongSoGhe, maLoai);

            MayBayDAO mbDAO = new MayBayDAO();
            if (mbDAO.update(mbDTO)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin máy bay thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Kiểm tra lại ràng buộc dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tổng số ghế phải nhập đúng định dạng số nguyên!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteAirplane() {
        int selectedRow = tableMayBay.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chiếc máy bay trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maMBXoa = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa máy bay: " + maMBXoa + " không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            MayBayDAO mbDAO = new MayBayDAO();
            if (mbDAO.delete(maMBXoa)) {
                JOptionPane.showMessageDialog(this, "Xóa máy bay thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Máy bay này hiện đang có lịch trình bay hoạt động.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}