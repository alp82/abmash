package com.abmash.api.browser;


import com.abmash.REMOVE.api.HtmlQuery;
import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.query.Query;
import com.abmash.api.query.QueryFactory;
import com.abmash.core.browser.waitcondition.ElementHasTextWaitCondition;
import com.abmash.core.browser.waitcondition.ElementWaitCondition;
import com.abmash.core.browser.waitcondition.JavaScriptEvaluatedWaitCondition;
import com.thoughtworks.selenium.Wait;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;



/**
 * Waits for the browser, used by calling {@link Browser#waitFor()}.
 * <p>
 * Sometimes it is necessary to wait for the web page, which needs to complete loading page elements.
 * This is usually the case when JavaScript gets executed, mostly AJAX calls. Wait conditions can be
 * customized by specifying own JavaScript code.
 * 
 * @author Alper Ortac
 */
public class WaitFor {
	
	public static final int DEFAULT_TIMEOUT_ON_WAIT = 20;
	
	private Browser browser;
	private int timeout;

	/**
	 * Constructs new BrowserWait instance to be able to define wait conditions.
	 * <p>
	 * The default timeout length is {@value #DEFAULT_TIMEOUT_ON_WAIT} seconds.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 */
	public WaitFor(Browser browser) {
		this(browser, DEFAULT_TIMEOUT_ON_WAIT);
	}

	/**
	 * Constructs new BrowserWait instance to be able to define wait conditions.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 * @param timeout wait for this amount of seconds until a timeout exception is thrown
	 */
	public WaitFor(Browser browser, int timeout) {
		this.browser = browser;
		this.timeout = timeout;
	}

	/**
	 * Waits until element is found. Useful for waiting for an AJAX call to complete.
	 * 
	 * @param query the element query
	 * @throws TimeoutException
	 */
	public void query(Query query) throws TimeoutException {
//		WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), timeout);
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(browser.getWebDriver())
	       .withTimeout(timeout, TimeUnit.SECONDS)
	       .pollingEvery(500, TimeUnit.MILLISECONDS);
		
		// start waiting for given element
		wait.until(new ElementWaitCondition(browser, query));
	}
	
	/**
	 * Waits until element is found. Useful for waiting for an AJAX call to complete.
	 * 
	 * @param query the element which needs to be found, identified by the visible text or attribute values 
	 * @throws TimeoutException
	 */
	public void element(String query) throws TimeoutException {
		query(browser.query(QueryFactory.contains(query)));
	}

	/**
	 * Waits until element contains the specified text. Useful for waiting for an AJAX call to complete.
	 * 
	 * @param element the element to observe
	 * @param text when the element contains this text the execution of the program will be continued
	 * @throws TimeoutException
	 */
	public void elementText(HtmlElement element, String text) throws TimeoutException {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(browser.getWebDriver())
	       .withTimeout(timeout, TimeUnit.SECONDS)
	       .pollingEvery(500, TimeUnit.MILLISECONDS);
		
		// start waiting for given text of element
		wait.until(new ElementHasTextWaitCondition(element, text));
	}

	/**
	 * Waits until element contains the specified text. Useful for waiting for an AJAX call to complete.
	 * 
	 * @param query the found element should contain the text or attribute value specified here 
	 * @param text when the element contains this text the execution of the program will be continued
	 * @throws TimeoutException
	 */
	public void elementText(String query, String text) throws TimeoutException {
		// wait until element is found
		element(text);
		// wait until element text is detected
		elementText(browser.query(QueryFactory.contains(query)).findFirst(), text);
	}

	/**
	 * Waits until the specified JavaScript returns a non-false value.
	 * 
	 * @param expression the JavaScript expression to evaluate
	 * @param timeout time in seconds to wait until a TimeoutException is thrown
	 * @throws TimeoutException
	 */
	public void evaluatedJavaScriptExpression(String expression, int timeout) throws TimeoutException {
		WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), timeout);
		
		// start waiting for adequate evaluation of expression
		wait.until(new JavaScriptEvaluatedWaitCondition(browser, expression));
	}

	/**
	 * Waits until the specified JavaScript returns a non-false value.
	 * 
	 * @param expression the JavaScript expression to evaluate
	 * @throws TimeoutException
	 */
	public void evaluatedJavaScriptExpression(String expression) throws TimeoutException {
		evaluatedJavaScriptExpression(expression, DEFAULT_TIMEOUT_ON_WAIT);
	}
	
}
