package com.github.elloray.beanstalkR;

public class Response {
	
	private Header header;
	
	private byte[] data;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
