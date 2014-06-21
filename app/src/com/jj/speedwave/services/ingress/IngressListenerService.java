package com.jj.speedwave.services.ingress;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.jj.speedwave.util.Intents;
import com.jj.speedwave.util.Log;

/**
 * Continuously running service polling the activity manager each second in order to
 * detect when Ingress is brought to the foreground or background. When this happens,
 * a local broadcast is sent, notifying the application and other services.
 * 
 * @author Michael Jess
 *
 */
public class IngressListenerService extends Service {
	
	private static final Log LOG = new Log();
	
	private static final String INGRESS_PACKAGE_NAME = "com.nianticproject.ingress";
	private boolean running = false;
	
	private ActivityManager activityManager;
	private ScheduledExecutorService scheduler;
	private LocalBroadcastManager broadcastManager; 
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LOG.d("Ingress listener started");
		activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		broadcastManager = LocalBroadcastManager.getInstance(this.getApplicationContext());
		scheduler.scheduleAtFixedRate
			      (new Runnable() {
			    	 @Override
			         public void run() {
			    		 ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
			    		 setRunning(INGRESS_PACKAGE_NAME.equals(topActivity.getPackageName()));
			         }
			      }, 0, 1, TimeUnit.SECONDS);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		scheduler.shutdown();
		LOG.d("Ingress listener stopped"); 
		super.onDestroy();
	}
	
	private void setRunning(boolean isRunning) {
		if(isRunning != this.running) {
			if(isRunning) {
				LOG.d("Ingress started");
				broadcastManager.sendBroadcast(new Intent(Intents.INGRESS_STARTED));
			} else {
				LOG.d("Ingress stopped");
				broadcastManager.sendBroadcast(new Intent(Intents.INGRESS_STOPPED));
			}
			this.running = isRunning;
		}
	}
	

}
