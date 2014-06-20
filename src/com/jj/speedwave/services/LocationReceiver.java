package com.jj.speedwave.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.util.Intents;

/**
 * BroadcastReceiver calling a LocationUpdateHandler whenever a location update broadcast is received.
 * 
 * @author Michael Jess
 *
 */
public class LocationReceiver extends BroadcastReceiver {

	private LocationUpdateHandler handler;
	
	public LocationReceiver(LocationUpdateHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.handler.updateLocation((Location)intent.getExtras().getParcelable(Intents.EXTRA_LOCATION));
	}
	
	public void registerWith(LocalBroadcastManager broadcastManager) {
		broadcastManager.registerReceiver(this, new IntentFilter(Intents.GEO_UPDATE));
	}
	
}
