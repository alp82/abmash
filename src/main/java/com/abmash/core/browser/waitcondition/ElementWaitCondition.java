package com.abmash.core.browser.waitcondition;


import com.abmash.REMOVE.api.HtmlQuery;
import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.query.Query;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


/**
 * condition for wait driver which checks the text of an element  
 */
public class ElementWaitCondition implements ExpectedCondition<Boolean> {
	 
	private Browser browser;
	private Query query;
 
	public ElementWaitCondition(Browser browser, Query query) {
		this.browser = browser;
		this.query = query;
	}
		
	/**
	 * Condition is met if at least one target is found
	 */
	public Boolean apply(WebDriver webDriver) {
		// check if element does exist
		HtmlElement element = query.findFirst();
		return element instanceof HtmlElement;
	}
 
}
