package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import org.openqa.selenium.Keys;



public abstract class ActionOnHtmlElement extends ActionOnBrowser {
	
	protected HtmlElement element = null;

	public ActionOnHtmlElement(Browser browser, HtmlElement element) {
		super(browser);
		this.element = element;
	}
	
	protected void performBefore() {
		if(element != null && element.isLocatedInFrame()) browser.frame().switchTo(element.getFrameElement());
	}

	protected void performAfter() {
		// TODO switch to previously focused content
		if(element != null && element.isLocatedInFrame()) browser.window().switchToMainContent();
	}
	
	protected Keys getKey(String keyName) {
		Keys key = Keys.NULL;
		try {
			// try to find the right key to press by its name
			key = Keys.valueOf(keyName.toUpperCase());
		} catch (Exception e) {
			// key identifier unknown
		}
		return key;
	}
}
