package com.github.elloray.beanstalkR.api;

import com.github.elloray.beanstalkR.common.JOB_TYPE;

public interface Client {

	public void use(String TubeName);

	public int put(Job job);

	public Job reserve();

	public Job reserve(int timeout);

	public int delete(int JobId);

	public int release(int JobId);
	
	public int bury(int JobId);
	
	public int touch(int JobId);
	
	public long watch(byte[] TubeName);
	
	public long ignore(byte[] TubeName);
	
	public Job peek(JOB_TYPE type);
	
	public Job peek(int JobId);
	
	
	
	

}
