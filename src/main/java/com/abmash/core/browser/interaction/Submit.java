package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import org.openqa.selenium.Keys;


public class Submit extends ActionOnHtmlElement {
	
	public enum SubmitMethod {
		ENTER, FORM
	}

	private SubmitMethod method;

	public Submit(Browser browser, HtmlElement element, SubmitMethod method) {
		super(browser, element);
		this.method = method;
	}

	@Override
	protected void perform() throws Exception {
		if(element != null) {
			browser.log().info("Submitted: {}", element);
			switch(method) {
				case ENTER:
					element.getSeleniumElement().sendKeys(Keys.ENTER);
					break;
				case FORM:
				default:
					element.getSeleniumElement().submit();
					break;
			}
		} else {
			browser.log().warn("Element to submit does not exist");
		}
	}
}
