package com.abmash.core.htmlquery.selector;


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
		if(this.expression.isEmpty()) throw new RuntimeException("JQuery expression cannot be null or empty");
	}

	@Override
	public String getExpressionAsJQueryCommand() {
		// TODO replace single quotes if necessary
		return "jQuery(abmash.getTempElement())." + expression/*.replaceAll("'", "\\\\'")*/;
	}

	@Override
	public HtmlElements find(Browser browser) {
		return find(browser, new HtmlElement(browser, (RemoteWebElement) browser.getWebDriver().findElement(By.tagName("body"))));
	}

	@Override
	public HtmlElements find(Browser browser, HtmlElement rootElement) {
		String script = "abmash.setTempElement(arguments[0]); var abmashQueryResult = " + getExpressionAsJQueryCommand() + "; return abmashQueryResult.get();";
		Object result = browser.javaScript(script, rootElement).getReturnValue();
		return result != null ? new HtmlElements(browser, (List<WebElement>) result) : null;
	}

}
