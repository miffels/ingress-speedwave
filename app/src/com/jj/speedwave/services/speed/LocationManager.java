package com.jj.speedwave.services.speed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.location.Location;

import com.jj.speedwave.services.LocationUpdateHandler;

public class LocationManager implements LocationUpdateHandler, LocationProvider {
	
	private List<Location> locations = new ArrayList<Location>();
	private Location currentLocation;
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private boolean trackPreviousLocations = true;
	
	private long numberOfUpdates = 1;
	
	@Override
	public void updateLocation(Location location) {
		synchronized(this.locations) {
			if(this.currentLocation != null && this.trackPreviousLocations) {
				this.numberOfUpdates++;
				this.locations.add(this.currentLocation);
				Collections.sort(this.locations, new DescendingDistanceComparator(this.currentLocation));
				this.scheduler.schedule(new LocationRemover(this.locations, this.currentLocation), 10, TimeUnit.MINUTES);
			}
			this.currentLocation = location;
		}		
	}

	@Override
	public Location getCurrentLocation() {
		return this.currentLocation;
	}
	
	@Override
	public List<Location> getPreviousLocations() {
		return this.locations;
	}
	
	/**
	 * Cancels all pending tasks and clears the location cache.
	 */
	public void shutdown() {
		this.scheduler.shutdown();;
		synchronized (this.locations) {
			this.locations.clear();
		}
	}

	@Override
	public boolean isReady() {
		return this.numberOfUpdates > 1;
	}
	
	/**
	 * If set to true, this location manager will track location updates as previously known locations.
	 * When set to false, only the current location will be updated and no tracking will take place.
	 * 
	 * @param trackPreviousLocations
	 */
	public void setTrackPreviousLocations(boolean trackPreviousLocations) {
		this.trackPreviousLocations = trackPreviousLocations;
	}

}
