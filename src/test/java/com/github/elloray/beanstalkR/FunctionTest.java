package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.surftools.BeanstalkClient.Client;
import com.surftools.BeanstalkClientImpl.ClientImpl;

public class FunctionTest {
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		
		List<String> servers = Arrays.asList("127.0.0.1:9001");
		BeanstalkClient client = new BeanstalkClient(servers);
		client.use("kk");
		Job job = new Job("haha".getBytes());
		client.put(job);
		
		
//		Client client2 = new ClientImpl("127.0.0.1", 9001);
//		client2.useTube("kk");
//		client2.put(job.getPriority(), job.getDelay(), job.getData().length, job.getData());
		
		client.watch("kk");
		Job job2 = client.reserve();
		System.out.println(job2);
		client.delete(job2);
	}
}
