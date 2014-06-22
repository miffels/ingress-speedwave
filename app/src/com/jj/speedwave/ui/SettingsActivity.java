package com.jj.speedwave.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.jj.speedwave.R;

/**
 * Settings go here when it's time.
 * 
 * @author Michael Jess
 *
 */
public class SettingsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.preferences);
	}

}
