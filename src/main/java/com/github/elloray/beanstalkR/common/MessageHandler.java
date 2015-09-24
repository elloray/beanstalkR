package com.github.elloray.beanstalkR.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageHandler extends Thread {

	private Socket socket = null;
	
	private byte[] message = null;
	private MsgType type;
	private Response response = null;
	
	public void setMessage(byte[] message) {
		this.message = message;
	}

	public void setType(MsgType type) {
		this.type = type;
	}

	public Response getResponse() {
		return this.response;
	}
	public void reset(){
		this.message = null;
		this.type = null;
		this.response = null;
	}
	

	public MessageHandler(String host, int port) {
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			this.response = sendMessage(message,type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Response sendMessage(byte[] send, MsgType type) throws IOException {

		// MessageHandlerPool.remove(this);
		Response res = new Response();

		try {
			socket.setTcpNoDelay(true);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write(send);
			out.flush();

			// while (in.available() == 0) {
			// continue;
			// }
			// receive = new byte[in.available()];
			// in.read(receive);

			res.setCommand(getCommand(in));
			if (type == MsgType.DATA) {
				res.setData(getBody(in, response.getCommand()));
			}

			// MessageHandlerPool.add(this);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("some trouble with connecting");
		}
		return res;
	}

	private byte[] getCommand(InputStream in) throws IOException {

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
