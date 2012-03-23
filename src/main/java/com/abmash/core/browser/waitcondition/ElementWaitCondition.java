package com.abmash.core.browser.waitcondition;


import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlQuery;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


/**
 * condition for wait driver which checks the text of an element  
 */
public class ElementWaitCondition implements ExpectedCondition<Boolean> {
	 
	HtmlQuery query = null;
 
	public ElementWaitCondition(HtmlQuery query) {
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
