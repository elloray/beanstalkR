package com.github.elloray.beanstalkR.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.github.elloray.beanstalkR.api.BeanstalkClient;
import com.github.elloray.beanstalkR.api.Job;
import com.github.elloray.beanstalkR.api.Strategy;

public class ClientImpl implements BeanstalkClient {

	private final static String END_STRING = "\r\n";

	private final static long MIN_PROPRITY = 4294967295L;

	private final static long MAX_PROPRITY = 0L;

	public ClientImpl(List<String> servers) {
		this(servers, new HashStrategy());
	}

	public ClientImpl(List<String> servers, Strategy strategy) {
		MessageHandlerPool.setStrategy(strategy);
		MessageHandlerPool.SetMessageHandlers(servers);
	}

	public void use(String TubeName) {
		// TODO Auto-generated method stub

	}

	public int put(Job job) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Job reserve() throws IOException {
		Response response = MessageHandlerPool.getResponse();
		if(response == null){
			MessageHandlerPool.submit(("reserve" + END_STRING).getBytes(), MsgType.DATA);
		}
// 		Response response = MessageHandlerPool.getHandler().sendMessage(
//				("reserve" + END_STRING).getBytes(), MsgType.DATA);		
		while(response == null){
			response = MessageHandlerPool.getResponse();
		}
		Job job = new JobImpl(response.getData());
		System.out.println(new String(job.getData()));
		
		return job;
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
		// TODO Auto-generated method stub
//		for(int i=0;i<5;i++){
//			Response response = MessageHandlerPool.getMessageHandlers().get(i).sendMessage(
//					("watch " + TubeName + END_STRING).getBytes(), MsgType.COMMAND);
//			String[] params = new String(response.getCommand()).split(" ");
//			long count = Long.parseLong(params[1]);
//		}
		for(int i=0;i<5;i++){
			MessageHandlerPool.submit(("watch " + TubeName + END_STRING).getBytes(), MsgType.COMMAND);
		}
		
		int i = 0;
		while(true){
			if(MessageHandlerPool.getResponse() != null){
				i++;
			}
			if(i==5){
				break;
			}
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

		Response response = MessageHandlerPool.getHandler().sendMessage(
				("stats" + END_STRING).getBytes(), MsgType.DATA);
		// byte[] RetMsg = MessageHandlerFactory.sendMsg(("stats" +
		// END_STRING).getBytes(), MsgType.DATA);
		String string = new String(response.getData());
		System.out.println(string);
		Map<String, String> map = new HashMap<String, String>();
		String[] tokens = string.split("\n");
		for (int i = 2; i < tokens.length - 1; i++) {
			String[] kv = tokens[i].split(":");
			map.put(kv[0], kv[1]);
		}
		// for(String key:map.keySet()){
		// System.out.println(key+":"+map.get(key));
		// }
		return map;
	}

}
