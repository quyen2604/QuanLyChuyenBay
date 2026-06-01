package model;

	public class SanBayDTO {
	    // Encapsulation: Giữ các fields là private
	    private String maSanBay;
	    private String tenSanBay;
	    private String thanhPho;

	    // Default Constructor
	    public SanBayDTO() {}

	    // Parameterized Constructor
	    public SanBayDTO(String maSanBay, String tenSanBay, String thanhPho) {
	        this.maSanBay = maSanBay;
	        this.tenSanBay = tenSanBay;
	        this.thanhPho = thanhPho;
	    }

	    // Getters and Setters
	    public String getMaSanBay() { return maSanBay; }
	    public void setMaSanBay(String maSanBay) { this.maSanBay = maSanBay; }

	    public String getTenSanBay() { return tenSanBay; }
	    public void setTenSanBay(String tenSanBay) { this.tenSanBay = tenSanBay; }

	    public String getThanhPho() { return thanhPho; }
	    public void setThanhPho(String thanhPho) { this.thanhPho = thanhPho; }
	}
	

