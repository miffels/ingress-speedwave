package com.jj.speedwave.services.speed;

import java.util.Vector;

import android.location.Location;

import com.jj.speedwave.util.Log;

// Just a sample to check if the concept is fine
public class SpeedWaveChecker implements Runnable {
	
	private static final Log LOG = new Log();
	
	private Vector<Location> locations;
	private CurrentLocationProvider currentLocationProvider;
	
	/**
	 * Speed limit in m/s (30 mph)
	 */
	private static final float SPEED_LIMIT = 30 * 1.609f / 3.6f;
	
	public SpeedWaveChecker(Vector<Location> locations,
			CurrentLocationProvider currentLocationProvider) {
		this.locations = locations;
		this.currentLocationProvider = currentLocationProvider;
	}

	@Override
	public void run() {
		Location currentLocation = this.currentLocationProvider.getCurrentLocation();
		Location startLocation;
		if(currentLocation == null || (startLocation = this.locations.get(0)) == currentLocation) {
			return;
		}
		
		long startTime = startLocation.getTime();
		long elapsedTime = (System.currentTimeMillis() - startTime)/1000;
		float distance = startLocation.distanceTo(this.currentLocationProvider.getCurrentLocation());
		
		float averageSpeed = distance/elapsedTime;
		
		LOG.d("Current average speed: " + averageSpeed + "m/s");
	}

}
