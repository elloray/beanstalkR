package com.github.elloray.beanstalkR;

public class Header {
	
	private static final String SPACE = " ";
	
	private String status;
	private String info;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public Header(byte[] headerbytes){
		String header = new String(headerbytes);
		if(header.contains(SPACE)){
			String[] parts = header.split(SPACE, 2);
			setStatus(parts[0]);
			setInfo(parts[1]);
		}else {
			setStatus(header);
		}
	}
}
