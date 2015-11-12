package com.github.elloray.beanstalkR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
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
			response.setHeader(getHeader(in));
			if (type == MsgType.DATA) {
				response.setData(getData(in, response, ErrorCode));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("some trouble with connecting");
		}
		return response;
	}

	private Header getHeader(InputStream in) throws IOException {

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
		Header header = new Header(bos.toByteArray());
		return header;
	}

	private byte[] getData(InputStream in, Response response,
			String ErrorCode) throws IOException {
		byte[] receive;
		String command = new String(response.getHeader().getStatus());
		/**
		 * if command line doesn't start with correct response,then don't get
		 * next data body
		 */
		if (!command.startsWith(ErrorCode)) {
			throw new IOException(command);
		}
		String[] params = response.getHeader().getInfo().split(" ");
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
