package com.jj.speedwave;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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
public class IngressSpeedWave extends Application implements OnSharedPreferenceChangeListener {
	
	private static final Log LOG = new Log();
	
	private Preferences preferences;

	@Override
	public void onCreate() {
		LOG.d("Application started");
		
		this.preferences =  new Preferences(this, PreferenceManager.getDefaultSharedPreferences(this));
		this.preferences.registerOnSharedPreferenceChangeListener(this);
		
		new IngressStartReceiver().registerWith(LocalBroadcastManager.getInstance(this));
		new ScreenReceiver(this.preferences).registerWith(this);
		
		if(this.preferences.isEnabled()) {
			this.start();
		}
	}
	
	private void start() {
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
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(Preferences.ENABLED_KEY.equals(key)) {
			if(this.preferences.isEnabled()) {
				this.start();
			} else {
				this.stop();
			}
		}		
	}
	

}
