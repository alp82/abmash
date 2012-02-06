package com.abmash.core.browser.interaction;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.data.JavaScriptResult;


public class JavaScript extends ActionOnBrowser {
	
	private String script = null;
	
	private Boolean sync = true;

	private Object[] args = null;

	private JavaScriptResult result = null;

	public JavaScript(Browser browser, String script, Boolean sync, Object... args) {
		super(browser);
		this.script = script;
		this.sync = sync;
		this.args = convertArguments(args);
	}
	
	// TODO check all argument classes
	private Object[] convertArguments(Object... args) {
		ArrayList<Object> arguments = new ArrayList<Object>();
		for (Object arg: args) {
			if(arg instanceof HtmlElement) arg = ((HtmlElement) arg).getSeleniumElement();
			arguments.add(arg);
		}
		return arguments.toArray();
	}
	
	@Override
	protected void perform() {
		browser.log().trace("Executing JavaScript: " + script);
		executeScript();
	}
	
	private void executeScript() {
		try {
			if(sync) {
				result = new JavaScriptResult(executeSyncScript());
			} else {
				result = new JavaScriptResult(executeAsyncScript());
			}
		} catch(Exception e) {
			String jsMessage = browser.query().xPathSelector("//body").findFirst().getAttribute("javaScriptErrorMessage");
			String errorMessage = "JavaScript execution failed: " + jsMessage;
			errorMessage += "\n" + e.getMessage();
			errorMessage += "\n >> for the following script:\n" + script;
			errorMessage += "\n >> with the following arguments:\n" + args.toString();
			System.err.println(errorMessage + "\n");
			e.printStackTrace();
		}
		// TODO output
		if(!result.isNull()) browser.log().debug("returnValue class: " + result.getClass());
	}
	
	private Object executeSyncScript() {
		return ((JavascriptExecutor) browser.getWebDriver()).executeScript(script, args);
	}
	
	private Object executeAsyncScript() {
		return ((JavascriptExecutor) browser.getWebDriver()).executeAsyncScript(script, args);
	}
	
	public JavaScriptResult getResult() {
		return result;
	}
	
}
