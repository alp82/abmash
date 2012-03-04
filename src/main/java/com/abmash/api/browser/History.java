package com.abmash.api.browser;

import com.abmash.api.Browser;


/**
 * Interaction with browser history, used by calling {@link Browser#history()}.
 * <p>
 * This class is used to move back and forward in the browser history.
 * 
 * @author Alper Ortac
 */
public class History {
	
	private Browser browser;

	/**
	 * Constructs new BrowserHistory instance to interact with the browser history
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 */
	public History(Browser browser) {
		this.browser = browser;
	}
	
	/**
	 * Goes back in the browser history.
	 * 
	 * @param count number of consecutive back commands 
	 */
	// TODO depending on browser
	public void back(int count) {
		for (int i = 0; i < count; i++) {
			// TODO wait for page
			browser.log().info("History back");
			browser.getWebDriver().navigate().back();
		}
	}
	
	/**
	 * Goes back in the browser history.
	 */
	public void back() {
		back(1);
	}

	/**
	 * Goes forward in the browser history
	 * 
	 * @param count number of consecutive forward commands 
	 */
	// TODO depending on browser
	public void forward(int count) {
		for (int i = 0; i < count; i++) {
			// TODO wait for page
			browser.log().info("History forward");
			browser.getWebDriver().navigate().forward();
		}		
	}

	/**
	 * Goes forward in the browser history
	 */
	public void forward() {
		forward(1);
	}
	
}
