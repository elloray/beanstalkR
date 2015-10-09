package com.github.elloray.beanstalkR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageHandler {

	private Socket socket;

	private static BlockingQueue<Response> responses = new LinkedBlockingQueue<Response>();

	public MessageHandler(String host, int port) {
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized Response sendMessage(byte[] send, MsgType type,
			String ErrorCode) throws IOException {

		Response response = new Response();

		try {
			socket.setTcpNoDelay(true);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write(send);
			out.flush();
			response.setCommandBody(getCommandBody(in));
			if (type == MsgType.DATA) {
				response.setDataBody(getDataBody(in, response, ErrorCode));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("some trouble with connecting");
		}
		return response;
	}

	private byte[] getCommandBody(InputStream in) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int next;
		boolean flag = false;
		while (true) {
			next = in.read();
			if ((next == '\n') && flag) {
				break;
			}
			if (next == '\r') {
				flag = true;
			} else {
				if (flag) {
					flag = false;
					bos.write('\r');
				}
				bos.write(next);
			}
		}
		bos.flush();
		bos.close();
		return bos.toByteArray();
	}

	private byte[] getDataBody(InputStream in, Response response,
			String ErrorCode) throws IOException {
		byte[] receive;
		String command = new String(response.getCommandBody());
		/**
		 * if command line doesn't start with normal response,then don't get
		 * next data body
		 */
		if (!command.startsWith(ErrorCode)) {
			return null;
		}
		String[] params = command.split(" ");
		receive = new byte[Integer.parseInt(params[params.length - 1].trim())];
		in.read(receive);
		in.read();
		in.read();
		return receive;
	}

	public String getServerInfo() {
		return socket.getLocalAddress().getHostAddress();
	}

	public static BlockingQueue<Response> getResponse() {
		return responses;
	}
}
