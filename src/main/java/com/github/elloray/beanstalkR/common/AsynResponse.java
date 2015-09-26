package com.github.elloray.beanstalkR.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class AsynResponse implements Callable<Response>{

	private Response response;
	private InputStream in;
	private MsgType type;
	
	public AsynResponse(Response response,InputStream in,MsgType type) {
		this.response = response;
		this.in =in;
		this.type = type;
	}
	@Override
	public Response call() throws Exception {
		try {
			response.setCommand(getCommand(in));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (type == MsgType.DATA) {
			try {
				response.setData(getBody(in, response.getCommand()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
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
