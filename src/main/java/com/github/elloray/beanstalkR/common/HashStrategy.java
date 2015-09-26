package com.github.elloray.beanstalkR.common;

import java.util.concurrent.atomic.AtomicInteger;

public class HashStrategy implements Strategy{

	private AtomicInteger i = new AtomicInteger(0);
	
	private int mod = 0;
	
	public HashStrategy(int mod){
		this.mod = mod;
	}
	
	public int sharding() {
		return i.incrementAndGet()%mod;
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public boolean retry() {
		// TODO Auto-generated method stub
		return false;
	}

}
