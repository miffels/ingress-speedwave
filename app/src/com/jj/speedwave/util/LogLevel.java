package com.jj.speedwave.util;


/**
 * Enumeration listing valid logging levels for the <code>Log</code> class.
 * Higher numeric values indicate more selective logging - messages with a
 * severity lower than the <code>LogLevel</code> set will not be logged.
 * 
 * @author Michael Jess
 *
 */
public enum LogLevel {
	
	/**
	 * Log Level equivalent to android.util.Log.ERROR (6).
	 */
	ERROR(android.util.Log.ERROR),
	
	/**
	 * Log Level equivalent to android.util.Log.WARN (5).
	 */
	WARN(android.util.Log.WARN),
	
	/**
	 * Log Level equivalent to android.util.Log.INFO (4).
	 */
	INFO(android.util.Log.INFO),
	
	/**
	 * Log Level equivalent to android.util.Log.DEBUG (3).
	 */
	DEBUG(android.util.Log.DEBUG),
	
	/**
	 * Log Level equivalent to android.util.Log.VERBOSE (2).
	 */
	VERBOSE(android.util.Log.VERBOSE),
	
	/**
	 * Turns logging off.
	 */
	NONE(Integer.MAX_VALUE);
	
	/**
	 * Numeric value of this <code>LogLevel</code>. Higher <code>value</code>s
	 * indicate a higher severity.
	 */
	private int value;
	
	/**
	 * Creates a new <code>LogLevel</code> with the numeric severity <code>
	 * value</code> specified.
	 * 
	 * @param value The severity value of the <code>LogLevel</code>. Higher
	 * 		values indicate a higher severity.
	 */
	private LogLevel(int value) {
		this.value = value;
	}
	
	/**
	 * Returns the numeric value of this <code>LogLevel</code>. Higher values
	 * indicate a higher severity.
	 * 
	 * @return The numeric severity value of this <code>LogLevel</code>
	 */
	public int getValue() {
		return this.value;
	}
}
