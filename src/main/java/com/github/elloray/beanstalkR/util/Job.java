package com.github.elloray.beanstalkR.util;

public class Job {

	private final static long MAX_JOB_SIZE = (long) Math.pow(2, 16);

	private final static long DEFAULT_PRIORITY = 10000L;

	private final static int DEFAULT_TIME_TO_RUN = 1;

	private final static int DEFAULT_DELAY = 0;

	private final static long MIN_PROPRITY = 0L;

	private final static long MAX_PROPRITY = 4294967295L;

	private int JobId;

	public int getJobId() {
		return JobId;
	}

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

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getTime_to_run() {
		return time_to_run;
	}

	public void setTime_to_run(int time_to_run) {
		this.time_to_run = time_to_run;
	}

	public long getPriority() {
		return priority;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public Job(byte[] data) {
		this(DEFAULT_PRIORITY, DEFAULT_DELAY, DEFAULT_TIME_TO_RUN, data);
	}

	public Job(long priority, byte[] data) {
		this(priority, DEFAULT_DELAY, DEFAULT_TIME_TO_RUN, data);
	}

	public Job(long priority, int delay, byte[] data) {
		this(priority, delay, DEFAULT_TIME_TO_RUN, data);
	}

	public Job(long priority, int delay, int time_to_run, byte[] data) {
		if ((priority > MAX_PROPRITY) || (priority < MIN_PROPRITY)) {
			throw new IllegalArgumentException("priority is out of range");
		}
		this.priority = priority;
		this.delay = delay;
		this.time_to_run = time_to_run;
		this.data = data;
	}

	@Override
	public String toString() {
		return new String(data);
	}

}
