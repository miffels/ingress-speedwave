package com.jj.speedwave;

import android.app.Application;
import android.util.Log;

public class IngressSpeedWave extends Application {

	@Override
	public void onCreate() {
		Log.d("speedwave", "Application start");
		super.onCreate();
	}

}
