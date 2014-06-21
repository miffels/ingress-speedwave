package com.jj.speedwave.services.speed;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat.Builder;

import com.jj.speedwave.R;

public class SpeedWaveNotificationManager implements SpeedWaveListener {
	
	private Builder builder;
	private NotificationManager notificationManager;
	
	private boolean shouldHideNotification = true;
	private static final int NOTIFICATION_ID = 0;
	private boolean tooFast;
	
	private PendingIntent ingressIntent;

	public SpeedWaveNotificationManager(Builder builder,
			NotificationManager notificationManager, IngressIntentBuilder intentBuilder) {
		this.builder = builder.setContentTitle("Ingress SpeedWave");
		this.ingressIntent = intentBuilder.build();
		this.notificationManager = notificationManager;
		
		this.showInitialNotification();
	}
	
	private void showInitialNotification() {
		this.restoreDefaultNotification();
		this.refreshNotification();
	}
	
	@Override
	public void onSpeedUpdate(boolean tooFast, long remainingSeconds,
			long totalSeconds) {
		if(tooFast) {
			this.shouldHideNotification = false;
			this.builder.setSmallIcon(R.drawable.ic_speedwave_blocked);
			long remainingMinutes = remainingSeconds/60;
			long remainingSecondsFraction = remainingSeconds - remainingMinutes * 60;
			this.builder.setContentText(String.format("%02d:%02d", remainingMinutes, remainingSecondsFraction));
			this.builder.setOngoing(true);
			this.builder.setContentIntent(null);
		} else {
			this.restoreDefaultNotification();
		}
		this.builder.setProgress((int)totalSeconds, (int)(totalSeconds-remainingSeconds), false);
		this.setTooFast(tooFast);
	}
	
	private void restoreDefaultNotification() {
		this.builder.setSmallIcon(R.drawable.ic_speedwave_ok);
		this.builder.setContentText("Your actions are not being blocked");
		this.builder.setOngoing(false);
		this.builder.setAutoCancel(true);
		this.builder.setContentIntent(this.ingressIntent);
	}
	
	private void setTooFast(boolean tooFast) {
		if(tooFast || tooFast != this.tooFast) {
			this.refreshNotification();			
		}
		this.tooFast = tooFast;
	}
	
	private void refreshNotification() {
		this.notificationManager.notify(NOTIFICATION_ID, this.builder.build());
	}

	@Override
	public void onFinish() {
		this.suggestCancel();
	}

	public void setHideNotification(boolean shouldHideNotification) {
		this.shouldHideNotification = shouldHideNotification;
	}
	
	public void suggestCancel() {
		if(this.shouldHideNotification) {
			this.notificationManager.cancel(NOTIFICATION_ID);
		}
	}
	
}
