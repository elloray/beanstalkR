package com.github.elloray.beanstalkR.common;

import java.util.concurrent.atomic.AtomicInteger;

import com.github.elloray.beanstalkR.api.Strategy;

public class HashStrategy implements Strategy{

	private static AtomicInteger i = new AtomicInteger(0);
	
	public int sharding() {
		return i.incrementAndGet()%5;
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public boolean retry() {
		// TODO Auto-generated method stub
		return false;
	}

}
