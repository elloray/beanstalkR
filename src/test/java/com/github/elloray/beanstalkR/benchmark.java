package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import com.github.elloray.beanstalkR.api.BeanstalkClient;
import com.github.elloray.beanstalkR.common.ClientImpl;
import com.surftools.BeanstalkClient.Client;
import com.surftools.BeanstalkClient.Job;
import com.xiaoju.ecom.common.beanstalkd.client.BeanstalkdClient;

public class benchmark {
	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {

		int NUM = 1;
		String host = "localhost";
//		TestA(host, NUM);
//		 TestB(host, NUM);
//		 TestC(host, NUM);
		TestD(host, NUM); 
	}

	private static void TestA(String host, int NUM) {
		Client bClient = new com.surftools.BeanstalkClientImpl.ClientImpl(host,
				9001);
		// Client cClient = new com.surftools.BeanstalkClientImpl.ClientImpl(
		// "10.10.38.213", 9001);
		long start = System.currentTimeMillis();
//		bClient.useTube("test");
		bClient.watch("test");
		for (int i = 0; i < NUM; i++) {
			// if (i % 2 == 0) {
			// bClient.stats();
//			bClient.put(0, 0, 1, Integer.toString(i).getBytes());
			Job job = bClient.reserve(1);
//			System.out.println(new String(job.getData()));
			// } else {
			// cClient.stats();
			// }
		}
		System.out.println(System.currentTimeMillis() - start);
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
		BeanstalkClient client = new ClientImpl(list);
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
		BeanstalkdClient xiaojuClient = new BeanstalkdClient(list);
		xiaojuClient.watch("test");
		long start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			Job job = xiaojuClient.reserve();
		}
		System.out.println(System.currentTimeMillis() - start);
	}
	
	
	private static void TestD(String host, int NUM) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String host1 = host + ":9001";
		list.add(host1);
		list.add(host1);
		list.add(host1);
		list.add(host1);
		list.add(host1);
		// String host2 = "10.10.38.213:9001";
		// list.add(host2);
		BeanstalkClient client = new ClientImpl(list);
		client.watch("test");
		long start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			client.reserve();
			// client.stat(host2);
		}

		System.out.println(System.currentTimeMillis() - start);
	}
}
