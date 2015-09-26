package com.github.elloray.beanstalkR.common;

public interface Strategy {

	public int sharding();

	public void reset();

	public boolean retry();
}
