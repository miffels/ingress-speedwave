package com.jj.speedwave;

import com.jj.speedwave.services.geo.LocationService;
import com.jj.speedwave.services.speed.SpeedWaveService;
import com.jj.speedwave.util.Intents;
import com.jj.speedwave.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Broadcast receiver that listens to Ingress start events and starts the location tracking services when it occurs.
 * 
 * @author Michael Jess
 *
 */
public class IngressStartReceiver extends BroadcastReceiver {
	
	private static final Log LOG = new Log();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LOG.d("Starting SpeedWave and Location services...");
		context.startService(new Intent(context, SpeedWaveService.class));
		context.startService(new Intent(context, LocationService.class));
	}
	
	public void registerWith(LocalBroadcastManager broadcastManager) {
		broadcastManager.registerReceiver(this, new IntentFilter(Intents.INGRESS_STARTED));
		LOG.d("Registered application listener");
	}

}
