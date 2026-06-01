package ui;

import service.CSVLoader;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== BAT DAU TEST CHUC NANG LOAD CSV ===");
     
        System.out.println("========================================");
        System.out.println("  BAT DAU LOAD DU LIEU TU CSV");
        System.out.println("========================================");
        CSVLoader.loadSanBay();                // 1. Không phụ thuộc
        CSVLoader.loadLoaiMayBay();            // 2. Không phụ thuộc
        CSVLoader.loadMayBay();                // 3. Cần LOAI_MAY_BAY
        CSVLoader.loadLoaiVe();                // 4. Không phụ thuộc
        CSVLoader.loadDichVu();                // 5. Không phụ thuộc
        CSVLoader.loadChiTietDichVuLoaiVe();   // 6. Cần LOAI_VE + DICH_VU
        CSVLoader.loadGhe();                   // 7. Cần MAY_BAY + LOAI_VE
        CSVLoader.loadKhachHang();             // 8. Không phụ thuộc
        CSVLoader.loadChuyenBay();             // 9. Cần SAN_BAY + MAY_BAY
        CSVLoader.loadGiaChuyenBay();          // 10. Cần CHUYEN_BAY + LOAI_VE
        CSVLoader.loadHoaDon();                // 11. Cần KHACH_HANG
        CSVLoader.loadVe();                    // 12. Cần CHUYEN_BAY + HOA_DON + GHE
        System.out.println("========================================");
        System.out.println("  HOAN THANH LOAD DU LIEU");
        System.out.println("========================================");
        
        System.out.println("=== KET THUC TEST ===");
    }
}