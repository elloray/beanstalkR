package com.github.elloray.beanstalkR;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements Strategy{

	private AtomicInteger i = new AtomicInteger(0);
	
	private int mod = 1;
	
	public RoundRobinStrategy(int mod){
		this.mod = mod;
	}
	
	public int sharding() {
		return i.incrementAndGet()%mod;
	}

	public void reset() {
		i.set(0);
	}

	public boolean retry() {
		return false;
	}

}
