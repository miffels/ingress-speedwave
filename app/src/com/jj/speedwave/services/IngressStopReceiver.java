package com.jj.speedwave.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.util.Intents;

/**
 * Broadcast receiver that listens to INGRESS_STOPPED events and suggests its associated service
 * to stop when the event occurs.
 * 
 * @author Michael Jess
 *
 */
public class IngressStopReceiver extends BroadcastReceiver {

	private StoppableService service;
	
	public IngressStopReceiver(StoppableService service) {
		this.service = service;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.service.suggestStop();
	}
	
	public void registerWith(LocalBroadcastManager broadcastManager) {
		broadcastManager.registerReceiver(this, new IntentFilter(Intents.INGRESS_STOPPED));
	}
}
