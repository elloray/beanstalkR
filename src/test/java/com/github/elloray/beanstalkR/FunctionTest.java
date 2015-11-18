package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class FunctionTest {

	private static BeanstalkClient client = null;
	private static List<String> servers = null;

	@BeforeClass
	public static void setup() {
		servers = Arrays.asList("127.0.0.1:9001");
		client = new BeanstalkClient(servers);
	}

	@Test
	public void testPut() throws IOException {
		client.use("kk");
		for (int i = 0; i < 3; i++) {
			Job job = new Job("haha".getBytes());
			int res = client.put(job);
			Assert.assertEquals(0,res );
			;
		}
	}
@Ignore
	@Test
	public void testReserve() throws IOException, InterruptedException,
			ExecutionException {
		client.watch("kk");
		for (int i = 0; i < 6; i++) {
			Job job = client.reserve();
			Assert.assertEquals("haha", new String(job.getData()));
			client.delete(job);
		}
	}

	@Test
	public void testStats() throws IOException, InterruptedException,
			ExecutionException {
		Assert.assertNotNull(client.stats(servers.get(0)).get("cmd-reserve"));
	}

	@Ignore
	@Test
	public void testAsynPut() throws IOException, InterruptedException,
			ExecutionException {
		client.use("kk");
		for (int i = 0; i < 3; i++) {
			Job job = new Job("haha".getBytes());
			Assert.assertEquals(0, client.asynput(job));
		}
	}

	@Test
	public void testAsynReserve() throws IOException, InterruptedException,
			ExecutionException {
		client.watch("kk");
		for (int i = 0; i < 3; i++) {
			client.asynreserve();
		}
		List<Job> jobs = client.getJobs(3);
		Assert.assertEquals("haha", new String(jobs.get(0).getData()));
		client.stop();
	}
}
