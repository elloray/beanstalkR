package com.github.elloray.beanstalkR;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class clientTest {
public static void main(String[] args) throws UnknownHostException, IOException {
	Socket socket = new Socket("localhost", 9001);
	
	InputStream in = socket.getInputStream();
	OutputStream out = socket.getOutputStream();
	
	String status = "stats\r\n";
	
	out.write(status.getBytes());
	
	byte[] b = new byte[in.available()];
	in.read(b);
	
	socket.close();
}
}
