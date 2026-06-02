package model;

public class GiaChuyenBayDTO {
    private String maCB;
    private String maLoaiVe;
    private double giaTien;

    public GiaChuyenBayDTO() {}

    public GiaChuyenBayDTO(String maCB, String maLoaiVe, double giaTien) {
        this.maCB = maCB;
        this.maLoaiVe = maLoaiVe;
        this.giaTien = giaTien;
    }

    public String getMaCB() { return maCB; }
    public void setMaCB(String maCB) { this.maCB = maCB; }

    public String getMaLoaiVe() { return maLoaiVe; }
    public void setMaLoaiVe(String maLoaiVe) { this.maLoaiVe = maLoaiVe; }

    public double getGiaTien() { return giaTien; }
    public void setGiaTien(double giaTien) { this.giaTien = giaTien; }

    @Override
    public String toString() {
        return "GiaChuyenBayDTO{maCB='" + maCB + "', maLoaiVe='" + maLoaiVe + "', giaTien=" + giaTien + "}";
    }
}
