package com.abmash.REMOVE.core.htmlquery.selector;


import org.openqa.selenium.By;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;



public class XpathSelector extends Selector {

	public XpathSelector(String expression) {
		this(expression, 0);
	}

	public XpathSelector(String expression, int weight) {
		super(expression, weight);
	}
	
	@Override
	public String getExpressionAsJQueryCommand() {
		// TODO replace single quotes if necessary
		String script = "jQuery(abmash.getData('queryElements')).xpath(\"" + expression/*.replaceAll("'", "\\\\'")*/ + "\")";
		return script;
	}
	
	@Override
	public HtmlElements find(Browser browser) {
//		return new HtmlElements(browser, browser.getWebDriver().findElements(By.xpath(expression)));
		return new JQuerySelector("xpath(\"" + expression + "\")").find(browser);
	}

	@Override
	public HtmlElements find(Browser browser, HtmlElement rootElement) {
//		return new HtmlElements(browser, rootElement.getSeleniumElement().findElements(By.xpath(expression)));
		return new JQuerySelector("xpath(\"" + expression + "\")").find(browser, rootElement);
	}

}
