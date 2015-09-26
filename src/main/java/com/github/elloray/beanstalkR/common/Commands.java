package com.github.elloray.beanstalkR.common;

public class Commands {

	private final static String END_STRING = "\r\n";

	public final static byte[] watch(String TubeName) {
		return ("watch " + TubeName + END_STRING).getBytes();
	}

	public final static byte[] reserve() {
		return ("reserve" + END_STRING).getBytes();
	}

	public final static byte[] stats() {
		return ("stats" + END_STRING).getBytes();
	}

}
