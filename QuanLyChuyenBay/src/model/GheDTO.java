package model;

public class GheDTO {
    private String maGhe;
    private String maMB;
    private String soGhe;
    private String maLoaiVe;

    public GheDTO() {}

    public GheDTO(String maGhe, String maMB, String soGhe, String maLoaiVe) {
        this.maGhe = maGhe;
        this.maMB = maMB;
        this.soGhe = soGhe;
        this.maLoaiVe = maLoaiVe;
    }

    public String getMaGhe() { return maGhe; }
    public void setMaGhe(String maGhe) { this.maGhe = maGhe; }

    public String getMaMB() { return maMB; }
    public void setMaMB(String maMB) { this.maMB = maMB; }

    public String getSoGhe() { return soGhe; }
    public void setSoGhe(String soGhe) { this.soGhe = soGhe; }

    public String getMaLoaiVe() { return maLoaiVe; }
    public void setMaLoaiVe(String maLoaiVe) { this.maLoaiVe = maLoaiVe; }

    @Override
    public String toString() {
        return "GheDTO{maGhe='" + maGhe + "', maMB='" + maMB +
               "', soGhe=" + soGhe + ", maLoaiVe='" + maLoaiVe + "'}";
    }
}
