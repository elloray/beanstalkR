package com.github.elloray.beanstalkR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsynMessageHandler {

	private static BlockingQueue<Response> responses = new LinkedBlockingQueue<Response>();

	private Socket socket;

	public static BlockingQueue<Response> getResponse() {
		return responses;
	}

	public AsynMessageHandler(String host, int port) {
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void asynSendMessage(byte[] send, MsgType type)
			throws IOException {

		new Thread() {
			@Override
			public void run() {
				try {
					Response response = new Response();
					System.out.println(socket.getLocalPort());
					socket.setTcpNoDelay(true);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					out.write(send);
					out.flush();
					response.setCommand(getCommand(in));
					if (type == MsgType.DATA) {
						response.setData(getBody(in, response.getCommand()));
					}
					responses.put(response);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private byte[] getCommand(InputStream in) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int next;
		boolean flag = false;
		while (true) {
			System.out.println(in.available());
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
		// System.out.println(new String(bos.toByteArray()));
		bos.flush();
		bos.close();
		return bos.toByteArray();
	}

	private byte[] getBody(InputStream in, byte[] command) throws IOException {
		byte[] receive;
		String[] params = new String(command).split(" ");
		receive = new byte[Integer.parseInt(params[params.length - 1].trim())];
		in.read(receive);
		in.read();
		in.read();
		return receive;
	}

}
