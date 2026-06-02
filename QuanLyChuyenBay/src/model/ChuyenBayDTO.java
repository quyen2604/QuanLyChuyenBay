package model;

import java.sql.Time;
import java.util.Date;

public class ChuyenBayDTO {
    private String maCB;
    private Time gioKH;
    private Date ngayKH;
    private String SBDen;
    private String SBDi;
    private String maMB;

    public ChuyenBayDTO() {}

    public ChuyenBayDTO(String maCB, Time gioKH, Date ngayKH, String SBDen, String SBDi, String maMB) {
        this.maCB = maCB;
        this.gioKH = gioKH;
        this.ngayKH = ngayKH;
        this.SBDen = SBDen;
        this.SBDi = SBDi;
        this.maMB = maMB;
    }

    public String getMaCB() { return maCB; }
    public void setMaCB(String maCB) { this.maCB = maCB; }

    public Time getGioKH() { return gioKH; }
    public void setGioKH(Time gioKH) { this.gioKH = gioKH; }

    public Date getNgayKH() { return ngayKH; }
    public void setNgayKH(Date ngayKH) { this.ngayKH = ngayKH; }

    public String getSBDen() { return SBDen; }
    public void setSBDen(String SBDen) { this.SBDen = SBDen; }

    public String getSBDi() { return SBDi; }
    public void setSBDi(String SBDi) { this.SBDi = SBDi; }

    public String getMaMB() { return maMB; }
    public void setMaMB(String maMB) { this.maMB = maMB; }

    @Override
    public String toString() {
        return "ChuyenBayDTO{maCB='" + maCB + "', ngayKH=" + ngayKH +
               ", SBDi='" + SBDi + "', SBDen='" + SBDen + "', maMB='" + maMB + "'}";
    }
}
