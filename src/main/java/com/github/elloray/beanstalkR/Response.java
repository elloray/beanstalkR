package com.github.elloray.beanstalkR;

public class Response {
	
	private byte[] command;
	
	private byte[] data;

	public byte[] getCommandBody() {
		return command;
	}

	public void setCommandBody(byte[] command) {
		this.command = command;
	}

	public byte[] getDataBody() {
		return data;
	}

	public void setDataBody(byte[] data) {
		this.data = data;
	}
}
