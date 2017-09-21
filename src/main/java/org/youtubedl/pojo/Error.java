package org.youtubedl.pojo;

public class Error {
	/**
	 * 错误代码
	 */
	private int code;
	
	/**
	 * 错误描述
	 */
	private String note;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Error(int code, String note) {
		super();
		this.code = code;
		this.note = note;
	}
	public Error() {
		super();
	}

	@Override
	public String toString() {
		return "Error{" +
				"code=" + code +
				", note='" + note + '\'' +
				'}';
	}
}
