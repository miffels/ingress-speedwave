package com.jj.speedwave.services.geo;

import com.jj.speedwave.util.Intents;
import com.jj.speedwave.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class LocationBroadcastReceiver extends BroadcastReceiver {
	
private static final Log LOG = new Log();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LOG.d("Stopping Geo service...");
		context.stopService(new Intent(context, LocationService.class));
	}
	
	public void registerWith(LocalBroadcastManager broadcastManager) {
		broadcastManager.registerReceiver(this, new IntentFilter(Intents.INGRESS_STOPPED));
		LOG.d("Registered geo listener");
	}

}
