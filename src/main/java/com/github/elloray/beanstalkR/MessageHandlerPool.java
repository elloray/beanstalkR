package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.ietf.jgss.Oid;

public class MessageHandlerPool {

	private ArrayList<MessageHandler> messageHandlers = new ArrayList<MessageHandler>();

	private Strategy strategy = null;

	private ExecutorService executor;

	private BlockingQueue<Future<Response>> futures = new LinkedBlockingQueue<Future<Response>>();

	public MessageHandlerPool(List<String> servers, Strategy strategy) {
		this.strategy = strategy;
		for (String server : servers) {
			// init pool
			String[] serverinfo = server.split(":");
			messageHandlers.add(new MessageHandler(serverinfo[0], Integer
					.parseInt(serverinfo[1])));
		}
		// init asyn submit pool
		executor = Executors.newFixedThreadPool(servers.size());
	}

	public Response submit(byte[] send, MsgType type, String ErrorCode)
			throws IOException {
		return messageHandlers.get(strategy.sharding()).sendMessage(send, type,
				ErrorCode);
	}

	public void asynsubmit(byte[] send, MsgType type, String ErrorCode)
			throws IOException, InterruptedException {
		Future<Response> response = executor.submit(new Callable<Response>() {
			@Override
			public Response call() throws Exception {
				return messageHandlers.get(strategy.sharding()).sendMessage(
						send, type, ErrorCode);
			}
		});
		futures.put(response);
	}

	public Response getResponse() throws InterruptedException,
			ExecutionException {
		Future<Response> future = futures.take();
		// wait for getting value
		while (future.get() == null) {
		}
		return future.get();
	}

	// kill executor
	public void stop() {
		executor.shutdown();
	}
}
