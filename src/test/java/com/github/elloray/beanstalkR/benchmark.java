package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.elloray.beanstalkR.BeanstalkClient;
import com.surftools.BeanstalkClient.Client;
import com.surftools.BeanstalkClient.Job;
import com.xiaoju.ecom.common.beanstalkd.client.BeanstalkdClient;

public class benchmark {
	
	private static String tubename = "benchmark";
	
	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {

		int NUM = 1;
		String host = "localhost";
		
//		prepare(host, NUM);
//		TestA(host, NUM);
//		System.err.println("--------------------------\n");
//		 TestB(host, NUM);
//		System.err.println("--------------------------\n");
//		 TestC(host, NUM);
//		System.err.println("--------------------------\n");
//		TestD(host, NUM); 
//		System.err.println("--------------------------\n");
		TestE(host, NUM); 
	}

	public static void prepare(String host, int NUM){
		Client bClient = new com.surftools.BeanstalkClientImpl.ClientImpl(host,
				9001);
		bClient.useTube(tubename);
		
		byte[] data = new byte[1000];
		Random random = new Random();
		random.nextBytes(data);
		for (int i = 0; i < NUM; i++) {
			bClient.put(1000, 0, 1, data);
		}
	}
	
	private static void TestA(String host, int NUM) {
		long start = System.currentTimeMillis();
		Client bClient = new com.surftools.BeanstalkClientImpl.ClientImpl(host,
				9001);
		System.out.println("init time : "+(System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		bClient.watch(tubename);
		System.out.println("watch time : "+(System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			Job job = bClient.reserve(1);
//			System.out.println(new String(job.getData()));
		}
		System.out.println("consume time : "+(System.currentTimeMillis() - start));
	}

	private static void TestB(String host, int NUM) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
		list.add(host1);
		list.add(host1);
		list.add(host1);
		list.add(host1);
		// String host2 = "10.10.38.213:9001";
		// list.add(host2);
		BeanstalkClient client = new BeanstalkClient(list);
		long start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			client.stat(host1);
			// client.stat(host2);
		}

		System.out.println(System.currentTimeMillis() - start);
	}

	private static void TestC(String host, int NUM) {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
		// String host2 = "10.10.38.213:9001";
		// list.add(host2);
		
		long start = System.currentTimeMillis();
		BeanstalkdClient xiaojuClient = new BeanstalkdClient(list);
		System.out.println("init time : "+(System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		xiaojuClient.watch(tubename);
		System.out.println("watch time : "+(System.currentTimeMillis() - start));
		
		 start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			Job job = xiaojuClient.reserve();
		}
		System.out.println("consume time : "+(System.currentTimeMillis() - start));
	}
	
	
	private static void TestD(String host, int NUM) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
		// String host2 = "10.10.38.213:9001";
		// list.add(host2);
		long start = System.currentTimeMillis();
		BeanstalkClient client = new BeanstalkClient(list);
		System.out.println("init time : "+(System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		client.watch(tubename);
		System.out.println("watch time : "+(System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			client.reserve();
			// client.stat(host2);
		}
		System.out.println("consume time : "+(System.currentTimeMillis() - start));
	}
	
	private static void TestE(String host, int NUM) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
		// String host2 = "10.10.38.213:9001";
		// list.add(host2);
		long start = System.currentTimeMillis();
		BeanstalkClient client = new BeanstalkClient(list);
		System.out.println("init time : "+(System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		client.watch(tubename);
		System.out.println("watch time : "+(System.currentTimeMillis() - start));
		
		BlockingQueue<com.github.elloray.beanstalkR.Job> jobs = null;
		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			client.asynreserve();
			// client.stat(host2);
		}
		for(int i =0;i<NUM;i++){
			try {
//				client.getJobs().take();
				System.out.println(new String(client.getJobs().take().getData()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("consume time : "+(System.currentTimeMillis() - start));
	}
}
