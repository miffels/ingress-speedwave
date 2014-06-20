package com.jj.speedwave.services.speed;

import android.location.Location;

/**
 * Interface for things that are able to provide the current location.
 * 
 * @author Michael Jess
 *
 */
public interface CurrentLocationProvider {
	
	/**
	 * Returns the current location.
	 * 
	 * @return the current location
	 */
	public Location getCurrentLocation();

}
