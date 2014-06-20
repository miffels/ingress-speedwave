package com.jj.speedwave.services.speed;

public interface SpeedWaveListener {
	
	public void onSpeedUpdate(boolean isTooFast, long remainingSeconds, long totalSeconds);
	public void onFinish();

}
