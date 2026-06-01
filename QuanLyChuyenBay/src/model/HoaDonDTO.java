package model;

import java.util.Date;

public class HoaDonDTO {
    private String maHD;
    private String maKH;
    private Date ngayLapHD;
    private double thanhTien;

    public HoaDonDTO() {}

    public HoaDonDTO(String maHD, String maKH, Date ngayLapHD, double thanhTien) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.ngayLapHD = ngayLapHD;
        this.thanhTien = thanhTien;
    }

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public Date getNgayLapHD() { return ngayLapHD; }
    public void setNgayLapHD(Date ngayLapHD) { this.ngayLapHD = ngayLapHD; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    @Override
    public String toString() {
        return "HoaDonDTO{maHD='" + maHD + "', maKH='" + maKH +
               "', ngayLap=" + ngayLapHD + ", thanhTien=" + thanhTien + "}";
    }
}
