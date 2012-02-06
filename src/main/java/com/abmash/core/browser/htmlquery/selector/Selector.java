package com.abmash.core.browser.htmlquery.selector;


import org.openqa.selenium.NoSuchElementException;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;


public abstract class Selector {
	
	protected String expression;
	protected Boolean expressionValid = true;

	public Selector(String expression) {
		this.expression = expression instanceof String ? expression : "";
		if(this.expression.isEmpty()) throw new RuntimeException("JQuery expression cannot be null or empty");
	}
	
	public String toString() {
		String type = "";
		if(this.getClass().getSimpleName().toLowerCase().startsWith("css")) type = "css";
		if(this.getClass().getSimpleName().toLowerCase().startsWith("xpath")) type = "xpath";
		return (expressionValid ? "" : "expression is not valid: ") + type + "=" + expression;
	}
	
	public abstract HtmlElements find(Browser browser) throws NoSuchElementException ;
	
	public abstract HtmlElements find(Browser browser, HtmlElement rootElement) throws NoSuchElementException ;
	
}