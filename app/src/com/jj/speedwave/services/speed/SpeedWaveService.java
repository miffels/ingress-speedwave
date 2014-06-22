package com.jj.speedwave.services.speed;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.services.IngressStopReceiver;
import com.jj.speedwave.services.LocationReceiver;
import com.jj.speedwave.services.StoppableService;
import com.jj.speedwave.util.Log;

public class SpeedWaveService extends Service implements StoppableService, SpeedWaveListener {
	
	private static final Log LOG = new Log();

	private LocationManager locationManager = new LocationManager();
	
	private LocalBroadcastManager broadcastManager;
	private IngressStopReceiver stopReceiver = new IngressStopReceiver(this);
	private LocationReceiver locationReceiver = new LocationReceiver(this.locationManager);
	
	private SpeedWaveNotificationManager notificationManager;
	
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	private boolean shouldStop;
	private boolean initial = true;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(this.initial) {
			LOG.d("Speed wave service started");
			
			this.notificationManager = new SpeedWaveNotificationManager(new Builder(this),
					(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE),
					new IngressIntentBuilder(this));
			
			this.broadcastManager = LocalBroadcastManager.getInstance(this);
			this.stopReceiver.registerWith(this.broadcastManager);
			this.locationReceiver.registerWith(this.broadcastManager);
			
			this.scheduler.scheduleAtFixedRate(new SpeedWaveChecker(this.locationManager,
					this, this.notificationManager), 0, 1, TimeUnit.SECONDS);
			
			this.initial = false;
		}
		
		this.shouldStop = false;
		this.notificationManager.resetHideNotification();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		LOG.d("Speed wave service stopped");
		this.broadcastManager.unregisterReceiver(this.stopReceiver);
		this.broadcastManager.unregisterReceiver(this.locationReceiver);
		this.scheduler.shutdown();
		this.notificationManager.forceCancel();
		this.locationManager.shutdown();
	}

	@Override
	public IBinder onBind(Intent intent) { // Unused
		return null;
	}

	@Override
	public void suggestStop() {
		this.shouldStop = true;
		this.notificationManager.suggestCancel();
	}
	
	@Override
	public void onSpeedUpdate(boolean isTooFast, long remainingSeconds,
			long totalSeconds) {
		if(isTooFast) {
			this.shouldStop = false;
		}
	}

	@Override
	public void onFinish() {
		if(this.shouldStop) {
			LOG.d("Locations faded, going to sleep");
			this.stopSelf();
		}
	}
	
}
