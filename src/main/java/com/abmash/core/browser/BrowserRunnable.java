package com.abmash.core.browser;

import com.abmash.api.Browser;

/**
 * Runnable implementation of Browser 
 */
public abstract class BrowserRunnable extends Browser implements Runnable {

	public BrowserRunnable(BrowserConfig config) {
		super(null, config);
	}

	public abstract void run();

}
