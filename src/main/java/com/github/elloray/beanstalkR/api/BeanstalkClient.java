package com.github.elloray.beanstalkR.api;

import java.io.IOException;
import java.util.Map;

import com.github.elloray.beanstalkR.common.JOB_TYPE;

public interface BeanstalkClient {

	public void use(String TubeName);

	public int put(Job job);

	public Job reserve() throws IOException;

	public Job reserve(int timeout);

	public int delete(int JobId);

	public int release(int JobId);
	
	public int bury(int JobId);
	
	public int touch(int JobId);
	
	public long watch(String TubeName) throws IOException;
	
	public long ignore(byte[] TubeName);
	
	public Job peek(JOB_TYPE type);
	
	public Job peek(int JobId);
	
	public Map<String, String> statJob(int JobId);
	
	public Map<String, String> stat(String server) throws IOException; 
	
}
