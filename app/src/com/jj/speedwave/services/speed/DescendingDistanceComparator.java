package com.jj.speedwave.services.speed;

import java.util.Comparator;

import android.location.Location;

/**
 * Comparator to sort locations based on their distance to a reference location.
 * 
 * @author Michael Jess
 *
 */
public class DescendingDistanceComparator implements Comparator<Location> {
	
	private Location currentLocation;
	
	public DescendingDistanceComparator(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	@Override
	public int compare(Location lhs, Location rhs) {
		float diff = this.currentLocation.distanceTo(rhs) - this.currentLocation.distanceTo(lhs);
		return diff < 0 ? -1 : diff > 0 ? 1 : 0;
	}

}
