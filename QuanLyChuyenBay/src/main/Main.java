package main;


import javax.swing.SwingUtilities;

import util.ConnectionManager;
import view.MainFrame;

public class Main {
	public static void main(String[] args) {
        
        try {
        	ConnectionManager.init();
            System.out.println("[HỆ THỐNG] Đang chạy MasterETL để nạp dữ liệu từ file CSV...");
            
            MasterETL.main(new String[]{}); 
            
            System.out.println("[HỆ THỐNG] Nạp dữ liệu thành công!");
        } catch (Exception e) {
            System.err.println("[HỆ THỐNG MẤT KẾT NỐI] Lỗi nạp dữ liệu ETL ban đầu: " + e.getMessage());
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true); 
        });
    }

	
}