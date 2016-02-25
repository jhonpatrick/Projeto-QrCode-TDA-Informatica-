package model;


public class ScannerQr {

	private long id;
	private String qr_code;
	private long imei_cel;
	
	public ScannerQr() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScannerQr(long id, String qr_code, long imei_cel) {
		super();
		this.id = id;
		this.qr_code = qr_code;
		this.imei_cel = imei_cel;
	}

	public ScannerQr(String qr_code, long imei_cel) {
		super();
		this.qr_code = qr_code;
		this.imei_cel = imei_cel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQr_code() {
		return qr_code;
	}

	public void setQr_code(String qr_code) {
		this.qr_code = qr_code;
	}

	public long getImei_cel() {
		return imei_cel;
	}

	public void setImei_cel(long imei_cel) {
		this.imei_cel = imei_cel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScannerQr other = (ScannerQr) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScannerQr [id=" + id + ", qr_code=" + qr_code + ", imei_cel="
				+ imei_cel + "]";
	}

}