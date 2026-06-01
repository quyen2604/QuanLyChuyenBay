package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.DatabaseConnection;

public class CSVLoader {
    private static final String CSV_DIR = "C:\\Users\\Trang\\Downloads\\file_csv_CSDL\\";
    private static final Charset CSV_ENCODING = StandardCharsets.UTF_8;
  
    // ============================================================
    // 1. SAN_BAY – MaSB, TenSB, TinhTP
    // ============================================================
    public static void loadSanBay() {
        String sql = "INSERT INTO SAN_BAY (MaSB, TenSB, TinhTP) VALUES (?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("SAN_BAY.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 3);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]); // MaSB
                    ps.setString(2, c[1]); // TenSB
                    ps.setString(3, c[2]); // TinhTP
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("SAN_BAY", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("SAN_BAY", e); }
        printResult("SAN_BAY", r[0], r[1]);
    }

    // ============================================================
    // 2. LOAI_MAY_BAY – MaLoai, HangSX
    // ============================================================
    public static void loadLoaiMayBay() {
        String sql = "INSERT INTO LOAI_MAY_BAY (MaLoai, HangSX) VALUES (?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("LOAI_MAY_BAY.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 2);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]); // MaLoai
                    ps.setString(2, c[1]); // HangSX
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("LOAI_MAY_BAY", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("LOAI_MAY_BAY", e); }
        printResult("LOAI_MAY_BAY", r[0], r[1]);
    }

    // ============================================================
    // 3. MAY_BAY – MaMB, TenMayBay, TongSoGhe(int), MaLoai
    // ============================================================
    public static void loadMayBay() {
        String sql = "INSERT INTO MAY_BAY (MaMB, TenMayBay, TongSoGhe, MaLoai) VALUES (?, ?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("MAY_BAY.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 4);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]);                    // MaMB
                    ps.setString(2, c[1]);                    // TenMayBay
                    ps.setInt(3, Integer.parseInt(c[2]));     // TongSoGhe
                    ps.setString(4, c[3]);                    // MaLoai (FK → LOAI_MAY_BAY)
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("MAY_BAY", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("MAY_BAY", e); }
        printResult("MAY_BAY", r[0], r[1]);
    }

    // ============================================================
    // 4. LOAI_VE – MaLoaiVe, TenLoai
    // ============================================================
    public static void loadLoaiVe() {
        String sql = "INSERT INTO LOAI_VE (MaLoaiVe, TenLoai) VALUES (?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("LOAI_VE.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 2);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]); // MaLoaiVe
                    ps.setString(2, c[1]); // TenLoai
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("LOAI_VE", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("LOAI_VE", e); }
        printResult("LOAI_VE", r[0], r[1]);
    }

    // ============================================================
    // 5. DICH_VU – MaDV, TenDichVu, GiaDichVu(decimal)
    // ============================================================
    public static void loadDichVu() {
        String sql = "INSERT INTO DICH_VU (MaDV, TenDichVu, GiaDichVu) VALUES (?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("DICH_VU.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 3);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]);                    // MaDV
                    ps.setString(2, c[1]);                    // TenDichVu
                    ps.setLong(3, Long.parseLong(c[2]));      // GiaDichVu
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("DICH_VU", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("DICH_VU", e); }
        printResult("DICH_VU", r[0], r[1]);
    }

    // ============================================================
    // 6. CHITIET_DICHVU_LOAIVE – MaLoaiVe, MaDV, SoLuong(int)
    // ============================================================
    public static void loadChiTietDichVuLoaiVe() {
        String sql = "INSERT INTO CHITIET_DICHVU_LOAIVE (MaLoaiVe, MaDV, SoLuong) VALUES (?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("CHITIET_DICHVU_LOAIVE.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 3);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]);                   // MaLoaiVe (FK)
                    ps.setString(2, c[1]);                   // MaDV     (FK)
                    ps.setInt(3, Integer.parseInt(c[2]));    // SoLuong
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("CHITIET_DICHVU_LOAIVE", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("CHITIET_DICHVU_LOAIVE", e); }
        printResult("CHITIET_DICHVU_LOAIVE", r[0], r[1]);
    }

    // ============================================================
    // 7. GHE – MaGhe, MaMB, SoGhe, MaLoaiVe
    // ============================================================
    public static void loadGhe() {
        String sql = "INSERT INTO GHE (MaGhe, MaMB, SoGhe, MaLoaiVe) VALUES (?, ?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("GHE.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 4);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]); // MaGhe
                    ps.setString(2, c[1]); // MaMB     (FK → MAY_BAY)
                    ps.setString(3, c[2]); // SoGhe    (varchar: "1A", "1B",...)
                    ps.setString(4, c[3]); // MaLoaiVe (FK → LOAI_VE)
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("GHE", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("GHE", e); }
        printResult("GHE", r[0], r[1]);
    }

    // ============================================================
    // 8. KHACH_HANG – MaKH, HoTen, SDT, CCCD, DiaChi
    //    DiaChi có thể chứa dấu phẩy ("Hoàn Kiếm, Hà Nội") → splitCSVWithQuotes
    // ============================================================
    public static void loadKhachHang() {
        String sql = "INSERT INTO KHACH_HANG (MaKH, HoTen, SDT, CCCD, DiaChi) VALUES (?, ?, ?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("KHACH_HANG.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSVWithQuotes(line, 5);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]); // MaKH
                    ps.setString(2, c[1]); // HoTen
                    ps.setString(3, c[2]); // SDT
                    ps.setString(4, c[3]); // CCCD
                    ps.setString(5, c[4]); // DiaChi
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("KHACH_HANG", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("KHACH_HANG", e); }
        printResult("KHACH_HANG", r[0], r[1]);
    }

    // ============================================================
    // 9. CHUYEN_BAY – MaCB, GioKH(time), NgayKH(date), SBDen, SBDi, MaMB
    //    GioKH  csv: "21:15:00" → truyền thẳng (DB kiểu time hiểu được)
    //    NgayKH csv: "2026-01-08" → truyền thẳng (DB kiểu date hiểu được)
    // ============================================================
    public static void loadChuyenBay() {
        String sql = "INSERT INTO CHUYEN_BAY (MaCB, GioKH, NgayKH, SBDen, SBDi, MaMB) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("CHUYEN_BAY.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 6);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]); // MaCB
                    ps.setString(2, c[1]); // GioKH  "HH:mm:ss" – SQL Server time OK
                    ps.setString(3, c[2]); // NgayKH "yyyy-MM-dd" – SQL Server date OK
                    ps.setString(4, c[3]); // SBDen  (FK → SAN_BAY)
                    ps.setString(5, c[4]); // SBDi   (FK → SAN_BAY)
                    ps.setString(6, c[5]); // MaMB   (FK → MAY_BAY)
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("CHUYEN_BAY", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("CHUYEN_BAY", e); }
        printResult("CHUYEN_BAY", r[0], r[1]);
    }

    // ============================================================
    // 10. GIA_CHUYEN_BAY – MaCB, MaLoaiVe, GiaTien(decimal)
    // ============================================================
    public static void loadGiaChuyenBay() {
        String sql = "INSERT INTO GIA_CHUYEN_BAY (MaCB, MaLoaiVe, GiaTien) VALUES (?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("GIA_CHUYEN_BAY.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 3);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]);                    // MaCB     (FK)
                    ps.setString(2, c[1]);                    // MaLoaiVe (FK)
                    ps.setLong(3, Long.parseLong(c[2]));      // GiaTien
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("GIA_CHUYEN_BAY", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("GIA_CHUYEN_BAY", e); }
        printResult("GIA_CHUYEN_BAY", r[0], r[1]);
    }

    // ============================================================
    // 11. HOA_DON – MaHoaDon, MaKH, NgayLapHD(date,NOT NULL), ThanhTien(decimal)
    //     NgayLapHD csv: "2026-01-13" → truyền thẳng
    // ============================================================
    public static void loadHoaDon() {
        String sql = "INSERT INTO HOA_DON (MaHoaDon, MaKH, NgayLapHD, ThanhTien) VALUES (?, ?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("HOA_DON.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 4);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]);                    // MaHoaDon
                    ps.setString(2, c[1]);                    // MaKH      (FK)
                    ps.setString(3, c[2]);                    // NgayLapHD "yyyy-MM-dd"
                    ps.setLong(4, Long.parseLong(c[3]));      // ThanhTien
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("HOA_DON", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("HOA_DON", e); }
        printResult("HOA_DON", r[0], r[1]);
    }

    // ============================================================
    // 12. VE – MaVe, MaCB, NgayDatVe(date,NOT NULL), MaHoaDon, NgayNhanVe(date), MaGhe
    //     NgayDatVe, NgayNhanVe csv: "yyyy-MM-dd" → truyền thẳng
    // ============================================================
    public static void loadVe() {
        String sql = "INSERT INTO VE (MaVe, MaCB, NgayDatVe, MaHoaDon, NgayNhanVe, MaGhe) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        int[] r = {0, 0};
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = openFile("VE.csv")) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] c = splitCSV(line, 6);
                if (c == null) { r[1]++; continue; }
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, c[0]); // MaVe
                    ps.setString(2, c[1]); // MaCB     (FK → CHUYEN_BAY)
                    ps.setString(3, c[2]); // NgayDatVe  "yyyy-MM-dd"
                    ps.setString(4, c[3]); // MaHoaDon (FK → HOA_DON)
                    ps.setString(5, c[4]); // NgayNhanVe "yyyy-MM-dd"
                    ps.setString(6, c[5]); // MaGhe    (FK → GHE)
                    ps.executeUpdate(); r[0]++;
                } catch (SQLException e) { logErr("VE", e); r[1]++; }
            }
        } catch (Exception e) { logFileErr("VE", e); }
        printResult("VE", r[0], r[1]);
    }
    /**
     * Mở file CSV.
     * File CSV gốc có UTF-8 BOM (EF BB BF).
     * Dùng InputStreamReader để bỏ BOM tự động.
     */
    private static BufferedReader openFile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(CSV_DIR + fileName);
        // Đọc và bỏ BOM nếu có
        fis.mark(3);
        byte[] bom = new byte[3];
        int read = fis.read(bom);
        if (read < 3 || !(bom[0] == (byte)0xEF && bom[1] == (byte)0xBB && bom[2] == (byte)0xBF)) {
            fis.reset(); // không có BOM → reset về đầu file
        }
        return new BufferedReader(new InputStreamReader(fis, CSV_ENCODING));
    }

    /**
     * Tách dòng CSV đơn giản (không xử lý nháy kép).
     * Dùng cho tất cả bảng trừ KHACH_HANG.
     */
    private static String[] splitCSV(String line, int expectedCols) {
        String[] parts = line.split(",", -1);
        if (parts.length < expectedCols) return null;
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    /**
     * Tách dòng CSV có xử lý trường bọc trong nháy kép "...".
     * Dùng cho KHACH_HANG vì DiaChi chứa dấu phẩy:
     *   KH0001,Tên,...,..."Hoàn Kiếm, Hà Nội"
     */
    private static String[] splitCSVWithQuotes(String line, int expectedCols) {
        java.util.List<String> tokens = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        tokens.add(sb.toString().trim());
        if (tokens.size() < expectedCols) return null;
        return tokens.toArray(new String[0]);
    }

    private static void logErr(String table, SQLException e) {
        System.err.println("  [" + table + "] LOI: " + e.getMessage());
    }

    private static void logFileErr(String table, Exception e) {
        System.err.println("[" + table + "] Loi doc file: " + e.getMessage());
    }

    /** In kết quả load của từng bảng */
    private static void printResult(String tableName, int success, int fail) {
        System.out.printf("[%-25s] Thanh cong: %4d dong | That bai: %d dong%n",
            tableName, success, fail);
    }
}
