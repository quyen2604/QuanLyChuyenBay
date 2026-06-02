package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.*;
import model.*;

public class DanhMucHeThongPanel extends JPanel {

    private JTabbedPane tabbedPane;
    
    // Các TableModel để quản lý dữ liệu bảng
    private DefaultTableModel modelLoaiMayBay;
    private DefaultTableModel modelLoaiVe;
    private DefaultTableModel modelGhe;
    private DefaultTableModel modelSanBay;

    public DanhMucHeThongPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadAllData();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // ---- TAB 1: LOẠI MÁY BAY (2 CỘT) ----
        JPanel panelLMB = new JPanel(new BorderLayout());
        String[] colsLMB = {"Mã Loại Máy Bay", "Hãng Sản Xuất"}; 
        modelLoaiMayBay = new DefaultTableModel(colsLMB, 0);
        JTable tableLMB = new JTable(modelLoaiMayBay);
        panelLMB.add(new JScrollPane(tableLMB), BorderLayout.CENTER);
        tabbedPane.addTab(" Loại Máy Bay ", panelLMB);

        // ---- TAB 2: LOẠI VÉ (2 CỘT) ----
        JPanel panelLV = new JPanel(new BorderLayout());
        String[] colsLV = {"Mã Loại Vé", "Tên Loại Vé"};
        modelLoaiVe = new DefaultTableModel(colsLV, 0);
        JTable tableLV = new JTable(modelLoaiVe);
        panelLV.add(new JScrollPane(tableLV), BorderLayout.CENTER);
        tabbedPane.addTab(" Loại Vé ", panelLV);

        // ---- TAB 3: GHẾ (4 CỘT) ----
        JPanel panelGhe = new JPanel(new BorderLayout());
        String[] colsGhe = {"Mã Ghế", "Mã Máy Bay", "Số Ghế / Vị Trí", "Mã Loại Vé"};
        modelGhe = new DefaultTableModel(colsGhe, 0);
        JTable tableGhe = new JTable(modelGhe);
        panelGhe.add(new JScrollPane(tableGhe), BorderLayout.CENTER);
        tabbedPane.addTab(" Quản Lý Ghế ", panelGhe);
        
        
     // ---- TAB 4: SÂN BAY (Chỉ Read) ----
        JPanel panelSB = new JPanel(new BorderLayout());
        String[] colsSB = {"Mã Sân Bay", "Tên Sân Bay", "Thành Phố"};
        modelSanBay = new DefaultTableModel(colsSB, 0);
        JTable tableSB = new JTable(modelSanBay);
        panelSB.add(new JScrollPane(tableSB), BorderLayout.CENTER);
        tabbedPane.addTab(" Danh Sách Sân Bay ", panelSB);

        // Đẩy toàn bộ TabbedPane vào giữa giao diện chính
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Hàm tổng hợp quét toàn bộ Database đổ lên các bảng tương ứng cùng lúc
    public void loadAllData() {
        // 1. Load Loại Máy Bay
        modelLoaiMayBay.setRowCount(0);
        LoaiMayBayDAO lmbDAO = new LoaiMayBayDAO();
        List<LoaiMayBayDTO> listLMB = lmbDAO.getAllLoaiMayBay();
        for (LoaiMayBayDTO x : listLMB) {
            modelLoaiMayBay.addRow(new Object[]{x.getMaLoai(), x.getHangSX()});
        }

        // 2. Load Loại Vé
        modelLoaiVe.setRowCount(0);
        LoaiVeDAO lvDAO = new LoaiVeDAO();
        List<LoaiVeDTO> listLV = lvDAO.getAllLoaiVe();
        for (LoaiVeDTO x : listLV) {
            modelLoaiVe.addRow(new Object[]{x.getMaLoaiVe(), x.getTenLoai()});
        }

        // 3. Load Ghế
        modelGhe.setRowCount(0);
        GheDAO gheDAO = new GheDAO();
        List<GheDTO> listGhe = gheDAO.getAllGhe();
        for (GheDTO x : listGhe) {
            modelGhe.addRow(new Object[]{x.getMaGhe(),x.getMaMB(),x.getSoGhe(), x.getMaLoaiVe()});
        }
     // 4. Load Sân Bay
        modelSanBay.setRowCount(0);
        SanBayDAO sanBayDAO = new SanBayDAO();
        List<SanBayDTO> listSanBay = sanBayDAO.getAllSanBay();
        for (SanBayDTO x : listSanBay) {
            modelSanBay.addRow(new Object[]{x.getMaSB(), x.getTenSB(), x.getTinhTP()});
        }
    }
}