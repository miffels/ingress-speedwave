package com.jj.speedwave.services.speed;

import android.test.AndroidTestCase;

public class BaseTest extends AndroidTestCase {
	
	public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath()); // Workaround for missing dex cache
    }

}
