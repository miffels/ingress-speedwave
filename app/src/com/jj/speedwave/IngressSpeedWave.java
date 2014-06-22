package com.jj.speedwave;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.receiver.ScreenReceiver;
import com.jj.speedwave.services.IngressStartReceiver;
import com.jj.speedwave.services.geo.LocationService;
import com.jj.speedwave.services.ingress.IngressListenerService;
import com.jj.speedwave.services.speed.SpeedWaveService;
import com.jj.speedwave.util.Log;

/**
 * Application class for service bootstrapping.
 * 
 * @author Michael Jess
 *
 */
public class IngressSpeedWave extends Application implements PreferenceChangeListener {
	
	private static final Log LOG = new Log();
	
	private Preferences preferences;

	@Override
	public void onCreate() {
		LOG.d("Application started");
		
		this.preferences =  new Preferences(this, PreferenceManager.getDefaultSharedPreferences(this));
		
		if(this.preferences.isEnabled()) {
			this.start();
		}
	}
	
	private void start() {
		new IngressStartReceiver().registerWith(LocalBroadcastManager.getInstance(this));
		new ScreenReceiver(this.preferences).registerWith(this);
		this.startService(new Intent(this, IngressListenerService.class));
	}
	
	private void stop() {
		this.stopService(new Intent(this, IngressListenerService.class));
		this.stopService(new Intent(this, LocationService.class));
		this.stopService(new Intent(this, SpeedWaveService.class));
	}

	@Override
	public void onTerminate() {
		LOG.d("Application end");
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent pce) {
		if(Preferences.ENABLED_KEY.equals(pce.getKey())) {
			if(this.preferences.isEnabled()) {
				this.start();
			} else {
				this.stop();
			}
		}
	}
	

}
