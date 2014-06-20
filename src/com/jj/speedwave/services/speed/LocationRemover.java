package com.jj.speedwave.services.speed;

import java.util.Collection;

import android.location.Location;

import com.jj.speedwave.util.Log;

/**
 * Runnable for scheduling in order to remove a location from a collection of locations.
 * 
 * @author Michael Jess
 *
 */
public class LocationRemover implements Runnable {
	
	private static final Log LOG = new Log();
	
	private Collection<Location> locations;
	private Location location;
	
	public LocationRemover(Collection<Location> locations, Location location) {
		this.locations = locations;
		this.location = location;
	}

	@Override
	public void run() {
		this.locations.remove(this.location);
		LOG.d("Removed a location. " + this.locations.size() + " remaining.");
	}
	
}
