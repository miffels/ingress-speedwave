package com.jj.speedwave.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jj.speedwave.services.geo.LocationService;
import com.jj.speedwave.services.ingress.IngressListenerService;
import com.jj.speedwave.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
	
	private static final Log LOG = new Log();

	@Override
	public void onReceive(Context context, Intent intent) {
		LOG.d("Broadcast received: " + intent.getAction());
		if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
			context.stopService(new Intent(context, IngressListenerService.class));
			context.stopService(new Intent(context, LocationService.class));
		} else {
			context.startService(new Intent(context, IngressListenerService.class));
		}
	}
	
	public void registerWith(Context context) {
		context.registerReceiver(this, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		context.registerReceiver(this, new IntentFilter(Intent.ACTION_SCREEN_ON));
	}

}
