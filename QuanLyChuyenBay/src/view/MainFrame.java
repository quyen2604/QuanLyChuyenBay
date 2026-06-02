package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    //  Sub-Panels 
    private JPanel currentPanel;
    private JPanel sidebar;
    private JPanel mainContentPanel; // Vùng đệm ở giữa để thay đổi các màn hình

    // Khai báo các Panel chức năng
    private ChuyenBayPanel chuyenBayPanel;
    private DanhMucHeThongPanel danhMucPanel;
    
    public MainFrame() {
        // 1. Setup basic configurations 
        setTitle("HỆ THỐNG QUẢN LÝ CHUYẾN BAY");
        setSize(1200, 700); 
        setLocationRelativeTo(null); // Căn giữa màn hình khi hiển thị
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tắt hẳn app khi bấm X
        
        // 2. Setup Layout cho Frame
        setLayout(new BorderLayout());
        
        // 3. Initialize components (Khởi tạo các thành phần giao diện)
        initSidebar();
        initMainContent();
    }
    
    private void initSidebar() {
        sidebar = new JPanel();
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); 
        
        JButton btnChuyenBay = new JButton("Quản Lý Chuyến Bay");
        JButton btnDanhMuc = new JButton("Danh Mục Hệ Thống");
        
        Dimension btnSize = new Dimension(200, 40);
        btnChuyenBay.setPreferredSize(btnSize);
        btnDanhMuc.setPreferredSize(btnSize);
        
        sidebar.add(btnChuyenBay);
        sidebar.add(btnDanhMuc);
        
        // Đẩy thanh menu vào đúng vị trí WEST của Frame
        add(sidebar, BorderLayout.WEST); 
        
        // 🎯 BẮT SỰ KIỆN: Lúc này các nút bấm chỉ việc gọi biến toàn cục "mainContentPanel" ra để xử lý
        btnChuyenBay.addActionListener(e -> {
            mainContentPanel.removeAll(); 
            mainContentPanel.add(chuyenBayPanel, BorderLayout.CENTER); 
            mainContentPanel.revalidate(); 
            mainContentPanel.repaint();
        });
        
        btnDanhMuc.addActionListener(e -> {
            mainContentPanel.removeAll(); 
            mainContentPanel.add(danhMucPanel, BorderLayout.CENTER); 
            
            // Tự động load mới dữ liệu khi click đổi sang tab danh mục
            danhMucPanel.loadAllData(); 
            
            mainContentPanel.revalidate(); 
            mainContentPanel.repaint();
        });
    }
    
    private void initMainContent() {
        // Vùng trung tâm hiển thị nội dung chi tiết của từng chức năng
    	mainContentPanel = new JPanel(new BorderLayout());
        
     // Nhúng thử ChuyenBayPanel vào vùng trung tâm của MainFrame để test
    	chuyenBayPanel = new ChuyenBayPanel(); 
    	danhMucPanel = new DanhMucHeThongPanel();
         mainContentPanel.add(chuyenBayPanel, BorderLayout.CENTER);
        
        add(mainContentPanel, BorderLayout.CENTER); // Đặt vùng nội dung ở giữa
    }
    
    public static void main(String[] args) {
        // Chạy UI trong Thread đặc biệt của Swing để đảm bảo Thread-safety (An toàn luồng)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true); // Chính thức hiển thị màn hình lên
            }
        });
    }
    
}