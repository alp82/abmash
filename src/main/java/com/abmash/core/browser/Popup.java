package com.abmash.core.browser;

import com.abmash.api.Browser;

public class Popup {
	
	Browser browser;
	
	String windowHandle;
	String initialUrl;

	public Popup(Browser browser, String windowHandle, String url) {
		this.browser = browser;
		this.windowHandle = windowHandle;
		this.initialUrl = url;
	}

	public String getWindowHandle() {
		return windowHandle;
	}

	public String getInitialUrl() {
		return initialUrl;
	}
	
	public void switchTo() {
		browser.window().switchToWindow(windowHandle);
	}
	
	public void close() {
		browser.window().closeWindow(windowHandle);
	}
	
	public String toString() {
		return windowHandle;
	}
}
