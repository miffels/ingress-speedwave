package com.jj.speedwave.services.speed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.location.Location;

/**
 * Strategy implementing one possible Ingress speed limit check. This checker assumes that all interaction
 * is blocked whenever the player moves too fast from the furthest known previous location.
 * 
 * @author Michael Jess
 *
 */
public class SpeedWaveChecker implements Runnable {
	
	private LocationProvider locationProvider;
	private List<SpeedWaveListener> listeners = new ArrayList<SpeedWaveListener>();
	private TimeProvider timeProvider;
	
	/**
	 * Speed limit in m/s (30 mi/h * 1.609 km/mi * 3600 s/h / 1000m/km)
	 */
	private static final float SPEED_LIMIT = 30 * 1.609f / 3.6f;
	
	public SpeedWaveChecker(LocationProvider currentLocationProvider, SpeedWaveListener... listeners) {
		this(currentLocationProvider, new DefaultTimeProvider(), listeners);
	}
	
	public SpeedWaveChecker(LocationProvider currentLocationProvider, TimeProvider timeProvider, SpeedWaveListener... listeners) {
		this.locationProvider = currentLocationProvider;
		this.listeners.addAll(Arrays.asList(listeners));
		this.timeProvider = timeProvider;
	}

	@Override
	public void run() {
		if(!this.locationProvider.isReady()) {
			return;
		}
		
		Location currentLocation = this.locationProvider.getCurrentLocation();
		List<Location> locations = this.locationProvider.getPreviousLocations();
		if(locations.size() == 0) {
			this.fireOnFinish();
			return;
		}
		
		int i = 0;
		long remainingTime;
		Location remoteLocation;
		do {
			remoteLocation = locations.get(i);
			remainingTime = this.getRemainingTime(currentLocation, remoteLocation);
			i++;
		} while(i < locations.size() &&
				remainingTime < this.getRemainingTime(currentLocation, locations.get(i)));
		
		long totalTime = this.getTotalTime(currentLocation, remoteLocation);
		boolean tooFast = this.isTooFast(currentLocation, remoteLocation);
		
		this.fireOnSpeedUpdate(tooFast, remainingTime, tooFast ? totalTime : 0l);
	}
	
	private long getRemainingTime(Location currentLocation, Location remoteLocation) {
		long startTime = remoteLocation.getTime()/1000;
		long currentTime = this.timeProvider.getCurrentTimeMillis()/1000;
		long elapsedTime = currentTime - startTime;
		
		long totalTime = this.getTotalTime(currentLocation, remoteLocation);
		
		return Math.max(0l, totalTime - elapsedTime);
	}
	
	private boolean isTooFast(Location currentLocation, Location remoteLocation) {
		long startTime = remoteLocation.getTime()/1000;
		long currentTime = this.timeProvider.getCurrentTimeMillis()/1000;
		long elapsedTime = currentTime - startTime;
		float distance = remoteLocation.distanceTo(currentLocation);
		
		float averageSpeed = elapsedTime == 0 ? 0.0f : distance/elapsedTime;
		
		return averageSpeed >= SPEED_LIMIT;
	}
	
	private long getTotalTime(Location currentLocation, Location remoteLocation) {
		float distance = remoteLocation.distanceTo(currentLocation);
		
		return Math.min(600, (long)Math.ceil(distance / SPEED_LIMIT));
	}
	
	private void fireOnSpeedUpdate(boolean tooFast, long remainingSeconds, long totalSeconds) {
		for(SpeedWaveListener l : this.listeners) {
			l.onSpeedUpdate(tooFast, remainingSeconds, totalSeconds);
		}
	}
	
	private void fireOnFinish() {
		for(SpeedWaveListener l : this.listeners) {
			l.onFinish();
		}
	}
	
}
