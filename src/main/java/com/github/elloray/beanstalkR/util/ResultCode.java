package com.github.elloray.beanstalkR.util;

public class ResultCode {
  // Correct Result Code
  public final static String RESERVE_OK = "RESERVED";
  public final static String PUT_OK = "INSERTED";
  public final static String STATS_OK = "OK";
  public final static String DELETE_OK = "DELETED";
  public final static String WATCH_OK = "WATCHING";
  public final static String USE_OK = "USING";

  // Incorrect Result Code
  public final static String RESERVE_DEADLINE_SOON = "DEADLINE_SOON";
  public final static String RESERVE_TIMED_OUT = "TIMED_OUT";
  public final static String PUT_BURIED = "BURIED";
  public final static String PUT_EXPECTED_CRLF = "EXPECTED_CRLF";
  public final static String PUT_JOB_TOO_BIG = "JOB_TOO_BIG";
  public final static String DELETE_NOT_FOUND = "NOT_FOUND";
}
