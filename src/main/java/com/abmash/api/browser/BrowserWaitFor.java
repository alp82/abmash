package com.abmash.api.browser;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.core.browser.waitcondition.ElementHasTextWaitCondition;
import com.abmash.core.browser.waitcondition.ElementWaitCondition;
import com.abmash.core.browser.waitcondition.JavaScriptEvaluatedWaitCondition;

import java.util.concurrent.TimeoutException;

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
public class BrowserWaitFor {
	
	private static final int DEFAULT_TIMEOUT_ON_WAIT = 20;
	
	private Browser browser;
	private int timeout;

	/**
	 * Constructs new BrowserWait instance to be able to define wait conditions.
	 * <p>
	 * The default timeout length is {@value DEFAULT_TIMEOUT_ON_WAIT} seconds.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 */
	public BrowserWaitFor(Browser browser) {
		this(browser, DEFAULT_TIMEOUT_ON_WAIT);
	}

	/**
	 * Constructs new BrowserWait instance to be able to define wait conditions.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 * @param timeout wait for this amount of seconds until a timeout exception is thrown
	 */
	public BrowserWaitFor(Browser browser, int timeout) {
		this.browser = browser;
		this.timeout = timeout;
	}

	/**
	 * Waits until element with the specified text is found. Useful for waiting for an AJAX call to complete.
	 * 
	 * @param query the element which needs to be found, identified by the visible text or attribute values 
	 * @throws TimeoutException
	 */
	public void element(String query) throws TimeoutException {
		WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), timeout);
		
		// start waiting for given text of element
		wait.until(new ElementWaitCondition(browser.query().has(query).isText().findFirst()));
	}

	/**
	 * Waits until element contains the specified text. Useful for waiting for an AJAX call to complete.
	 * 
	 * @param element the element to observe
	 * @param text when the element contains this text the execution of the program will be continued
	 * @throws TimeoutException
	 */
	public void elementText(HtmlElement element, String text) throws TimeoutException {
		WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), timeout);
		
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
		elementText(browser.query().has(query).isText().findFirst(), text);
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
