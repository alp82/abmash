package com.abmash.core.browser.waitcondition;


import com.abmash.api.Browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


/**
 * condition for wait driver which evaluates a given javascript expression
 */
public class JavaScriptEvaluatedWaitCondition implements ExpectedCondition<Boolean> {
	 
	Browser browser;
	String expressionCondition;
 
	public JavaScriptEvaluatedWaitCondition(Browser browser, String expression) {
		this.browser = browser;
		
		// prepend return expression to ensure correct return value
		// only for single line commands
		if(!expression.startsWith("return") && !expression.contains("\n")) expression = "return " + expression;
		this.expressionCondition = expression;
	}
 
	public Boolean apply(WebDriver webDriver) {
		return browser.javaScript(expressionCondition).hasAdequateReturnValue();
	}
 
}
