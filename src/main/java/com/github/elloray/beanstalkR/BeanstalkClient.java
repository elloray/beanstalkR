package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class BeanstalkClient {

	private final static long MIN_PROPRITY = 4294967295L;

	private final static long MAX_PROPRITY = 0L;

	private MessageHandlerPool pool = null;

	private int server_num;

	public BeanstalkClient(List<String> servers) {
		this(servers, new RoundRobinStrategy(servers.size()));
		server_num = servers.size();
	}

	public BeanstalkClient(List<String> servers, Strategy strategy) {
		pool = new MessageHandlerPool(servers, strategy);
	}

	public void use(String TubeName) {

	}

	public int put(Job job) {
		return 0;
	}

	public Job reserve() throws IOException {
		Job job = new Job(pool.submit(Commands.reserve(), MsgType.DATA,
				ResultCode.RESERVE_OK).getDataBody());
		return job;
	}

	public void asynreserve() throws IOException {
		try {
			pool.asynsubmit(Commands.reserve(), MsgType.DATA,
					ResultCode.RESERVE_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Job> getJobs(int num) throws InterruptedException,
			ExecutionException {
		List<Job> jobs = new ArrayList<Job>();
		for (int i = 0; i < num; i++) {
			Response response = pool.getResponse();
			Job job = new Job(response.getDataBody());
//			 job.setJobId();
			jobs.add(job);
		}
		return jobs;
	}

	public Job reserve(int timeout) throws IOException {
		Job job = new Job(pool.submit(Commands.reservewithTimeout(timeout),
				MsgType.DATA, ResultCode.RESERVE_OK).getDataBody());
		return job;
	}

	public int delete(int JobId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int release(int JobId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int bury(int JobId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int touch(int JobId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long watch(String TubeName) throws IOException,
			InterruptedException, ExecutionException {

		for (int i = 0; i < server_num; i++) {
			pool.submit(Commands.watch(TubeName), MsgType.COMMAND, null);
		}
		for (int i = 0; i < server_num; i++) {
			Job job = new Job(pool.getResponse().getCommandBody());
		}
		return 0;
	}

	public long ignore(byte[] TubeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Job peek(int JobId) {
		return null;
	}

	public Map<String, String> statJob(int JobId) {
		return null;
	}

	public Map<String, String> stat(String server) throws IOException,
			InterruptedException, ExecutionException {
		pool.submit(Commands.stats(), MsgType.DATA, ResultCode.STATS_OK);
		String string = new String(pool.getResponse().getDataBody());
		Map<String, String> map = new HashMap<String, String>();
		String[] tokens = string.split("\n");
		for (int i = 2; i < tokens.length - 1; i++) {
			String[] kv = tokens[i].split(":");
			map.put(kv[0], kv[1]);
		}
		for (String key : map.keySet()) {
			System.out.println(key + ":" + map.get(key));
		}
		return map;
	}

	public void stop() {
		pool.stop();
	}
}
