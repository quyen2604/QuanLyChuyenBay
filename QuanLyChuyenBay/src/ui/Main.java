package ui;

import java.util.List;
import dao.SanBayDAO;
import model.SanBayDTO;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== STARTING AIRPORT DATA RETRIEVAL DEMO ===");
        
        // 1. Initialize the Data Access Object (DAO)
        SanBayDAO sanBayDAO = new SanBayDAO();
        
        // 2. Execute the query to fetch data from SQL Server
        System.out.println("[Info] Fetching data from database...");
        List<SanBayDTO> danhSachSanBay = sanBayDAO.getAllSanBay();
        
        // 3. Verify the output (Data Validation)
        if (danhSachSanBay.isEmpty()) {
            System.out.println("[Warning] No data found or connection failed. Check your DB record!");
        } else {
            System.out.println("[Success] Retrieved " + danhSachSanBay.size() + " airports.");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-12s | %-30s | %-20s\n", "MA SAN BAY", "TEN SAN BAY", "THANH PHO");
            System.out.println("--------------------------------------------------");
            
            // Loop through the Data Transfer Objects (DTO)
//            for (SanBayDTO sb : danhSachSanBay) {
//                System.out.printf("%-12s | %-30s | %-20s\n", 
//                    sb.getMaSanBay(), 
//                    sb.getTenSanBay(), 
//                    sb.getThanhPho());
//            }
            System.out.println("--------------------------------------------------");
        }
        
        System.out.println("=== DEMO FINISHED ===");
    }
}