package com.jj.speedwave.services.speed;

import java.util.ArrayList;
import java.util.Arrays;

import org.mockito.Mockito;

import android.location.Location;

public class SpeedWaveCheckerTest extends BaseTest {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	private Location mockLocation() {
		return Mockito.mock(Location.class);		
	}
	
	private TimeProvider mockTimeProvider(long time) {
		TimeProvider provider = Mockito.mock(TimeProvider.class);
		Mockito.when(provider.getCurrentTimeMillis()).thenReturn(time);
		return provider;
	}
	
	private LocationProvider mockLocationProvider(Location... locations) {
		LocationProvider locationProvider = Mockito.mock(LocationProvider.class);
		Mockito.when(locationProvider.getCurrentLocation()).thenReturn(locations[locations.length - 1]);
		Location[] previousLocations = new Location[locations.length - 1];
		System.arraycopy(locations, 0, previousLocations, 0, previousLocations.length);
		Mockito.when(locationProvider.getPreviousLocations()).thenReturn(Arrays.asList(previousLocations));
		Mockito.when(locationProvider.isReady()).thenReturn(locations.length > 1);
		return locationProvider;
	}
	
	public void testEmptyList() {
		LocationProvider locationProvider = mockLocationProvider(mockLocation());
		Mockito.when(locationProvider.isReady()).thenReturn(true);
		Mockito.when(locationProvider.getPreviousLocations()).thenReturn(new ArrayList<Location>()); // Simulate fading of previous locations
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		new SpeedWaveChecker(locationProvider, listener).run();
		
		Mockito.verify(listener).onFinish();
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testNullLocation() {
		LocationProvider locationProvider = mockLocationProvider((Location)null);
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		new SpeedWaveChecker(locationProvider, listener).run();
		
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testInitialLocation() {
		Location location = mockLocation();
		LocationProvider locationProvider = mockLocationProvider(location);
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		new SpeedWaveChecker(locationProvider, listener).run();
		
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testSlowMovement() {
		Location furthestLocation = mockLocation();
		Location currentLocation = mockLocation();
		LocationProvider locationProvider = mockLocationProvider(furthestLocation, currentLocation);
		
		Mockito.when(furthestLocation.getTime()).thenReturn(0l);
		Mockito.when(furthestLocation.distanceTo(currentLocation)).thenReturn(10f); // 10 meters movement in 1 second
		
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		
		new SpeedWaveChecker(locationProvider, mockTimeProvider(1000l), listener).run();;
		
		Mockito.verify(listener).onSpeedUpdate(false, 0, 0);
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testFastMovement() {
		Location furthestLocation = mockLocation();
		Location currentLocation = mockLocation();
		LocationProvider locationProvider = mockLocationProvider(furthestLocation, currentLocation);
		
		Mockito.when(furthestLocation.getTime()).thenReturn(0l);
		Mockito.when(furthestLocation.distanceTo(currentLocation)).thenReturn(100f); // 100 meters movement in 1 second
		
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		
		new SpeedWaveChecker(locationProvider, mockTimeProvider(1000l), listener).run();;
		
		Mockito.verify(listener).onSpeedUpdate(true, 7, 8);
		Mockito.verifyNoMoreInteractions(listener);
	}
	
	public void testExceedinglyFastMovement() {
		Location furthestLocation = mockLocation();
		Location intermediateLocation = mockLocation();
		Location currentLocation = mockLocation();
		LocationProvider locationProvider = mockLocationProvider(furthestLocation, intermediateLocation, currentLocation);
		
		Mockito.when(furthestLocation.getTime()).thenReturn(0l);
		Mockito.when(intermediateLocation.getTime()).thenReturn(1000l); // 1 second later
		Mockito.when(intermediateLocation.distanceTo(currentLocation)).thenReturn(10000f); // 10,000 meters movement in 1 second
		Mockito.when(furthestLocation.distanceTo(currentLocation)).thenReturn(20000f); // 20,000 meters movement in 2 seconds
		
		SpeedWaveListener listener = Mockito.mock(SpeedWaveListener.class);
		
		new SpeedWaveChecker(locationProvider, mockTimeProvider(2000l), listener).run();;
		
		Mockito.verify(listener).onSpeedUpdate(true, 599, 600);
		Mockito.verifyNoMoreInteractions(listener);
	}

}
