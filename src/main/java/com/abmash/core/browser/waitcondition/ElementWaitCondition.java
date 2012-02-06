package com.abmash.core.browser.waitcondition;


import com.abmash.api.HtmlElement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


/**
 * condition for wait driver which checks the text of an element  
 */
public class ElementWaitCondition implements ExpectedCondition<Boolean> {
	 
	HtmlElement element = null;
 
	public ElementWaitCondition(HtmlElement element) {
		this.element = element;
	}
 
	/**
	 * Condition is met if target is found and its inner text or value equals the given text
	 */
	public Boolean apply(WebDriver webDriver) {
		// check if element does exist 
		return element instanceof HtmlElement;
	}
 
}
