package com.abmash.core.browser.interaction;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

public class OpenURL extends ActionOnBrowser {
	
	String url;
	
	public OpenURL(Browser browser, String url) {
		super(browser);
		this.url = url;
	}

	@Override
	protected void perform() throws Exception {
		// TODO validate URL
		if(url instanceof String) {
			browser.log().info("Opening URL: {}", url);
			browser.getWebDriver().get(url);
		}
	}

}
