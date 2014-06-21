package com.jj.speedwave.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jj.speedwave.util.Log;

public class BootReceiver extends BroadcastReceiver {
	
	private static final Log LOG = new Log();

	@Override
	public void onReceive(Context context, Intent intent) {
		LOG.d("Broadcast received: " + intent.getAction());
		// Calling this receiver automatically starts the actual application
		
//		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Intent serviceIntent = new Intent(context, IngressListenerService.class);
//            context.startService(serviceIntent);
//        }
	}

}
