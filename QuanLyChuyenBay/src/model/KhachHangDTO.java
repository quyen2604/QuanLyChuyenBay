package model;

public class KhachHangDTO {
    private String maKH;
    private String hoTen;
    private String SDT;
    private String CCCD;
    private String diaChi;

    public KhachHangDTO() {}

    public KhachHangDTO(String maKH, String hoTen, String SDT, String CCCD, String diaChi) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.SDT = SDT;
        this.CCCD = CCCD;
        this.diaChi = diaChi;
    }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSDT() { return SDT; }
    public void setSDT(String SDT) { this.SDT = SDT; }

    public String getCCCD() { return CCCD; }
    public void setCCCD(String CCCD) { this.CCCD = CCCD; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    @Override
    public String toString() {
        return "KhachHangDTO{maKH='" + maKH + "', hoTen='" + hoTen +
               "', SDT='" + SDT + "', CCCD='" + CCCD + "', diaChi='" + diaChi + "'}";
    }
}
