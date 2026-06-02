package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    //  Sub-Panels 
    private JPanel currentPanel;
    
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
        // Nơi chứa các nút bấm chuyển màn hình (Quản lý chuyến bay, Khách hàng, Hóa đơn...)
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(250, 700));
        
        // TODO: Em sẽ thêm các JButton vào đây sau
        
        add(sidebar, BorderLayout.WEST); // Đặt sidebar nằm bên trái
    }
    
    private void initMainContent() {
        // Vùng trung tâm hiển thị nội dung chi tiết của từng chức năng
        JPanel mainContent = new JPanel(new BorderLayout());
        
     // Nhúng thử ChuyenBayPanel vào vùng trung tâm của MainFrame để test
         currentPanel = new ChuyenBayPanel(); 
         mainContent.add(currentPanel, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER); // Đặt vùng nội dung ở giữa
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