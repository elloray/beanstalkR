package com.github.elloray.beanstalkR;

public class Commands {

	private final static String END_STRING = "\r\n";
	private final static String SPACE = " ";

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
	
	public final static byte[] put(int pri, int delay, int ttr, int bytes,
			byte[] data) {
		return ("put" + SPACE + String.valueOf(pri) + SPACE
				+ String.valueOf(delay) + SPACE + String.valueOf(ttr) + SPACE + String
					.valueOf(bytes)).getBytes();
	}
}
