package com.github.elloray.beanstalkR.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MessageHandlerPool {

	private static ArrayList<AsynMessageHandler> asynMessageHandlers = new ArrayList<AsynMessageHandler>();

	private ArrayList<MessageHandler> messageHandlers = new ArrayList<MessageHandler>();

	private Strategy strategy = null;

	private ExecutorService executor = Executors.newFixedThreadPool(10);

	public MessageHandlerPool(List<String> servers, Strategy strategy) {
		this.strategy = strategy;
		for (String server : servers) {
			String[] serverinfo = server.split(":");
			messageHandlers.add(new MessageHandler(serverinfo[0], Integer
					.parseInt(serverinfo[1])));
			asynMessageHandlers.add(new AsynMessageHandler(serverinfo[0],
					Integer.parseInt(serverinfo[1])));
		}
	}

	public Response submit(byte[] send, MsgType type) throws IOException {
		return messageHandlers.get(strategy.sharding()).sendMessage(send, type);
	}

	public void asynsubmit(byte[] send, MsgType type) throws IOException {
		executor.execute(new Thread() {
			@Override
			public void run() {
				try {
					asynMessageHandlers.get(strategy.sharding()).sendMessage(
							send, type);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public BlockingQueue<Response> getResponses() {
		return AsynMessageHandler.getResponse();
	}
}
