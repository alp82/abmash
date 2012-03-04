package com.abmash.core.htmlquery.selector;


import java.util.Collection;

import org.openqa.selenium.NoSuchElementException;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;


public abstract class Selector {
	
	protected String expression;
	protected Boolean expressionValid = true;

	public Selector(String expression) {
		this.expression = expression instanceof String ? expression : "";
	}
	
	public String toString() {
		String type = "";
		if(getType().toLowerCase().startsWith("css")) type = "css";
		if(getType().toLowerCase().startsWith("xpath")) type = "xpath";
		return (expressionValid ? "" : "expression is not valid: ") + type + "=" + expression;
	}
	
	public String getType() {
		return this.getClass().getSimpleName();
	}
	
	public abstract String getExpressionAsJQueryCommand();
	
	public abstract HtmlElements find(Browser browser) throws NoSuchElementException ;
	
	public abstract HtmlElements find(Browser browser, HtmlElement rootElement) throws NoSuchElementException ;

}