package com.github.elloray.beanstalkR.api;

public interface Job {
	
	public void setData(String data);
	
	public byte[] getData();

	public void setData(byte[] byteArray);
	
	public void setPriority(long priority);

}
