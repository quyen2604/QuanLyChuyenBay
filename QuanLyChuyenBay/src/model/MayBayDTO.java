package model;

public class MayBayDTO {
    private String maMB;
    private String tenMayBay;
    private int tongSoGhe;
    private String maLoai;

    public MayBayDTO() {}

    public MayBayDTO(String maMB, String tenMayBay, int tongSoGhe, String maLoai) {
        this.maMB = maMB;
        this.tenMayBay = tenMayBay;
        this.tongSoGhe = tongSoGhe;
        this.maLoai = maLoai;
    }

    public String getMaMB() { return maMB; }
    public void setMaMB(String maMB) { this.maMB = maMB; }

    public String getTenMayBay() { return tenMayBay; }
    public void setTenMayBay(String tenMayBay) { this.tenMayBay = tenMayBay; }

    public int getTongSoGhe() { return tongSoGhe; }
    public void setTongSoGhe(int tongSoGhe) { this.tongSoGhe = tongSoGhe; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    @Override
    public String toString() {
        return "MayBayDTO{maMB='" + maMB + "', tenMayBay='" + tenMayBay +
               "', tongSoGhe=" + tongSoGhe + ", maLoai='" + maLoai + "'}";
    }
}
