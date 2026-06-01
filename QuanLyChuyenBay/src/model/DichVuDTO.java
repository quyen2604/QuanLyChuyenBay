package model;

public class DichVuDTO {
    private String maDV;
    private String tenDichVu;
    private double giaDichVu;

    public DichVuDTO() {}

    public DichVuDTO(String maDV, String tenDichVu, double giaDichVu) {
        this.maDV = maDV;
        this.tenDichVu = tenDichVu;
        this.giaDichVu = giaDichVu;
    }

    public String getMaDV() { return maDV; }
    public void setMaDV(String maDV) { this.maDV = maDV; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public double getGiaDichVu() { return giaDichVu; }
    public void setGiaDichVu(double giaDichVu) { this.giaDichVu = giaDichVu; }

    @Override
    public String toString() {
        return "DichVuDTO{maDV='" + maDV + "', tenDichVu='" + tenDichVu + "', giaDichVu=" + giaDichVu + "}";
    }
}
