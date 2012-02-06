package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import org.openqa.selenium.Keys;


public class KeyPress extends ActionOnHtmlElement {
	
	private Keys key;

	public KeyPress(Browser browser, HtmlElement element, String keyName) {
		super(browser, element);
		key = getKey(keyName);
	}

	@Override
	protected void perform() throws Exception {
		if(element != null) {
			if(key != Keys.NULL) {
				browser.log().info("Pressing key '{}' on: {}", key, element);
				element.getSeleniumElement().sendKeys(key);
			}
		} else {
			browser.log().warn("Element to press key '{}' on does not exist", key);
		}
	}
}
