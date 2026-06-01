package model;

public class LoaiVeDTO {
    private String maLoaiVe;
    private String tenLoai;

    public LoaiVeDTO() {}

    public LoaiVeDTO(String maLoaiVe, String tenLoai) {
        this.maLoaiVe = maLoaiVe;
        this.tenLoai = tenLoai;
    }

    public String getMaLoaiVe() { return maLoaiVe; }
    public void setMaLoaiVe(String maLoaiVe) { this.maLoaiVe = maLoaiVe; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    @Override
    public String toString() {
        return "LoaiVeDTO{maLoaiVe='" + maLoaiVe + "', tenLoai='" + tenLoai + "'}";
    }
}
