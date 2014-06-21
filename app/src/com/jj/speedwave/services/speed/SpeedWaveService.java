package com.jj.speedwave.services.speed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.R;
import com.jj.speedwave.services.LocationReceiver;
import com.jj.speedwave.services.LocationUpdateHandler;
import com.jj.speedwave.services.IngressStopReceiver;
import com.jj.speedwave.services.StoppableService;
import com.jj.speedwave.util.Log;

public class SpeedWaveService extends Service implements StoppableService, LocationUpdateHandler, CurrentLocationProvider, SpeedWaveListener {
	
	private static final Log LOG = new Log();
	
	private LocalBroadcastManager broadcastManager;
	private ScheduledExecutorService scheduler;
	private Builder builder;
	private NotificationManager notificationManager;
	
	private IngressStopReceiver stopReceiver = new IngressStopReceiver(this);
	private LocationReceiver locationReceiver = new LocationReceiver(this);
	
	private List<Location> locations = new ArrayList<Location>();
	private Location currentLocation;
	
	private static final int NOTIFICATION_ID = 0;
	
	private boolean shouldStop;
	private boolean shouldHideNotification;
	
	private boolean initial = true;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.shouldStop = false;
		this.shouldHideNotification = true;
		
		if(this.initial) {
			LOG.d("Speed wave service started");
			
			this.broadcastManager = LocalBroadcastManager.getInstance(this);
			this.scheduler = Executors.newSingleThreadScheduledExecutor();
			this.notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
			
			this.stopReceiver.registerWith(this.broadcastManager);
			this.locationReceiver.registerWith(this.broadcastManager);
			
			this.scheduler.scheduleAtFixedRate(new SpeedWaveChecker(this.locations, this, this), 0, 1, TimeUnit.SECONDS);
			
			this.builder = new Builder(this)
			.setContentTitle("Ingress SpeedWave");
			
			this.initial = false;
		}
		
		if(this.shouldHideNotification) { // Notification not visible before
			this.showInitialNotification();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void showInitialNotification() {
		this.onSpeedUpdate(false, 0, 0);
	}

	@Override
	public void onDestroy() {
		LOG.d("Speed wave service stopped");
		this.broadcastManager.unregisterReceiver(this.stopReceiver);
		this.broadcastManager.unregisterReceiver(this.locationReceiver);
		if(this.shouldHideNotification) {
			this.notificationManager.cancel(NOTIFICATION_ID);
		}
		this.scheduler.shutdown();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) { // Unused
		return null;
	}

	public void suggestStop() {
		this.shouldStop = true;
		if(this.shouldHideNotification) {
			this.notificationManager.cancel(NOTIFICATION_ID);
		}
	}

	@Override
	public void updateLocation(Location location) {
		synchronized(this.locations) {
			this.currentLocation = location;
			this.locations.add(location);
			Collections.sort(this.locations, new DescendingDistanceComparator(this.currentLocation));
			this.scheduler.schedule(new LocationRemover(this.locations, location), 10, TimeUnit.MINUTES);
		}
	}
	
	@Override
	public Location getCurrentLocation() {
		return this.currentLocation;
	}

	@Override
	public void onSpeedUpdate(boolean isTooFast, long remainingSeconds,
			long totalSeconds) {
		if(isTooFast) {
			this.shouldStop = false;
			this.shouldHideNotification = false;
			this.builder.setSmallIcon(R.drawable.ic_speedwave_blocked);
			long remainingMinutes = remainingSeconds/60;
			long remainingSecondsFraction = remainingSeconds - remainingMinutes * 60;
			this.builder.setContentText(String.format("%02d:%02d", remainingMinutes, remainingSecondsFraction));
		} else {
			this.builder.setSmallIcon(R.drawable.ic_speedwave_ok);
			this.builder.setContentText("Your actions are not being blocked");
			this.builder.setProgress(0, 0, false);
		}
		this.builder.setProgress((int)totalSeconds, (int)(totalSeconds-remainingSeconds), false);
		if(this.shouldStop && this.shouldHideNotification) {
			return;
		}
		this.notificationManager.notify(NOTIFICATION_ID, this.builder.build());
	}

	@Override
	public void onFinish() {
		if(this.shouldStop) {
			LOG.d("Locations faded, going to sleep");
			this.stopSelf();
		}
	}
	
}
