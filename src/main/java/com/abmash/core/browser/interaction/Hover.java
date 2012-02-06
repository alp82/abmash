package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

public class Hover extends ActionOnHtmlElement {
	
	public Hover(Browser browser, HtmlElement element) {
		super(browser, element);
	}
	
	protected void perform() throws Exception {
		if(element != null) {
			browser.log().info("Hovering: {}", element);
			browser.javaScript("jQuery(arguments[0]).trigger('mouseover')", element).getReturnValue();
		} else {
			browser.log().warn("Element to hover does not exist");
		}
		
	}
}
