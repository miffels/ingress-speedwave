package com.jj.speedwave.services.speed;

public class DefaultTimeProvider implements TimeProvider {

	@Override
	public long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

}
