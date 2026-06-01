package model;

import java.util.Date;

public class VeDTO {
    private String maVe;
    private String maCB;
    private Date ngayDatVe;
    private String maHoaDon;
    private Date ngayNhanVe;
    private String maGhe;

    public VeDTO() {}

    public VeDTO(String maVe, String maCB, Date ngayDatVe, String maHoaDon, Date ngayNhanVe, String maGhe) {
        this.maVe = maVe;
        this.maCB = maCB;
        this.ngayDatVe = ngayDatVe;
        this.maHoaDon = maHoaDon;
        this.ngayNhanVe = ngayNhanVe;
        this.maGhe = maGhe;
    }

    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }

    public String getMaCB() { return maCB; }
    public void setMaCB(String maCB) { this.maCB = maCB; }

    public Date getNgayDatVe() { return ngayDatVe; }
    public void setNgayDatVe(Date ngayDatVe) { this.ngayDatVe = ngayDatVe; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public Date getNgayNhanVe() { return ngayNhanVe; }
    public void setNgayNhanVe(Date ngayNhanVe) { this.ngayNhanVe = ngayNhanVe; }

    public String getMaGhe() { return maGhe; }
    public void setMaGhe(String maGhe) { this.maGhe = maGhe; }

    @Override
    public String toString() {
        return "VeDTO{maVe='" + maVe + "', maCB='" + maCB +
               "', maHoaDon='" + maHoaDon + "', maGhe='" + maGhe + "'}";
    }
}
