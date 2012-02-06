package com.abmash.trial;

import com.abmash.core.browser.BrowserConfig;
import com.abmash.core.browser.BrowserRunnable;

public class StoryParallelDataConsolidation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread thread1 = new Thread(new BrowserRunnable(new BrowserConfig()) {
			@Override
			public void run() {
				openUrl("http://google.de");
				click("Bilder");
				type("q", "Matrix");
				click("Videos");
//				breakpoint();
				close();
			}
		});
		Thread thread2 = new Thread(new BrowserRunnable(new BrowserConfig()) {
			@Override
			public void run() {
				openUrl("http://yahoo.de");
				click("Bilder");
				type("q", "Matrix");
				click("Video");
//				breakpoint();
				close();
			}
		});
		
		thread1.start();
		thread2.start();
	}

}
