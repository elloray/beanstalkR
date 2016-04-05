package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.github.elloray.beanstalkR.exception.ServerUnusedException;
import com.github.elloray.beanstalkR.handler.MessageHandlerPool;
import com.github.elloray.beanstalkR.strategy.RoundRobinStrategy;
import com.github.elloray.beanstalkR.strategy.Strategy;
import com.github.elloray.beanstalkR.util.BeanstalkMessage;
import com.github.elloray.beanstalkR.util.Job;
import com.github.elloray.beanstalkR.util.MsgType;
import com.github.elloray.beanstalkR.util.Response;
import com.github.elloray.beanstalkR.util.ResultCode;

public class BeanstalkClient {

  private MessageHandlerPool pool = null;

  private int server_num;

  public BeanstalkClient(List<String> servers) {
    this(servers, new RoundRobinStrategy(servers.size()));
    server_num = servers.size();
  }

  public BeanstalkClient(List<String> servers, Strategy strategy) {
    try {
      pool = new MessageHandlerPool(servers, strategy);
    } catch (Exception e) {
      throw new ServerUnusedException(e.getMessage());
    }

  }

  public void use(String TubeName) throws IOException {
    for (int i = 0; i < server_num; i++) {
      pool.submit(BeanstalkMessage.use(TubeName), MsgType.COMMAND,
          ResultCode.USE_OK);
    }
  }

  public int put(Job job) throws IOException {
    pool.submit(BeanstalkMessage.put(job), MsgType.COMMAND, ResultCode.PUT_OK);
    return 0;
  }

  public int asynput(Job job) throws IOException, InterruptedException {
    pool.asynsubmit(BeanstalkMessage.put(job), MsgType.COMMAND,
        ResultCode.PUT_OK);
    return 0;
  }

  public Job reserve() throws IOException {
    Response response = pool.submit(BeanstalkMessage.reserve(), MsgType.DATA,
        ResultCode.RESERVE_OK);
    Job job = new Job(response.getData());
    job.setJobId(Integer.parseInt(response.getHeader().getInfo()
        .split(BeanstalkMessage.SPACE)[0]));
    return job;
  }

  public void asynreserve() throws IOException, InterruptedException {
    pool.asynsubmit(BeanstalkMessage.reserve(), MsgType.DATA,
        ResultCode.RESERVE_OK);
  }

  public List<Job> getJobs(int num) throws InterruptedException,
      ExecutionException {
    List<Job> jobs = new ArrayList<Job>();
    for (int i = 0; i < num; i++) {
      Response response = pool.getResponse();
      Job job = new Job(response.getData());
      job.setJobId(Integer.parseInt(response.getHeader().getInfo()
          .split(BeanstalkMessage.SPACE)[0]));
      jobs.add(job);
    }
    return jobs;
  }

  public Job reserve(int timeout) throws IOException {
    Response response = pool.submit(
        BeanstalkMessage.reservewithTimeout(timeout), MsgType.DATA,
        ResultCode.RESERVE_OK);
    Job job = new Job(response.getData());
    job.setJobId(Integer.parseInt(response.getHeader().getInfo()
        .split(BeanstalkMessage.SPACE)[0]));
    return job;
  }

  public int delete(Job job) throws IOException {
    pool.submit(BeanstalkMessage.delete(job.getJobId()), MsgType.COMMAND,
        ResultCode.DELETE_OK);
    return 0;
  }

  public int release(int JobId) {
    return 0;
  }

  public int bury(int JobId) {
    return 0;
  }

  public int touch(int JobId) {
    return 0;
  }

  public long watch(String TubeName) throws IOException, InterruptedException,
      ExecutionException {
    for (int i = 0; i < server_num; i++) {
      pool.submit(BeanstalkMessage.watch(TubeName), MsgType.COMMAND,
          ResultCode.WATCH_OK);
    }
    return 0;
  }

  public long ignore(byte[] TubeName) {
    return 0;
  }

  public Job peek(int JobId) {
    return null;
  }

  public Map<String, String> statJob(int JobId) {
    return null;
  }

  public Map<String, String> stats(String server) throws IOException,
      InterruptedException, ExecutionException {
    Response response = pool.submit(BeanstalkMessage.stats(), MsgType.DATA,
        ResultCode.STATS_OK);
    String string = new String(response.getData());
    Map<String, String> map = new HashMap<String, String>();
    String[] tokens = string.split("\n");
    for (int i = 2; i < tokens.length - 1; i++) {
      String[] kv = tokens[i].split(":");
      map.put(kv[0], kv[1]);
    }
    return map;
  }

  public void stop() {
    pool.stop();
  }

}
