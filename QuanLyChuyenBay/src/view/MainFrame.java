package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    
    // Sub-Panels 
    private JPanel sidebar;
    private JPanel mainContentPanel; // Vùng đệm ở giữa để thay đổi các màn hình

    // Khai báo các Panel chức năng OLTP
    private ChuyenBayPanel chuyenBayPanel;
    private DanhMucHeThongPanel danhMucPanel;
    private MayBayPanel mayBayPanel;
    
    // Khai báo các Panel chức năng DWH
    private EtlControlPanel etlControlPanel;
    private DimensionViewerPanel dimensionViewerPanel;
    private FactViewerPanel factViewerPanel;
    
    public MainFrame() {
        // 1. Setup basic configurations 
        setTitle("HỆ THỐNG QUẢN LÝ CHUYẾN BAY & DWH ENTERPRISE");
        setSize(1300, 800); 
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        // 2. Setup Layout cho Frame
        setLayout(new BorderLayout());
        
        // 3. Initialize components
        initMainContent(); // Khởi tạo nội dung trước để truyền vào sự kiện
        initSidebar();
    }
    
    private void initSidebar() {
        sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80)); // Màu Dark Blue cổ điển
        sidebar.setPreferredSize(new Dimension(280, 800));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15)); 
        
        Dimension btnSize = new Dimension(240, 40);
        Font headerFont = new Font("Arial", Font.BOLD, 14);
        Color headerColor = new Color(236, 240, 241);

        // ================= KHU VỰC OLTP =================
        JLabel lblOltp = new JLabel("--- HỆ THỐNG OLTP ---");
        lblOltp.setForeground(headerColor);
        lblOltp.setFont(headerFont);
        sidebar.add(lblOltp);

        JButton btnChuyenBay = createSidebarButton("📝 Quản Lý Chuyến Bay", btnSize);
        JButton btnMayBay = createSidebarButton("✈️ Quản Lý Máy Bay", btnSize);
        JButton btnDanhMuc = createSidebarButton("🗂️ Danh Mục Hệ Thống", btnSize);
        
        sidebar.add(btnChuyenBay);
        sidebar.add(btnMayBay);
        sidebar.add(btnDanhMuc); 

        // Khoảng cách
        sidebar.add(Box.createVerticalStrut(20));

        // ================= KHU VỰC DWH & ETL =================
        JLabel lblDwh = new JLabel("--- DATA WAREHOUSE ---");
        lblDwh.setForeground(headerColor);
        lblDwh.setFont(headerFont);
        sidebar.add(lblDwh);

        JButton btnEtl = createSidebarButton("⚙️ Điều Khiển ETL", btnSize);
        btnEtl.setBackground(new Color(39, 174, 96)); // Nhấn mạnh nút ETL
        
        JButton btnDim = createSidebarButton("🧊 Xem Dimension", btnSize);
        JButton btnFact = createSidebarButton("📈 Xem Thống Kê Fact", btnSize);

        sidebar.add(btnEtl);
        sidebar.add(btnDim);
        sidebar.add(btnFact);
        
        // Đẩy thanh menu vào đúng vị trí WEST của Frame
        add(sidebar, BorderLayout.WEST); 
        
        // ================= SỰ KIỆN CLICK OLTP =================
        btnChuyenBay.addActionListener(e -> switchPanel(chuyenBayPanel));
        btnMayBay.addActionListener(e -> switchPanel(mayBayPanel));
        btnDanhMuc.addActionListener(e -> {
            switchPanel(danhMucPanel);
            danhMucPanel.loadAllData(); // Tự động load mới dữ liệu khi click
        });
        
        // ================= SỰ KIỆN CLICK DWH =================
        btnEtl.addActionListener(e -> switchPanel(etlControlPanel));
        btnDim.addActionListener(e -> switchPanel(dimensionViewerPanel));
        btnFact.addActionListener(e -> switchPanel(factViewerPanel));
    }

    private JButton createSidebarButton(String text, Dimension size) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(size);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
    
    // Hàm hỗ trợ chuyển đổi màn hình mượt mà
    private void switchPanel(JPanel panel) {
        mainContentPanel.removeAll(); 
        mainContentPanel.add(panel, BorderLayout.CENTER); 
        mainContentPanel.revalidate(); 
        mainContentPanel.repaint();
    }
    
    private void initMainContent() {
        // Vùng trung tâm hiển thị nội dung chi tiết
        mainContentPanel = new JPanel(new BorderLayout());
        
        // Khởi tạo các module OLTP
        chuyenBayPanel = new ChuyenBayPanel(); 
        danhMucPanel = new DanhMucHeThongPanel();
        mayBayPanel = new MayBayPanel();

        // Khởi tạo các module DWH
        etlControlPanel = new EtlControlPanel();
        dimensionViewerPanel = new DimensionViewerPanel();
        factViewerPanel = new FactViewerPanel();

        // Mặc định hiển thị màn hình Chuyến Bay (OLTP)
        mainContentPanel.add(chuyenBayPanel, BorderLayout.CENTER);
        
        add(mainContentPanel, BorderLayout.CENTER); 
    }
}