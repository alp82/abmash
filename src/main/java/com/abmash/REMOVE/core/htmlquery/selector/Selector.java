package com.abmash.REMOVE.core.htmlquery.selector;


import java.util.Collection;

import org.openqa.selenium.NoSuchElementException;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;


public abstract class Selector {
	
	protected String expression;
	protected Boolean expressionValid = true;
	protected int weight = 0;

	public Selector(String expression, int weight) {
		this.expression = expression instanceof String ? expression : "";
		setWeight(weight);
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
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public abstract String getExpressionAsJQueryCommand();
	
	public abstract HtmlElements find(Browser browser) throws NoSuchElementException ;
	
	public abstract HtmlElements find(Browser browser, HtmlElement rootElement) throws NoSuchElementException ;

}