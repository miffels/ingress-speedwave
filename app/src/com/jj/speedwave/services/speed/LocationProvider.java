package com.jj.speedwave.services.speed;

import java.util.List;

import android.location.Location;

/**
 * Interface for things that are able to provide the current location and a list of previous locations.
 * 
 * @author Michael Jess
 *
 */
public interface LocationProvider {
	
	/**
	 * Returns the current location.
	 * 
	 * @return the current location
	 */
	public Location getCurrentLocation();
	
	/**
	 * Returns the previous locations, not including the current location.
	 * 
	 * @return the previous locations
	 */
	public List<Location> getPreviousLocations();
	
	/**
	 * Tells if this location manager is prepared for the first speed checks, i.e., if it has received at
	 * least two location updates.
	 * 
	 * @return true if sufficient locations are available
	 */
	public boolean isReady();
	
}
