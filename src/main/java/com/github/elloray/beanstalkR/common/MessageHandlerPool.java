package com.github.elloray.beanstalkR.common;

import java.util.ArrayList;
import java.util.List;

import com.github.elloray.beanstalkR.api.Strategy;

public class MessageHandlerPool {

	private static ArrayList<MessageHandler> ActivePool = new ArrayList<MessageHandler>();
	private static ArrayList<MessageHandler> UsedPool = new ArrayList<MessageHandler>();
	private static ArrayList<Response> ResponsePool = new ArrayList<Response>();

	private static Strategy strategy = null;

	private static ResponseMover mover = new ResponseMover();

	private final static MessageHandler getHandler() {
		return ActivePool.get(0);
	}

	public static void SetMessageHandlers(List<String> servers) {

		for (String server : servers) {

			String[] serverinfo = server.split(":");

			MessageHandler handler = new MessageHandler(serverinfo[0],
					Integer.parseInt(serverinfo[1]));

			ActivePool.add(handler);
		}
		mover.start();
	}

	public static void setStrategy(Strategy strategy) {
		MessageHandlerPool.strategy = strategy;
	}

	public static synchronized MessageHandler remove(MessageHandler handler) {
		UsedPool.add(handler);
		ActivePool.remove(handler);
		return UsedPool.get(UsedPool.size() - 1);
	}

	public static synchronized MessageHandler add(MessageHandler handler) {
		ActivePool.add(handler);
		UsedPool.remove(handler);
		return ActivePool.get(ActivePool.size() - 1);
	}

	public static void submit(byte[] send, MsgType type) {
		MessageHandler handler = getHandler();
		handler.setMessage(send);
		handler.setType(type);
		MessageHandlerPool.remove(getHandler()).start();
	}

	public static Response getResponse() {
		synchronized (ResponsePool) {
			if (ResponsePool.size() == 0) {
				return null;
			}
			Response response = ResponsePool.get(0);
			ResponsePool.remove(0);
			return response;
		}
	
	}

	public static void addResponse(Response response) {
		synchronized (ResponsePool) {
			ResponsePool.add(response);
		}
	}

	public static int size() {
		return ActivePool.size();
	}

	private static class ResponseMover extends Thread {
		@Override
		public void run() {

			while (true) {
				while (UsedPool.size() == 0) {
				}
				int i = 0;
				while (i < UsedPool.size()) {
					System.out.println(UsedPool.size() + ":" + i);
					if (Check(UsedPool.get(i))) {
						MessageHandler handler = UsedPool.get(i);
						MessageHandlerPool.addResponse(handler.getResponse());
						handler.reset();
						System.out.println(UsedPool.get(i).getState());
						MessageHandlerPool.add(UsedPool.get(i));
						break;
					} else {
						i++;
					}
				}
			}
		}

		private boolean Check(MessageHandler handler) {
			if (handler.getResponse() != null)
				return true;
			return false;
		}

	}
}
