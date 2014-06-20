package com.jj.speedwave.services;

import android.location.Location;

/**
 * Interface for location update handlers.
 * 
 * @author Michael Jess
 *
 */
public interface LocationUpdateHandler {
	
	public void updateLocation(Location location);

}
