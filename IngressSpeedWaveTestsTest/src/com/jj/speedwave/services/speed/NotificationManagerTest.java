package com.jj.speedwave.services.speed;

import org.mockito.Mockito;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat.Builder;

public class NotificationManagerTest extends BaseTest {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	private NotificationManager mockNotificationManager() {
		return Mockito.mock(NotificationManager.class);
	}
	
	private Builder mockBuilder() {
		Builder builder = Mockito.mock(Builder.class);
		Mockito.when(builder.setContentTitle(Mockito.anyString())).thenReturn(builder);
		return builder;
	}
	
	private IngressIntentBuilder mockIntentBuilder() {
		return Mockito.mock(IngressIntentBuilder.class);
	}
	
	public void testInitialNotification() {
		NotificationManager notificationManager = mockNotificationManager();
		
		new SpeedWaveNotificationManager(mockBuilder(), notificationManager, mockIntentBuilder());
		
		Mockito.verify(notificationManager).notify(0, null);
		Mockito.verifyNoMoreInteractions(notificationManager);
	}
	
	public void testSlowNotificationAndCancel() {
		NotificationManager androidNotiMgr = mockNotificationManager();
		
		SpeedWaveNotificationManager speedWaveNotiMgr =
				new SpeedWaveNotificationManager(mockBuilder(), androidNotiMgr, mockIntentBuilder());
		Mockito.verify(androidNotiMgr).notify(0, null);
		
		speedWaveNotiMgr.onSpeedUpdate(false, 0, 0);
		speedWaveNotiMgr.suggestCancel();
		
		Mockito.verify(androidNotiMgr).cancel(0);
		Mockito.verifyNoMoreInteractions(androidNotiMgr);
	}
	
	public void testFastNotificationAndCancel() {
		NotificationManager androidNotiMgr = mockNotificationManager();
		Builder builder = mockBuilder();
		
		SpeedWaveNotificationManager speedWaveNotiMgr =
				new SpeedWaveNotificationManager(builder, androidNotiMgr, mockIntentBuilder());
		Mockito.verify(androidNotiMgr).notify(0, null);
		
		speedWaveNotiMgr.onSpeedUpdate(true, 1, 10);
		Mockito.verify(builder).setContentText("00:01");
		Mockito.verify(builder).setProgress(10, 9, false);
		
		speedWaveNotiMgr.suggestCancel();
		
		Mockito.verify(androidNotiMgr, Mockito.times(2)).notify(0, null);
		Mockito.verifyNoMoreInteractions(androidNotiMgr);
	}

}
