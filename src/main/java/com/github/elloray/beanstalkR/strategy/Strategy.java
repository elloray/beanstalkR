package com.github.elloray.beanstalkR.strategy;

public interface Strategy {

	public int sharding();

	public void reset();

	public boolean retry();
}
