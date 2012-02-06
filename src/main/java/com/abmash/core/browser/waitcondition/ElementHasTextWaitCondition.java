package com.abmash.core.browser.waitcondition;


import com.abmash.api.HtmlElement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


/**
 * condition for wait driver which checks the text of an element  
 */
public class ElementHasTextWaitCondition implements ExpectedCondition<Boolean> {
	 
	HtmlElement element = null;
	String text;
 
	public ElementHasTextWaitCondition(HtmlElement element, String text) {
		this.element = element;
		this.text = text;
	}
 
	/**
	 * Condition is met if target is found and its inner text or value equals the given text
	 */
	public Boolean apply(WebDriver webDriver) {
		// if element does not exist return
		if(element == null) return false;
		// check for inner text and value
		return element.getText().contains(text) || element.getAttribute("value").equals(text);
	}
 
}
