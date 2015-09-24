package com.github.elloray.beanstalkR.common;

import com.github.elloray.beanstalkR.api.Job;

public class JobImpl implements Job {

	private final static long MAX_JOB_SIZE = (long) Math.pow(2, 16);

	private final static long DEFAULT_PRIORITY = 10000L;

	private final static int DEFAULT_TIME_TO_RUN = 10000;

	private final static int DEFAULT_DELAY = 0;

	private int JobId;

	private byte[] data;

	private long priority;

	private int delay;

	private int time_to_run;

	public void setJobId(int JobId) {
		this.JobId = JobId;
	}

	public void setData(String data) {
		setData(data.getBytes());
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] byteArray) {
		data = byteArray;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public JobImpl(byte[] data) {
		this(DEFAULT_PRIORITY, DEFAULT_DELAY, DEFAULT_TIME_TO_RUN, data);
	}

	public JobImpl(long priority, byte[] data) {
		this(priority, DEFAULT_DELAY, DEFAULT_TIME_TO_RUN, data);
	}

	public JobImpl(long priority, int delay, byte[] data) {
		this(priority, delay, DEFAULT_TIME_TO_RUN, data);
	}

	public JobImpl(long priority, int delay, int time_to_run, byte[] data) {
		this.priority = priority;
		this.delay = delay;
		this.time_to_run = time_to_run;
		this.data = data;
	}

}
