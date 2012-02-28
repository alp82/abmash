package com.abmash.api.browser;

import java.io.InputStream;

import com.abmash.api.Browser;
import com.abmash.api.data.JavaScriptResult;
import com.abmash.core.browser.interaction.JavaScriptExecution;
import com.abmash.core.tools.IOTools;


/**
 * Execute or evaluate custom JavaScript, used by calling {@link Browser#javaScript(String)}.
 * <p>
 * Executing a script returns either true if the result was non-false, or false if it was empty.
 * Evaluating a script returns the return value of the script.
 * <p>
 * JavaScript can be executed or evaluated synchronously or asynchronously. The parameter is optional
 * and can be used to execute the script on a specific object instead of the whole document.
 * <ul>
 * <li>{@link JavaScript#execute(Object...)} executes the script synchronously</li>
 * <li>{@link JavaScript#executeAsync(Object...)} executes the script asynchronously</li>
 * <li>{@link JavaScript#evaluate(Object...)} evaluates the script synchronously</li>
 * <li>{@link JavaScript#evaluateAsync(Object...)} evaluates the script asynchronously</li>
 * <ul>
 * <p>
 * @author Alper Ortac
 */
public class JavaScript {
	
	private Browser browser;
	
	private String script;

	/**
	 * Constructs new BrowserJavaScript instance for running JavaScript.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 * @param script the JavaScript to execute or evaluate
	 */
	public JavaScript(Browser browser, String script) {
		this.browser = browser;
		this.script = script;
		// load prerequisites
		// TODO cache the string
		// TODO custom prerequisites
		(new JavaScriptExecution(browser, loadPrerequisites(), true)).execute();
	}

	/**
	 * Constructs new BrowserJavaScript instance for running JavaScript.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 * @param script the JavaScript to execute or evaluate
	 * @param isFile if true, the script parameter is taken as JavaScript filename which contains the script  
	 */
	public JavaScript(Browser browser, String script, Boolean isFile) {
		this(browser, isFile ? getJsFromFile(script) : script);
	}

	/**
	 * Evaluates JavaScript synchronously.
	 * 
	 * @param args optional list of objects which are passed to the script as arguments, accessable with arguments[0] etc.
	 * @return returned value of executed script
	 */
	public JavaScriptResult evaluate(Object... args) {
		JavaScriptExecution js = new JavaScriptExecution(browser, script, true, args);
		js.execute();
		return js.getResult();
	}

	/**
	 * Evaluates JavaScript asynchronously.
	 * 
	 * @param args optional list of objects which are passed to the script as arguments, accessable with arguments[0] etc.
	 * @return returned value of executed script
	 */
	public JavaScriptResult evaluateAsync(Object... args) {
		JavaScriptExecution js = new JavaScriptExecution(browser, script, false, args);
		js.execute();
		return js.getResult();
	}
	
	private String loadPrerequisites() {
		String prerequisiteScripts = "";

		// load jquery
		prerequisiteScripts += "if(typeof jQuery == 'undefined') {\n" + getJsFromFile("jquery.min") + "\n}\n";
		
		// don't collide with other frameworks (i.e. Prototype)
		prerequisiteScripts += "jQuery.noConflict();\n";

		// don't load custom scripts each time, only once and if it's needed
		prerequisiteScripts += "if(typeof abmashCustomScripts == 'undefined') {\n";
		
		prerequisiteScripts += "abmashCustomScripts = { 'loaded': true };\n";
			
		// custom attribute selector which is case insensitive in jQuery
		// Usage in JavaScript:
		//   var searchInputs = jQuery('input:attr(value, "submit")');
		prerequisiteScripts += getJsFromFile("jquery-attr-caseinsensitive");
		
		// custom visual closeness/direction selector in jQuery
		// Usage in JavaScript:
		//   var divs = jQuery('div:above("selector")');
		//   var divs = jQuery('div:below("selector")');
		//   var divs = jQuery('div:leftTo("selector")');
		//   var divs = jQuery('div:rightTo("selector")');
		// Parameters:
		//   selector: any jQuery selector which finds exactly one element
		prerequisiteScripts += getJsFromFile("jquery-visual");
		
		// get unique css selector from elements in jQuery
		// Usage in JavaScript:
		//    var path = $('#foo').getPath();
		prerequisiteScripts += getJsFromFile("jquery-getpath");
		
		// Prototypes for base objects
		prerequisiteScripts += getJsFromFile("javascript-prototypes");
			
		prerequisiteScripts += "}\n";
		
		// execute all needed scripts
		return prerequisiteScripts;
	}
	
	private static String getJsFromFile(String filename) {
		InputStream stream = JavaScript.class.getResourceAsStream("/js/" + filename + ".js");
		return IOTools.convertStreamToString(stream);
	}

}
