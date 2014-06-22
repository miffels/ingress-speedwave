package com.jj.speedwave;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
 * Encapsulates and provides access to the preferences relevant to mobEx.
 * 
 * @author Michael Jess
 * 
 */
public class Preferences {
	
	/**
	 * The underlying preference object
	 */
	private SharedPreferences preferences;
	
	public static final String ENABLED_KEY = "enabled";
	
	
	/**
	 * Creates a new <code>Preferences</code> object accessing the specified
	 * <code>SharedPreferences</code>.
	 * 
	 * @param preferences The <code>SharedPreferences</code> to use
	 */
	public Preferences(Context context, SharedPreferences preferences) {
		if(context != null) {
			PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
		}
		this.preferences = preferences;
	}
	
	/**
	 * Tells if SpeedWave is enabled.
	 * 
	 * @return <code>true</code> if SpeedWave is enabled; <code>false</code> otherwise
	 */
	public boolean isEnabled() {
		return this.isOptionEnabled(ENABLED_KEY);
	}
	
	/**
	 * Tells if the specified option is enabled. Defaults to <code>true</code>.
	 * 
	 * @param key The option key
	 * @return <code>true</code> if the option is enabled; <code>false</code> otherwise
	 */
	private boolean isOptionEnabled(String key) {
		return this.preferences.getBoolean(key, false);
	}

//	/**
//	 * Returns the number value of the specified option. Defaults to 0.
//	 * 
//	 * @param key The option key
//	 * @return The value of the option
//	 */
//	private int getNumberOfOption(String key) {
//		return this.preferences.getInt(key, 0);
//	}
//	
//	/**
//	 * Returns the string value of the specified option. Defaults to an empty
//	 * string.
//	 * 
//	 * @param key The option key
//	 * @return The value of the option
//	 */
//	private String getStringOption(String key) {
//		return this.preferences.getString(key, "");
//	}
	
	/**
	 * Registers a listener for preference changes.
	 * 
	 * @param listener The listener to register
	 */
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		this.preferences.registerOnSharedPreferenceChangeListener(listener);
	}
	
	public SharedPreferences.Editor edit() {
		return this.preferences.edit();
	}
	
}
