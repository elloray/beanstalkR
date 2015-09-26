package com.github.elloray.beanstalkR;

public interface Strategy {

	public int sharding();

	public void reset();

	public boolean retry();
}
