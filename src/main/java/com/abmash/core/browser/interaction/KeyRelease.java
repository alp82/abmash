package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;



public class KeyRelease extends ActionOnHtmlElement {
	
	private Keys key;

	public KeyRelease(Browser browser, HtmlElement sourceElement, String keyName) {
		super(browser, sourceElement);
		key = getKey(keyName);
	}
	
	protected void perform() throws Exception {
		if(element != null) {
			if(key != Keys.NULL) {
				browser.log().info("Releasing key '{}' on: {}", key, element);
				// TODO Only working if native events are enabled on this platform
				Action action = new Actions(browser.getWebDriver()).keyUp(element.getSeleniumElement(), key).build();
				action.perform();
			}
		} else {
			browser.log().warn("Element to release key '{}' on does not exist", key);
		}
	}
}
