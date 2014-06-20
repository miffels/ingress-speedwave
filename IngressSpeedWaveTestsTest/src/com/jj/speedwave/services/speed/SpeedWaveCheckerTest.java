package com.jj.speedwave.services.speed;

import java.util.ArrayList;
import java.util.Arrays;

import org.mockito.Mockito;

import android.location.Location;
import android.test.AndroidTestCase;

public class SpeedWaveCheckerTest extends AndroidTestCase {
	
	public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath()); // Workaround for missing dex cache
    }
	
	private Location mockLocation() {
		return Mockito.mock(Location.class);		
	}
	
	private TimeProvider mockTimeProvider(long time) {
		TimeProvider provider = Mockito.mock(TimeProvider.class);
		Mockito.when(provider.getCurrentTimeMillis()).thenReturn(time);
		return provider;
	}
	
	private CurrentLocationProvider mockLocationProvider(Location location) {
		CurrentLocationProvider locationProvider = Mockito.mock(CurrentLocationProvider.class);
		Mockito.when(locationProvider.getCurrentLocation()).thenReturn(location);
		return locationProvider;
	}
	
	public void testEmptyList() {
		Location location = mockLocation();
		CurrentLocationProvider locationProvider = mockLocationProvider(location);
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		new SpeedWaveChecker(new ArrayList<Location>(), locationProvider, listener).run();
		
		Mockito.verify(listener).onFinish();
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testNullLocation() {
		CurrentLocationProvider locationProvider = mockLocationProvider(null);
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		new SpeedWaveChecker(Arrays.asList(mockLocation()), locationProvider, listener).run();
		
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testInitialLocation() {
		Location location = mockLocation();
		CurrentLocationProvider locationProvider = mockLocationProvider(location);
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		new SpeedWaveChecker(Arrays.asList(location), locationProvider, listener).run();
		
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testSlowMovement() {
		Location furthestLocation = mockLocation();
		Location currentLocation = mockLocation();
		CurrentLocationProvider locationProvider = mockLocationProvider(currentLocation);
		
		Mockito.when(furthestLocation.getTime()).thenReturn(0l);
		Mockito.when(furthestLocation.distanceTo(currentLocation)).thenReturn(10f); // 10 meters movement in 1 second
		
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		
		new SpeedWaveChecker(Arrays.asList(furthestLocation, currentLocation), locationProvider, listener, mockTimeProvider(1000l)).run();;
		
		Mockito.verify(listener).onSpeedUpdate(false, 0, 0);
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testFastMovement() {
		Location furthestLocation = mockLocation();
		Location currentLocation = mockLocation();
		CurrentLocationProvider locationProvider = mockLocationProvider(currentLocation);
		
		Mockito.when(furthestLocation.getTime()).thenReturn(0l);
		Mockito.when(furthestLocation.distanceTo(currentLocation)).thenReturn(100f); // 100 meters movement in 1 second
		
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		
		new SpeedWaveChecker(Arrays.asList(furthestLocation, currentLocation), locationProvider, listener, mockTimeProvider(1000l)).run();;
		
		Mockito.verify(listener).onSpeedUpdate(true, 7, 8);
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testExceedinglyFastMovement() {
		Location furthestLocation = mockLocation();
		Location intermediateLocation = mockLocation();
		Location currentLocation = mockLocation();
		CurrentLocationProvider locationProvider = mockLocationProvider(currentLocation);
		
		Mockito.when(furthestLocation.getTime()).thenReturn(0l);
		Mockito.when(intermediateLocation.getTime()).thenReturn(1000l); // 1 second later
		Mockito.when(intermediateLocation.distanceTo(currentLocation)).thenReturn(10000f); // 10,000 meters movement in 1 second
		Mockito.when(furthestLocation.distanceTo(currentLocation)).thenReturn(20000f); // 20,000 meters movement in 2 seconds
		
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		
		new SpeedWaveChecker(Arrays.asList(furthestLocation, intermediateLocation, currentLocation), locationProvider, listener, mockTimeProvider(2000l)).run();;
		
		Mockito.verify(listener).onSpeedUpdate(true, 599, 600);
		Mockito.verifyNoMoreInteractions(listener);
	}

}
