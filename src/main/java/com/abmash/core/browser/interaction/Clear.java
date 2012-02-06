package com.abmash.core.browser.interaction;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

public class Clear extends ActionOnHtmlElement {
	
	public Clear(Browser browser, HtmlElement element) {
		super(browser, element);
	}

	@Override
	protected void perform() throws Exception {
		// TODO validation if element is clearable
		if(element != null) {
			browser.log().info("Clearing: {}", element);
			element.getSeleniumElement().clear();
		} else {
			browser.log().warn("Element to clear does not exist");
		}
	}

}
