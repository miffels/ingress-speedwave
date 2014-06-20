package com.jj.speedwave.services;

/**
 * Interface for services that should receive suggestions on when to stop, but should
 * decide independently when the best point in time to stop is.
 * 
 * @author Michael Jess
 *
 */
public interface StoppableService {
	
	/**
	 * Suggests the service to stop. It does not necessarily stop immediately.
	 */
	public void suggestStop();

}
