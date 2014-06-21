package com.jj.speedwave;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.receiver.ScreenReceiver;
import com.jj.speedwave.services.ingress.IngressListenerService;
import com.jj.speedwave.util.Log;

/**
 * Application class for service bootstrapping.
 * 
 * @author Michael Jess
 *
 */
public class IngressSpeedWave extends Application {
	
	private static final Log LOG = new Log();

	@Override
	public void onCreate() {
		LOG.d("Application started");
		new IngressStartReceiver().registerWith(LocalBroadcastManager.getInstance(this));
		new ScreenReceiver().registerWith(this);
		this.startService(new Intent(this, IngressListenerService.class));
	}

	@Override
	public void onTerminate() {
		LOG.d("Application end");
	}
	

}
