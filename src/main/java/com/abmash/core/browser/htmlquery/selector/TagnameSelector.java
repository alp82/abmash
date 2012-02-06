package com.abmash.core.browser.htmlquery.selector;


import org.openqa.selenium.By;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;



public class TagnameSelector extends Selector {

	public TagnameSelector(String expression) {
		super(expression);
	}

	@Override
	public HtmlElements find(Browser browser) {
		return new HtmlElements(browser, browser.getWebDriver().findElements(By.tagName(expression)));
	}

	@Override
	public HtmlElements find(Browser browser, HtmlElement rootElement) {
		return new HtmlElements(browser, rootElement.getSeleniumElement().findElements(By.tagName(expression)));
	}
	
}
