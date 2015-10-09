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
import com.surftools.BeanstalkClientImpl.ClientImpl;
import com.xiaoju.ecom.common.beanstalkd.client.BeanstalkdClient;

public class benchmark {

	private static String tubename = "benchmark";

	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {

		int NUM = 1;
		String host = "localhost";
		
//		 prepare(host, NUM);
//		 TestA(host, NUM);
//		 System.err.println("--------------------------\n");
//		TestB(host, NUM);
		// System.err.println("--------------------------\n");
//		 TestC(host, NUM);
		// System.err.println("--------------------------\n");
		// TestD(host, NUM);
		// System.err.println("--------------------------\n");
//		 TestE(host, NUM);
	}

	public static void prepare(String host, int NUM) {
		Client bClient = new com.surftools.BeanstalkClientImpl.ClientImpl(host,
				9001);
//		Client cClient = new com.surftools.BeanstalkClientImpl.ClientImpl(host,
//				9002);
//		Client dClient = new com.surftools.BeanstalkClientImpl.ClientImpl(host,
//				9003);

		bClient.useTube(tubename);
//		cClient.useTube(tubename);
//		dClient.useTube(tubename);

		byte[] data = new byte[1000];
		Random random = new Random();
		random.nextBytes(data);
		for (int i = 0; i < NUM; i++) {
			bClient.put(1000, 0, 1, data);
//			cClient.put(1000, 0, 1, data);
//			dClient.put(1000, 0, 1, data);
		}
	}

	private static void TestA(String host, int NUM) {
		long start = System.currentTimeMillis();
		Client bClient = new com.surftools.BeanstalkClientImpl.ClientImpl(host,
				9003);
		System.out.println("init time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		bClient.watch(tubename);
		System.out.println("watch time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			Job job = bClient.reserve(1);
			// System.out.println("test"+i+":\n"+new String(job.getData()));
		}
		System.out.println("consume time : "
				+ (System.currentTimeMillis() - start));
	}

	private static void TestB(String host, int NUM) throws IOException {
		Client client = new ClientImpl(host, 9001);
		for(String key:client.stats().keySet()){
			System.out.println(key+":"+client.stats().get(key));
		}
	}

	private static void TestC(String host, int NUM) {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
		 String host2 = host + ":9002";
		 list.add(host2);      
		 String host3 = host + ":9003";
		 list.add(host3);

		long start = System.currentTimeMillis();
		BeanstalkdClient xiaojuClient = new BeanstalkdClient(list);
		System.out.println("init time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		xiaojuClient.watch(tubename);
		System.out.println("watch time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			Job job = xiaojuClient.reserve();
		}
		System.out.println("consume time : "
				+ (System.currentTimeMillis() - start));
	}

	private static void TestD(String host, int NUM) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
		// String host2 = host + ":9002";
		// list.add(host2);
		// String host3 = host + ":9003";
		// list.add(host3);
		long start = System.currentTimeMillis();
		BeanstalkClient client = new BeanstalkClient(list);
		System.out.println("init time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		client.watch(tubename);
		System.out.println("watch time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			com.github.elloray.beanstalkR.Job job = client.reserve();
			// System.out.println(new String(job.getData()));
			// client.stat(host2);
		}
		System.out.println("consume time : "
				+ (System.currentTimeMillis() - start));
		client.stop();
	}

	private static void TestE(String host, int NUM) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
//		String host2 = host + ":9002";
//		list.add(host2);
//		String host3 = host + ":9003";
//		list.add(host3);
		long start = System.currentTimeMillis();
		BeanstalkClient client = new BeanstalkClient(list);
		System.out.println("init time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		client.watch(tubename);
		System.out.println("watch time : "
				+ (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			client.asynreserve();
		}
//		System.out.println("send time : "
//				+ (System.currentTimeMillis() - start));

		// BlockingQueue<com.github.elloray.beanstalkR.Job> jobs
		// =client.getJobs();

//		start = System.currentTimeMillis();
		client.getJobs(NUM);
		client.stop();
		// System.out.println(new String(client.getJobs().take().getData()));
		System.out.println("consume time : "
				+ (System.currentTimeMillis() - start));
	}
}
