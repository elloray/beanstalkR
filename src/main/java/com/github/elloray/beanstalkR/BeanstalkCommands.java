package com.github.elloray.beanstalkR;

public class BeanstalkCommands {

	public final static String END_STRING = "\r\n";
	public final static String SPACE = " ";

	public final static byte[] watch(String TubeName) {
		return ("watch" + SPACE + TubeName + END_STRING).getBytes();
	}

	public final static byte[] reserve() {
		return ("reserve" + END_STRING).getBytes();
	}

	public final static byte[] reservewithTimeout(int timeout) {
		return ("reserve" + SPACE + String.valueOf(timeout) + END_STRING)
				.getBytes();
	}

	public final static byte[] stats() {
		return ("stats" + END_STRING).getBytes();
	}

	public final static byte[] statsTube(String TubeName) {
		return ("stats-tube" + SPACE + TubeName + END_STRING).getBytes();
	}

	public final static byte[] use(String TubeName) {
		return ("use" + SPACE + TubeName + END_STRING).getBytes();
	}

	public final static byte[] delete(int JobId) {
		return ("delete" + SPACE + String.valueOf(JobId) + END_STRING)
				.getBytes();
	}

	public final static byte[] put(Job job) {
		return ("put" + SPACE + String.valueOf(job.getPriority()) + SPACE
				+ String.valueOf(job.getDelay()) + SPACE
				+ String.valueOf(job.getTime_to_run()) + SPACE
				+ String.valueOf(job.getData().length) + END_STRING
				+ new String(job.getData()) + END_STRING).getBytes();
	}
}
