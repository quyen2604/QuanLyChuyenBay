package model;

public class SanBayDTO {
    private String maSB;
    private String tenSB;
    private String tinhTP;

    public SanBayDTO() {}

    public SanBayDTO(String maSB, String tenSB, String tinhTP) {
        this.maSB = maSB;
        this.tenSB = tenSB;
        this.tinhTP = tinhTP;
    }

    public String getMaSB() { return maSB; }
    public void setMaSB(String maSB) { this.maSB = maSB; }

    public String getTenSB() { return tenSB; }
    public void setTenSB(String tenSB) { this.tenSB = tenSB; }

    public String getTinhTP() { return tinhTP; }
    public void setTinhTP(String tinhTP) { this.tinhTP = tinhTP; }

    @Override
    public String toString() {
        return "SanBayDTO{maSB='" + maSB + "', tenSB='" + tenSB + "', tinhTP='" + tinhTP + "'}";
    }
}
