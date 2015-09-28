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
		this(servers, new HashStrategy(servers.size()));
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
		Job job = new Job(pool.submit(Commands.reserve(), MsgType.DATA)
				.getData());
		return job;
	}

	public void asynreserve() throws IOException {

		try {
			pool.asynsubmit(Commands.reserve(), MsgType.DATA);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Job> getJobs(int num) {
		List<Job> jobs = new ArrayList<Job>();
		for (int i = 0; i < num; i++) {
			Job job = new Job(pool.getResponse().getData());
			jobs.add(job);
		}
		return jobs;
	}

	public Job reserve(int timeout) {
		// TODO Auto-generated method stub
		return null;
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

	public long watch(String TubeName) throws IOException {

		for (int i = 0; i < server_num; i++) {
			pool.submit(Commands.watch(TubeName), MsgType.COMMAND);
		}
		for (int i = 0; i < server_num; i++) {
			Job job = new Job(pool.getResponse().getCommand());
		}
		return 0;
	}

	public long ignore(byte[] TubeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Job peek(JOB_TYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

	public Job peek(int JobId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> statJob(int JobId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> stat(String server) throws IOException {

		pool.submit(Commands.stats(), MsgType.DATA);
		String string = new String(pool.getResponse().getData());
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
