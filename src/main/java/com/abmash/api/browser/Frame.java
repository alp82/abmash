package com.abmash.api.browser;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.query.QueryFactory;


/**
 * Interaction with browser frames, used by calling {@link Browser#frame()}.
 * <p>
 * This class is used to switch the focus between different frames and iframes.
 * 
 * @author Alper Ortac
 */
public class Frame {
	
	private Browser browser;

	// TODO automatic detection of frames
//	private static ArrayList<String> frameNames = new ArrayList<String>();
	
	/**
	 * Constructs new BrowserFrame instance to interact with browser frames
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 */
	public Frame(Browser browser) {
		this.browser = browser;
	}
	
	/**
	 * Switches to main content in this window.
	 */
	public void switchToMain() {
		browser.window().switchToMainContent();
	}
	
	/**
	 * Switches to frame with specified name.
	 * 
	 * @param name
	 */
	public void switchTo(String name) {
		// TODO only switch to parent frame if necessary
		// TODO do not change the window, just use the root of the current window
		switchToMain();
		switchTo(browser.query(QueryFactory.frame(name)).findFirst());
	}
	
	/**
	 * Switches to frame which is specified by given {@link HtmlElement}.
	 * 
	 * @param frame frame or iframe {@link HtmlElement}
	 */
	public void switchTo(HtmlElement frame) {
		// TODO only switch to parent frame if necessary
		// TODO do not change the window, just use the root of the current window
		switchToMain();
		// TODO exception handling if frame is null
		browser.log().debug("Switching to frame: " + frame);
		browser.getWebDriver().switchTo().frame(frame.getSeleniumElement());
	}
	
}
