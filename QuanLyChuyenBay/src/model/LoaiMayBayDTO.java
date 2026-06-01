package model;

public class LoaiMayBayDTO {
    private String maLoai;
    private String hangSX;

    public LoaiMayBayDTO() {}

    public LoaiMayBayDTO(String maLoai, String hangSX) {
        this.maLoai = maLoai;
        this.hangSX = hangSX;
    }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public String getHangSX() { return hangSX; }
    public void setHangSX(String hangSX) { this.hangSX = hangSX; }

    @Override
    public String toString() {
        return "LoaiMayBayDTO{maLoai='" + maLoai + "', hangSX='" + hangSX + "'}";
    }
}
