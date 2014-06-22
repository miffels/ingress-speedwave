package com.jj.speedwave.services.geo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.util.Intents;
import com.jj.speedwave.util.Log;

/**
 * Background service listening to location updates and broadcasting accurate fixes.
 * 
 * @author Michael Jess
 *
 */
public class LocationService extends Service implements LocationListener {
	
	private static final Log LOG = new Log();
	private LocationManager locationManager;
	private LocalBroadcastManager broadcastManager;
	
	/**
	 * Value also found in Ingress.
	 */
	private static final float MAXIMUM_ERROR = 100f;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LOG.d("Geo service started");
		
		this.init();
		this.registerLocationService();
		this.checkInitialLocation();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * Initializes the dependecies of this service.
	 */
	private void init() {
		this.locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		this.broadcastManager = LocalBroadcastManager.getInstance(this);
	}
	
	/**
	 * Registers this service with the location service.
	 */
	private void registerLocationService() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}
	
	/**
	 * Checks the accuracy of the initial GPS location and broadcasts it, if applicable.
	 */
	private void checkInitialLocation() {
		this.checkLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
	}
	
	/**
	 * Checks the accuracy of the specified location and broadcasts it, if applicable.
	 * 
	 * @param location The location to check.
	 */
	private void checkLocation(Location location) {
		if(!this.isAccurate(location)) {
			LOG.d(location == null ?
					"Location inaccurate (unavailable)" :
					"Location inaccurate (" + location.getAccuracy() + ")");
		} else {
			this.broadcastManager.sendBroadcast(
					new Intent(Intents.GEO_UPDATE)
					.putExtra(Intents.EXTRA_LOCATION, location));
		}
	}
	
	/**
	 * Tells if a location is sufficiently accurate, i.e., it must not be null, must have an accuracy value,
	 * and the accuracy value must not exceed MAXIMUM_ERROR (100).
	 * 
	 * @param location The location to check
	 * @return true, if it is accurate
	 */
	private boolean isAccurate(Location location) {
		return location != null && location.hasAccuracy() && location.getAccuracy() <= MAXIMUM_ERROR;
	}

	@Override
	public void onLocationChanged(Location location) {
		this.checkLocation(location);
	}
	
	@Override
	public void onDestroy() {
		LOG.d("Geo service stopped");
		locationManager.removeUpdates(this);
		super.onDestroy();
	}
	
	
	// **************** UNUSED METHODS *****************
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}
