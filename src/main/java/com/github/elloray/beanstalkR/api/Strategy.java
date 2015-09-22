package com.github.elloray.beanstalkR.api;

public interface Strategy {

	public int sharding();

	public void reset();

	public boolean retry();
}
