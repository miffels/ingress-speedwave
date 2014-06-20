package com.jj.speedwave.services.speed;

import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.services.LocationReceiver;
import com.jj.speedwave.services.LocationUpdateHandler;
import com.jj.speedwave.services.StopReceiver;
import com.jj.speedwave.services.StoppableService;
import com.jj.speedwave.util.Log;

public class SpeedWaveService extends Service implements StoppableService, LocationUpdateHandler, CurrentLocationProvider {
	
	private static final Log LOG = new Log();
	
	private LocalBroadcastManager broadcastManager;
	private ScheduledExecutorService scheduler;
	
	private StopReceiver stopReceiver = new StopReceiver(this);
	private LocationReceiver locationReceiver = new LocationReceiver(this);
	
	private Vector<Location> locations = new Vector<Location>();
	private Location currentLocation;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LOG.d("Speed wave service started");
		
		this.broadcastManager = LocalBroadcastManager.getInstance(this);
		this.scheduler = Executors.newSingleThreadScheduledExecutor();
		
		this.stopReceiver.registerWith(this.broadcastManager);
		this.locationReceiver.registerWith(this.broadcastManager);
		
		this.scheduler.scheduleAtFixedRate(new SpeedWaveChecker(this.locations, this), 0, 1, TimeUnit.SECONDS);
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		LOG.d("Speed wave service stopped");
		this.broadcastManager.unregisterReceiver(this.stopReceiver);
		this.broadcastManager.unregisterReceiver(this.locationReceiver);
		this.scheduler.shutdown();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) { // Unused
		return null;
	}

	public void suggestStop() {
		LOG.d("Checking if I may stop...");
		this.stopSelf();
	}

	@Override
	public void updateLocation(Location location) {
		this.currentLocation = location;
		this.locations.add(location);
		Collections.sort(this.locations, new DescendingDistanceComparator(this.currentLocation));
		this.scheduler.schedule(new LocationRemover(this.locations, location), 10, TimeUnit.MINUTES);
	}
	
	@Override
	public Location getCurrentLocation() {
		return this.currentLocation;
	}
	
}
