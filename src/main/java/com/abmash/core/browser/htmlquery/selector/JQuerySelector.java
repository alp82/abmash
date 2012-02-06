package com.abmash.core.browser.htmlquery.selector;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;



public class JQuerySelector extends Selector {
	
	String selector;

	public JQuerySelector(String expression) {
		super(expression);
	}

	@Override
	public HtmlElements find(Browser browser) {
		return find(browser, new HtmlElement(browser, (RemoteWebElement) browser.getWebDriver().findElement(By.tagName("body"))));
	}

	@Override
	public HtmlElements find(Browser browser, HtmlElement rootElement) {
		// TODO replace single quotes if necessary
		String script =
			"var abmashQueryResult = jQuery(arguments[0])." + expression/*.replaceAll("'", "\\\\'")*/ + ";"
			+ "return abmashQueryResult.get();";
		Object result = browser.javaScript(script, rootElement).getReturnValue();
		return result != null ? new HtmlElements(browser, (List<WebElement>) result) : null;
	}

}
