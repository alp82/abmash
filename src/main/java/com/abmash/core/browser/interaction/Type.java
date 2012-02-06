package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

public class Type extends ActionOnHtmlElement {
	
	private String text;
	
	public Type(Browser browser, HtmlElement element, String text) {
		super(browser, element);
		this.text = text;
	}

	protected void perform() throws Exception {
		if(element != null) {
			browser.log().info("Typing in: {}", element);
			element.getSeleniumElement().sendKeys(text);
		} else {
			browser.log().warn("Element to type in does not exist");
		}
	}
	

}
