package model;

public class ChiTietDichVuLoaiVeDTO {
    private String maLoaiVe;
    private String maDV;
    private int soLuong;

    public ChiTietDichVuLoaiVeDTO() {}

    public ChiTietDichVuLoaiVeDTO(String maLoaiVe, String maDV, int soLuong) {
        this.maLoaiVe = maLoaiVe;
        this.maDV = maDV;
        this.soLuong = soLuong;
    }

    public String getMaLoaiVe() { return maLoaiVe; }
    public void setMaLoaiVe(String maLoaiVe) { this.maLoaiVe = maLoaiVe; }

    public String getMaDV() { return maDV; }
    public void setMaDV(String maDV) { this.maDV = maDV; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    @Override
    public String toString() {
        return "ChiTietDichVuLoaiVeDTO{maLoaiVe='" + maLoaiVe + "', maDV='" + maDV + "', soLuong=" + soLuong + "}";
    }
}
