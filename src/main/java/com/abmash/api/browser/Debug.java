package com.abmash.api.browser;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.api.query.QueryFactory;


/**
 * Additional debug methods for developers, used by calling {@link Browser#debug()}.
 * <p>
 * This class can be used to highlight elements on the page or to stop the execution until a button is pressed.
 * 
 * @author Alper Ortac
 */
public class Debug {
	
	private Browser browser;

	/**
	 * Constructs new BrowserDebug instance for additional debug methods for developers.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 */
	public Debug(Browser browser) {
		this.browser = browser;
	}
	
	/**
	 * Debug method which highlights the given {@link HtmlElement} with a red dashed border.
	 * 
	 * @param element the element to highlight
	 */
	public void highlight(HtmlElement element) {
		if (element instanceof HtmlElement) {
			String jsExp = "jQuery(arguments[0]).css('border', '3px dashed red');";
			browser.javaScript(jsExp, element).getReturnValue();
			browser.log().debug("element highlighted: " + element);
		} else {
			browser.log().debug("element to highlight not found!");
		}
	}
	
	/**
	 * Debug method which highlights all given {@link HtmlElements} with a red dashed border.
	 * 
	 * @param elements
	 * @see #highlight(HtmlElement)
	 */
	public void highlight(HtmlElements elements) {
		for (HtmlElement element: elements) {
			// TODO special highlight of first element, or visible count number
			highlight(element);
		}
	}
	
	/**
	 * Debug method which searches for an element with the given query string and highlights it with a red dashed border.
	 * 
	 * @param query the element to highlight
	 */
	public void highlight(String query) {
		highlight(browser.query(QueryFactory.contains(query)).findFirst());
	}
	
	/**
	 * Debug method which stops the execution of the application and creates a "Continue"
	 * button at the top-left corner of the page. When the button is clicked, the execution continues. 
	 */
	public void breakpoint() {
		browser.log().debug("set breakpoint, waiting until continue button is pressed");
		// remove old finished debug indicators
		browser.javaScript("if(jQuery('#abmash-debug-finished').length) jQuery('#abmash-debug-finished').remove();").getReturnValue();
		// create continue button
		browser.javaScript("jQuery('body').prepend('<div id=\"abmash-debug-continue\" style=\"position: absolute; z-index: 99999;\"><input id=\"abmash-debug-continue-button\" type=\"button\" value=\"Abmash Debug: Continue\" /></div>');").getReturnValue();
		// add click handler to button
		browser.javaScript("jQuery('#abmash-debug-continue-button').click(function() { jQuery('body').append('<div id=\"abmash-debug-finished\" />'); jQuery('abmash-debug-continue').remove(); });").getReturnValue();

		// wait until button is clicked, there is no timeout
		boolean continueFromBreakpoint = false;
		while(!continueFromBreakpoint) {
			try {
				browser.waitFor().evaluatedJavaScriptExpression("jQuery('#abmash-debug-finished').length");
				continueFromBreakpoint = true;
			} catch (Exception e) {
			}
		}
	}
}
