package com.github.elloray.beanstalkR;

public class ResultCode {
	//Correct Result Code
	final static String RESERVE_OK = "RESERVED";
	final static String PUT_OK = "INSERTED";
	final static String STATS_OK = "OK";
	
	
	//Incorrect Result Code
	final static String RESERVE_DEADLINE_SOON = "DEADLINE_SOON";
	final static String RESERVE_TIMED_OUT = "TIMED_OUT";
	final static String PUT_BURIED = "BURIED";
	final static String PUT_EXPECTED_CRLF = "EXPECTED_CRLF";
	final static String PUT_JOB_TOO_BIG = "JOB_TOO_BIG";
}
