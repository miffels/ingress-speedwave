package com.jj.speedwave;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class IngressListenerService extends Service {
	
	private static final String INGRESS_PACKAGE_NAME = "com.nianticproject.ingress";
	private boolean running = false;
	
	private ActivityManager activityManager;
	private ScheduledExecutorService scheduler;
	
	public IngressListenerService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("speedwave", "Ingress listener start");
		activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		
		scheduler.scheduleAtFixedRate
			      (new Runnable() {
			    	 @Override
			         public void run() {
			    		 setRunning(INGRESS_PACKAGE_NAME.equals(activityManager.getRunningTasks(1).get(0).topActivity.getPackageName()));
			         }
			      }, 0, 1, TimeUnit.SECONDS);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		scheduler.shutdown();
		Log.d("speedwave", "Shutting down"); 
		super.onDestroy();
	}
	
	private void setRunning(boolean isRunning) {
		if(isRunning != this.running) {
			if(isRunning) {
				Log.d("speedwave", "Ingress started");
			} else {
				Log.d("speedwave", "Ingress stopped");
			}
			this.running = isRunning;
		}
	}
	

}
