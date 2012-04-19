package com.abmash.api.browser;

import java.io.InputStream;

import com.abmash.api.Browser;
import com.abmash.core.browser.JavaScriptResult;
import com.abmash.core.browser.interaction.JavaScriptExecution;
import com.abmash.core.tools.IOTools;


/**
 * Execute or evaluate custom JavaScript, used by calling {@link Browser#javaScript(String, Object...)}.
 * <p>
 * Executing a script returns either true if the result was non-false, or false if it was empty.
 * Evaluating a script returns the return value of the script.
 * <p>
 * JavaScript can be executed or evaluated synchronously or asynchronously. The parameter is optional
 * and can be used to execute the script on a specific object instead of the whole document.
 * <ul>
 * <li>{@link JavaScript#evaluate(Browser, Object...)} evaluates the script synchronously</li>
 * <li>{@link JavaScript#evaluateAsync(Browser, Object...)} evaluates the script asynchronously</li>
 * <ul>
 * <p>
 * @author Alper Ortac
 */
public class JavaScript {
	
	private String script;

	/**
	 * Constructs new BrowserJavaScript instance for running JavaScript.
	 * 
	 * @param script the JavaScript to execute or evaluate
	 */
	public JavaScript(String script) {
		this.script = script;
		// load prerequisites
		// TODO cache the string
		// TODO custom prerequisites
	}

	/**
	 * Constructs new BrowserJavaScript instance for running JavaScript.
	 * 
	 * @param script the JavaScript to execute or evaluate
	 * @param isFile if true, the script parameter is taken as JavaScript filename which contains the script  
	 */
	public JavaScript(String script, Boolean isFile) {
		this(isFile ? getJsFromFile(script) : script);
	}

	/**
	 * Evaluates JavaScript synchronously.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 * @param args optional list of objects which are passed to the script as arguments, accessable with arguments[0] etc.
	 * @return returned value of executed script
	 */
	public JavaScriptResult evaluate(Browser browser, Object... args) {
		loadPrerequisites(browser);
		JavaScriptExecution js = new JavaScriptExecution(browser, script, true, args);
		js.execute();
		return js.getResult();
	}

	/**
	 * Evaluates JavaScript asynchronously.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 * @param args optional list of objects which are passed to the script as arguments, accessable with arguments[0] etc.
	 * @return returned value of executed script
	 */
	public JavaScriptResult evaluateAsync(Browser browser, Object... args) {
		loadPrerequisites(browser);
		JavaScriptExecution js = new JavaScriptExecution(browser, script, false, args);
		js.execute();
		return js.getResult();
	}
	
	private void loadPrerequisites(Browser browser) {
		String prerequisiteScripts = "";

		// load jquery
		prerequisiteScripts += "if(typeof jQuery == 'undefined') {\n" + getJsFromFile("jquery.min") + "\n}\n";
		
		// don't collide with other frameworks (i.e. Prototype)
		prerequisiteScripts += "if(typeof jQuery.noConflict != 'undefined') { jQuery.noConflict(); }\n";

		// don't load custom scripts each time, only once and if it's needed
		prerequisiteScripts += "if(typeof abmashCustomScripts == 'undefined') {\n";
		
		prerequisiteScripts += "abmashCustomScripts = { 'loaded': true };\n";
			
		// Prototypes for base objects
		prerequisiteScripts += getJsFromFile("javascript-prototypes");
			
		// xpath support for jquery
		// Usage in JavaScript:
		//   var paragraphs = jQuery().xpath('//p');
		prerequisiteScripts += getJsFromFile("jquery.xpath");

		// get unique css selector from elements in jQuery
		// Usage in JavaScript:
		//    var path = $('#foo').getPath();
		prerequisiteScripts += getJsFromFile("jquery-getpath");
		
		// custom attribute selector which is case insensitive in jQuery
		// Usage in JavaScript:
		//   var addressElements = jQuery('div').textMatch('CONTAINS', 'AdDresS')');
		//   var searchInputs = jQuery('input').attrMatch('CONTAINS', 'value', "SUbmIT")');
		prerequisiteScripts += getJsFromFile("jquery-caseinsensitive");
		
		// custom attribute names getter
		// Usage in JavaScript:
		//   var attributes = jQuery('div#header').getAttributeNames();
		prerequisiteScripts += getJsFromFile("jquery-attributes");

		// abmash control scripts
		prerequisiteScripts += getJsFromFile("abmash-control");

		// custom visual closeness/direction selector in jQuery
		// Usage in JavaScript:
		//   divs = jQuery('div:above(jQuery("selector"))');
		//   divs = jQuery('div:below(jQuery("selector"))');
		//   divs = jQuery('div:leftTo(jQuery("selector"))');
		//   divs = jQuery('div:rightTo(jQuery("selector"))');
		// Parameters:
		//   selector: any jQuery object
		prerequisiteScripts += getJsFromFile("abmash-visual");
		
		// abmash image processing
		prerequisiteScripts += getJsFromFile("abmash-image");
		
		prerequisiteScripts += "}\n";
		
		// execute all needed scripts at once
		(new JavaScriptExecution(browser, prerequisiteScripts, true)).execute();
	}
	
	private static String getJsFromFile(String filename) {
		InputStream stream = JavaScript.class.getResourceAsStream("/js/" + filename + ".js");
		return IOTools.convertStreamToString(stream) + "\n";
	}

}
