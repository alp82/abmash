package com.abmash.api;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.joda.time.DateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.abmash.api.query.QueryFactory.*;

import com.abmash.api.browser.Debug;
import com.abmash.api.browser.Frame;
import com.abmash.api.browser.History;
import com.abmash.api.browser.JavaScript;
import com.abmash.api.browser.WaitFor;
import com.abmash.api.browser.Window;
import com.abmash.api.query.Query;
import com.abmash.api.query.QueryFactory;
import com.abmash.core.browser.BrowserConfig;
import com.abmash.core.browser.JavaScriptResult;
import com.abmash.core.browser.interaction.OpenURL;
import com.abmash.core.document.Document;
import com.abmash.core.query.predicate.Predicate;
import com.abmash.core.tools.IOTools;


/**
 * Main class for using a browser instance and interacting with it. You are encouraged to use more than one browser instance
 * to interact with more than one page at a time. This can prove useful if you are switching between the same pages multiple
 * times.
 * <p>
 * <strong>Examples:</strong>
 * <ul>
 * <li><code>browser.click("Save");</code> clicks on a link or button labeled <em>Save</em>
 * <li><code>browser.type("Username", "Jack");</code> enter the text <em>Jack</em> in the input field labeled <em>Username</em>
 * <li><code>browser.select("language", "english");</code> selects the options <em>english</em> in the dropdown/list labeled <em>language</em>
 * <li><code>HtmlElement firstTitle = browser.query(headline()).findFirst();</code> finds the first title element on the current page and saves them</li>
 * <li><code>HtmlElements imagesLeftOfTitle = browser.query(image(), leftOf(title)).find();</code> finds all image elements left of the previously found title</li>
 * </ul>
 * <p>
 * Elements can be searched by queries to get their values and text, and to interact with them. Search queries are <strong>case-sensitive</strong>.
 * <p>
 * Use {@link Browser#click(String)} to search for elements with the specified name or label and click the best match.
 * <p>
 * Use {@link Browser#type(String, String)} to search for input elements (usually {@code <input>} or {@code <textarea>}, but also
 * rich text editors used by many CMS, forums or blog applications) with the specified name or label, get the best match
 * and enter the given text.
 * <p>
 * Use {@link Browser#choose(String, String)} to search for {@code <select>} elements (usually a dropdown selection or a box with a list
 * of options) with the specified name or label, get the best match and select the given option from the list.
 * <p>
 * Use {@link Browser#query(Predicate...)} to create a {@link Query} instance and to search for {@link HtmlElements}. Predicates can be arbitrarily combined.
 * The call of {@link Query#find()} or {@link Query#findFirst()} will return the result(s).
 * <p>
 * Found elements can be further interacted with, see {@link HtmlElement}.
 * 
 * @see Query
 * @see HtmlElement
 * @see HtmlElements
 * @author Alper Ortac
 */
public class Browser {

	/**
	 * Logging instance to send messages to preconfigured devices.
	 * See src/main/resources/logback.xml for details.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Browser.class);

	private RemoteWebDriver webDriver;

	private History history;
	private Window window;
	private Frame frame;
	private WaitFor waitFor;
	private Debug debug;


	// TODO http://twill.idyll.org/

	// -----------------------------------------------------------------------
	// Main methods
	// -----------------------------------------------------------------------

	/**
	 * Starts new browser session. Opens the browser and waits until it is ready.
	 * <p>
	 * The default browser configuration is the use of Firefox 4 and can be overwritten by creating a default.properties file.
	 * TODO explanation properties. For further customization see also {@link #Browser(String, BrowserConfig)}.
	 * <p>
	 * Each browser instance needs to be closed at the end of the program by calling the {@link #close()} method.
	 * Use {@link #openUrl(String)} to open another URL in the browser.
	 * 
	 * @see #close()
	 * @see #openUrl(String)
	 */
	public Browser() {
		// create new browser config and instantiate all needed classes
		this(null, new BrowserConfig());
	}
	
	/**
	 * Starts new browser session. Opens the browser, loads the given URL and waits until it is ready.
	 * <p>
	 * The default browser configuration is the use of Firefox 4 and can be overwritten by creating a default.properties file.
	 * TODO explanation properties. For further customization see also {@link #Browser(String, BrowserConfig)}.
	 * <p>
	 * Each browser instance needs to be closed at the end of the program by calling the {@link #close()} method.
	 * Use {@link #openUrl(String)} to open another URL in the browser.
	 * 
	 * @param url The URL to open, set to null to prevent loading a page
	 * @see #close()
	 * @see #openUrl(String)
	 */
	public Browser(String url) {
		// create new browser config and instantiate all needed classes
		this(url, new BrowserConfig());
	}
	
	/**
	 * Starts new browser session with specified {@link BrowserConfig}. Opens the browser, loads the given URL and waits until it is ready.
	 * <p>
	 * Each browser instance needs to be closed at the end of the program by calling the {@link #close()} method.
	 * Use {@link #openUrl(String)} to open another URL in the browser.
	 * 
	 * @param url The URL to open, set to null to prevent loading a page
	 * @param config BrowserConfig instance
	 * @see #close()
	 * @see #openUrl(String)
	 */
	public Browser(String url, BrowserConfig config) {
		// use browser config and instantiate all needed classes
		webDriver = config.getWebDriver();
		// TODO where to put detection of alerts and popups?
		history = new History(this);
		window = new Window(this);
		frame = new Frame(this);
		waitFor = new WaitFor(this);
		debug = new Debug(this);
		openUrl(url);
	}

	/**
	 * Opens page with specified URL.
	 * <p>
	 * If there is more than one window open, the URL will be opened in current focused browser window.
	 * 
	 * @param url the URL to open, <code>null</code> does not open a web page
	 */
	public void openUrl(String url) {
		new OpenURL(this, url).execute();
	}
	
	/**
	 * Closes all browser windows and stops all browser interactions.
	 */
	public void close() {
		log().info("Closing browser");
		webDriver.close();
		webDriver.quit();
	}
	
	// -----------------------------------------------------------------------
	// Browser functionality
	// -----------------------------------------------------------------------

	/**
	 * Allows to interact with the browser history.
	 * 
	 * @return the browser history manager
	 */
	public History history() {
		return history;
	}

	/**
	 * Allows to interact with browser windows. Windows can be opened, closed and focused.
	 * 
	 * @return the browser window manager
	 */
	public Window window() {
		return window;
	}

	/**
	 * Allows to focus page frames and iframes.
	 * 
	 * @return the browser frame manager
	 */
	public Frame frame() {
		return frame;
	}

	/**
	 * Allows to wait for the browser to finish specified tasks.
	 * 
	 * @return the browser wait manager
	 */
	public WaitFor waitFor() {
		return waitFor;
	}

	/**
	 * Debugging methods to highlight elements or pause the execution.
	 * 
	 * @return the browser debug manager
	 */
	public Debug debug() {
		return debug;
	}
	
	/**
	 * Loads JavaScript and evaluates it synchronously.
	 * <p>
	 * If needed, the jQuery library is automatically loaded.
	 * <p>
	 * Examples:
	 * <pre>
	 * browser.javaScript("alert('info message');");
	 * JavaScriptResult result = browser.javaScript("return jQuery('#content').html();");
	 * </pre>
	 * 
	 * @param script The script to run
	 * @return JavaScriptResult result object to retrieve the return value and type
	 */
	public JavaScriptResult javaScript(String script, Object... args) {
		return new JavaScript(script).evaluate(this, args);
	}
	
	/**
	 * Reads JavaScript from a file and evaluates it synchronously.
	 * <p>
	 * The files need to be placed in the src/main/resources/js directory.
	 * <p>
	 * If needed, the jQuery library is automatically loaded.
	 * 
	 * @param scriptFilename The script to load (without the file extension .js)
	 * @return JavaScriptResult result object to retrieve the return value and type
	 */
	public JavaScriptResult javaScriptFromFile(String scriptFilename, Object... args) {
		return new JavaScript(scriptFilename, true).evaluate(this, args);
	}
	
	/**
	 * Loads JavaScript and evaluates it asynchronously.
	 * <p>
	 * If needed, the jQuery library is automatically loaded.
	 * <p>
	 * Examples:
	 * <pre>
	 * browser.javaScriptAsync("alert('info message');");
	 * JavaScriptResult result = browser.javaScriptAsync("return jQuery('#content').html();");
	 * </pre>
	 * 
	 * @param script The script to run
	 * @return JavaScriptResult result object to retrieve the return value and type
	 */
	public JavaScriptResult javaScriptAsync(String script, Object... args) {
		return new JavaScript(script).evaluateAsync(this, args);
	}
	
	/**
	 * Reads JavaScript from a file and evaluates it asynchronously.
	 * <p>
	 * The files need to be placed in the src/main/resources/js directory.
	 * <p>
	 * If needed, the jQuery library is automatically loaded.
	 * 
	 * @param scriptFilename The script to load (without the file extension .js)
	 * @return JavaScriptResult result object to retrieve the return value and type
	 */
	public JavaScriptResult javaScriptFromFileAsync(String scriptFilename, Object... args) {
		return new JavaScript(scriptFilename, true).evaluateAsync(this, args);
	}
	
	/**
	 * Reads CSS from a string and injects it into the current web page.
	 * Image urls are converted to binary data so that they can be displayed.
	 * <p>
	 * The image files need to be placed in the src/main/resources/images directory.
	 * 
	 * @param styleDefinitions the stylesheet definitions
	 * @param flatImageFolder true if all images are in the same folder or false if the images are in the corresponding subfolders
	 * @return this browser instance
	 */
	public Browser css(String styleDefinitions, Boolean flatImageFolder, Object... args) {
		Pattern pattern;
		if(flatImageFolder) {
			pattern = Pattern.compile("url\\((?:.*?\\/)(.*?).(png|gif)\\)");
		} else {
			pattern = Pattern.compile("url\\(((?:.*?\\/).*?).(png|gif)\\)");
		}
		Matcher matcher = pattern.matcher(styleDefinitions);
		StringBuffer styleDefinitionsWithInlineImageData = new StringBuffer();
		int lastMatchEndPosition = 0;
		while(matcher.find()) {
			String filename = matcher.group(1);
			String extension = matcher.group(2);
			lastMatchEndPosition = matcher.end();
			matcher.appendReplacement(styleDefinitionsWithInlineImageData, "url(" + IOTools.convertImageToBinaryData(
							JavaScript.class.getResourceAsStream("/images/" + filename + "." + extension), extension) + ")");
		}
		// add all definitions from the last match until the end of the stylesheet
		styleDefinitionsWithInlineImageData.append(styleDefinitions.substring(lastMatchEndPosition));
		String script = "jQuery('<style type=\"text/css\">" + styleDefinitionsWithInlineImageData.toString() + "</style>').appendTo('html > head');";
		new JavaScript(script).evaluate(this, args);
		return this;
	}
	
	/**
	 * Reads CSS from a file and injects it into the current web page.
	 * Image urls are converted to binary data so that they can be displayed.
	 * <p>
	 * The css files need to be placed in the src/main/resources/css directory.
	 * <p>
	 * The image files need to be placed in the src/main/resources/images directory.
	 * 
	 * @param stylesheetFile the stylesheet to load (without the file extension .css)
	 * @param flatImageFolder true if all images are in the same folder or false if the images are in the corresponding subfolders
	 * @return this browser instance
	 */
	public Browser cssFromFile(String stylesheetFile, Boolean flatImageFolder, Object... args) {
		InputStream stream = JavaScript.class.getResourceAsStream("/css/" + stylesheetFile + ".css");
		// TODO the css is not allowed to have line breaks, why?
		return css(IOTools.convertStreamToString(stream).replace("\n", " "), flatImageFolder, args);
	}
	
//	/**
//	 * Reads PNG from a file and injects it into the current web page.
//	 * <p>
//	 * The files need to be placed in the src/main/resources/css directory.
//	 * 
//	 * @param stylesheet the stylesheet to load (without the file extension .css)
//	 * @return this browser instance
//	 */
//	public Browser imagePngFile(String imageFile) {
//		InputStream stream = BrowserJavaScript.class.getResourceAsStream("/images/" + imageFile + ".png");
//		String script = "jQuery('');";
//		javaScript(script).execute();
//		return this;
//	}
	
	// -----------------------------------------------------------------------
	// HtmlQuery
	// -----------------------------------------------------------------------

	// TODO root elements?
	public Query query(Predicate... predicates) {
		return QueryFactory.query(this, predicates);
	}

//	/**
//	 * Creates {@link HtmlQuery} instance to find elements on current page.
//	 * <p>
//	 * <strong>Examples:</strong>
//	 * <ul>
//	 * <li><code>browser.query().has("result").findFirst();</code> searches for elements containing the attribute or
//	 * inner text <em>result</em></li> 
//	 * <li><code>browser.query().isClickable().findFirst();</code> searches for clickable elements like links and buttons</li> 
//	 * <li>for more examples see {@link HtmlQuery}</li>
//	 * </ul>
//	 * <p>
//	 * <strong>Description:</strong>
//	 * <p>
//	 * Define conditions by chaining <code>is</code>, <code>has</code>, <code>tag</code> and <code>select</code>
//	 * methods. Get the query result by executing {@link HtmlQuery#find()} or {@link HtmlQuery#findFirst()}.
//	 * 
//	 * @return HtmlQuery instance to add an arbitrary number of find conditions
//	 * @see HtmlQuery
//	 */
//	public HtmlQuery query() {
//		return new HtmlQuery(this);
//	}
//	
//	/**
//	 * Creates {@link HtmlQuery} instance to filter elements on current page.
//	 * <p>
//	 * TODO examples
//	 * 
//	 * @return HtmlQuery instance to add an arbitrary number of find conditions
//	 * @see Browser#query()
//	 * @see HtmlQuery#subsetOf(HtmlElements)
//	 */
//	public HtmlQuery query(HtmlElements elementsToFilter) {
//		return new HtmlQuery(this).subsetOf(elementsToFilter);
//	}
	
	// -----------------------------------------------------------------------
	// Browser Interaction with HtmlElements
	// -----------------------------------------------------------------------
	
	/**
	 * Searches for a clickable element with specified query string and clicks it.
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.click("Save");</code> clicks on a link or button labeled <em>Save</em></li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * Elements having an attribute value or visible text containing the query string will
	 * be added to the result set. The first result will be used for clicking. If you already have an
	 * {@link HtmlElement} instance use {@link HtmlElement#click()} instead.
	 * <p>
	 * Note that if the page reloads after a click, all found {@link HtmlElement} instances may lose their validity.
	 * 
	 * @param clickable element label
	 * @return HtmlElement to further interact with that element or {@code null} if element could not be found
	 * @see HtmlElement#click()
	 */
	public HtmlElement click(String clickable) {
		HtmlElement element = query(clickable(clickable)).findFirstWithWait();
		return element instanceof HtmlElement ? element.click() : null;
	}
	
	/**
	 * Searches for a element with specified query string and hovers it with the mouse.
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.hover("News");</code> fires the "mouseover" event on the element labeled <em>News</em></li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * Elements having an attribute value or visible text containing the query string will
	 * be added to the result set. The first result will be used for hovering. If you already have an
	 * {@link HtmlElement} instance use {@link HtmlElement#hover()} instead.
	 * 
	 * @param clickable element which will be used for hovering with the mouse
	 * @return HtmlElement to further interact with that element or {@code null} if element could not be found
	 * @see HtmlElement#hover()
	 */
	public HtmlElement hover(String clickable) {
		HtmlElement element = query(clickable(clickable)).findFirstWithWait();
		return element instanceof HtmlElement ? element.hover() : null;
	}
	
	/**
	 * Searches for a element with specified query string and drags it with the mouse to another element.
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.dragTo("product", "cart");</code> drags the element labeled <em>Product</em> to the
	 * element labeled <em>Cart</em></li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * Elements having an attribute value or visible text containing the query string will
	 * be added to the result set. The first result will be used for dragging. If you already have an
	 * {@link HtmlElement} instance use {@link HtmlElement#dragTo(HtmlElement)} instead.
	 * 
	 * @param elementToDrag source element label; this is going to be dragged
	 * @param elemenToDropOn target element label; the source element is dropped here
	 * @return HtmlElement to further interact with that element or {@code null} if element could not be found
	 * @see HtmlElement#dragTo(HtmlElement)
	 */
	public HtmlElement drag(String elementToDrag, String elemenToDropOn) {
		HtmlElement source = query(contains(elementToDrag)).findFirstWithWait();
		HtmlElement target = query(contains(elemenToDropOn)).findFirstWithWait();
		return source instanceof HtmlElement && target instanceof HtmlElement ? source.dragTo(target) : null;
	}
	
	/**
	 * Searches for element with specified query string and enters the text. 
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.type("location","New York").submit();</code> types in the text <em>New York</em>
	 * in the input field named or labeled <em>location</em> and then submits the form which contains that element.</li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * Elements having an attribute value or visible text containing the query string will
	 * be added to the result set. The first result will be used for typing in the text. If you already have an
	 * {@link HtmlElement} instance use {@link HtmlElement#type(String)} instead. Use {@link HtmlElement#submit()}
	 * on the returned object to submit the form.
	 * 
	 * @param typable the label of the input field
	 * @param text the text to type in
	 * @return HtmlElement to further interact with the form element, for instance to {@link HtmlElement#submit()} the form,
	 * or {@code null} if element could not be found
	 * @see HtmlElement#type(String)
	 */
	public HtmlElement type(String typable, String text) {
		HtmlElement element = query(typable(typable)).findFirstWithWait();
		return element instanceof HtmlElement ? element.type(text) : null;
	}
	
	/**
	 * Searches for dropdown/list input field and selects the specified option. Use {@link HtmlElement#submit()}
	 * on the returned object to submit the form.
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.choose("language","english").submit();</code> selects the <em>english</em> option from the list
	 * select field named or labeled <em>language</em> and then submits the form which contains that element.</li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * TODO Select description
	 * 
	 * @param choosable dropdown/select element
	 * @param option the label or value of the option to select
	 * @return HtmlElement to further interact with the form element, for instance to {@link HtmlElement#submit()} the form,
	 * or {@code null} if element could not be found
	 */
	public HtmlElement choose(String choosable, String option) {
		HtmlElement element = query(choosable(choosable)).findFirstWithWait();
		return element instanceof HtmlElement ? element.choose(option) : null;
	}
	
	/**
	 * Searches for dropdown/list input field and deselects the specified option. Use {@link HtmlElement#submit()}
	 * on the returned object to submit the form.
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.unchoose("language","english").submit();</code> deselects the <em>english</em> option from the list
	 * select field named or labeled <em>language</em> and then submits the form which contains that element.</li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * TODO Select description
	 * 
	 * @param choosable dropdown/select element
	 * @param option the label or value of the option to deselect
	 * @return HtmlElement to further interact with the form element, for instance to {@link HtmlElement#submit()} the form,
	 * or {@code null} if element could not be found
	 */
	public HtmlElement unchoose(String choosable, String option) {
		HtmlElement element = query(choosable(choosable)).findFirstWithWait();
		return element instanceof HtmlElement ? element.unchoose(option) : null;
	}
	
	/**
	 * Searches for checkbox input field and toggles it. Use {@link HtmlElement#submit()}
	 * on the returned object to submit the form.
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.checkToggle("newsletter").submit();</code> toggles the checkbox labeled <em>newsletter</em>.</li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * TODO Select description
	 * 
	 * @param checkable checkbox element
	 * @return HtmlElement to further interact with the form element, for instance to {@link HtmlElement#submit()} the form,
	 * or {@code null} if element could not be found
	 */	
	public HtmlElement checkToggle(String checkable) {
		HtmlElement element = query(checkable(checkable)).findFirstWithWait();
		return element instanceof HtmlElement ? element.click() : null;
	}
	
	/**
	 * Searches for calendar/date picker input fields and selects the specified date. Use {@link HtmlElement#submit()}
	 * on the returned object to submit the form.
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.chooseDate("Arrival", new Date()).submit();</code> selects the specified date in the date picker
	 * named or labeled <em>Arrival</em> and then submits the form which contains that element.</li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * TODO Select description
	 * 
	 * @param datepicker calendar/date picker element
	 * @param dateTime the date to select
	 * @return HtmlElement to further interact with the form element, for instance to {@link HtmlElement#submit()} the form,
	 * or {@code null} if element could not be found
	 */
	public HtmlElement chooseDate(String datepicker, DateTime dateTime) {
		HtmlElement element = query(datepicker(datepicker)).findFirstWithWait();
		return element instanceof HtmlElement ? element.chooseDate(dateTime) : null;
	}
	
	/**
	 * Submits the form, which contains the given form element. 
	 * <p>
	 * <strong>Example:</strong>
	 * <ul>
	 * <li><code>browser.submit("address");</code> submits the form which contains the form element labeled <em>address</em>.</li> 
	 * </ul>
	 * <p>
	 * 
	 * @param submittable form submit element
	 * @return HtmlElement to further interact with the form element or {@code null} if element could not be found
	 */
	public HtmlElement submit(String submittable) {
		HtmlElement element = query(submittable(submittable)).findFirstWithWait();
		return element instanceof HtmlElement ? element.submit() : null;
	}
	
//	/**
//	 * Finds the first list whose visible text or attribute values contains the given search query,
//	 * and the {@link List} representation.
//	 * <p> 
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>List ingredientTable = browser.getTable("Ingredient");</code> returns the first table which contains the
//	 * text <em>Ingredient</em>.</li> 
//	 * <li><code>ingredientTable.get(0);</code> returns the first list item.</li> 
//	 * </ul>
//	 * 
//	 * @param query text which is contained in the list (visible text or attribute value)
//	 * @return List representation of this element
//	 * @see List
//	 */
//	public List getList(String query) {
//		return new List(this, query);
//	}
//	
//	/**
//	 * Returns the {@link List} representation of a {@code <ul>}, {@code <ol>} or {@code <dl>} element.
//	 * <p> 
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>HtmlElement listTitle = browser.query().isTitle().has("Ingredients").findFirst();</code> Returns the first element being a title labeled <em>Ingredients</em></li>
//	 * <li><code>HtmlElement list = browser.query().isList().below(tableTitle).findFirst();</code> returns the first list below the previously selected title element</li>
//	 * <li><code>List ingredientList = browser.getList(table);</code> finally returns the list representation.</li> 
//	 * <li><code>ingredientList.get(0);</code> returns the first list item.</li> 
//	 * <li>See {@link List} for more examples on how to interact with the table.</li> 
//	 * </ul>
//	 * 
//	 * @param listElement has to be an {@link HtmlElement} with the tag {@code <ul>}, {@code <ol>} or {@code <dl>}
//	 * @return List representation of this element
//	 * @see List
//	 */
//	public List getList(HtmlElement listElement) {
//		return new List(listElement);
//	}
//	
//	/**
//	 * Finds the first table whose visible text or attribute values contains the given search query,
//	 * and the {@link Table} representation.
//	 * <p> 
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table userTable = browser.getTable("User");</code> returns the first table which contains the
//	 * text <em>User</em>.</li> 
//	 * <li><code>userTable.get(0, "email");</code> returns the <em>email</em> address of the user in the first row.</li> 
//	 * </ul>
//	 * 
//	 * @param query text which is contained in the table (visible text or attribute value)
//	 * @return Table representation of this element
//	 * @see Table
//	 */
//	public Table getTable(String query) {
//		return new Table(this, query);
//	}
//	
//	/**
//	 * Returns the {@link Table} representation of a {@code <table>} element.
//	 * <p> 
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>HtmlElement tableTitle = browser.query().isTitle().has("Userlist").findFirst();</code> Returns the first element being a title labeled <em>Userlist</em></li>
//	 * <li><code>HtmlElement table = browser.query().isTable().below(tableTitle).findFirst();</code> returns the first table below the previously selected title element</li>
//	 * <li><code>Table userTable = browser.getTable(table);</code> finally returns the table representation.</li> 
//	 * <li><code>userTable.getCell(0, "email");</code> returns the <em>email</em> address of the user in the first row.</li> 
//	 * <li>See {@link Table} for more examples on how to interact with the table.</li> 
//	 * </ul>
//	 * 
//	 * @param tableElement has to be an {@link HtmlElement} with the tag {@code <table>}
//	 * @return Table representation of this element
//	 * @see Table
//	 */
//	public Table getTable(HtmlElement tableElement) {
//		return new Table(tableElement);
//	}

	// -----------------------------------------------------------------------
	// Content Output and Parser
	// -----------------------------------------------------------------------

	// TODO parser
//	public DocumentParser getParser() {
//		// depending on current document type, create the appropriate parser instance
//		String contentType = windowManager.getCurrentContentType();
//		
//		DocumentParser parser = new HTMLParser(this);
//		if(false) {
//		}
//		else if (contentType.startsWith("text/html")) {
//			// default
//		}
//		else {
//			// default
//		}
//		return parser;
//	}
	
	// TODO get document for different content types
	// TODO with htmlelement
//	public Document getDocument(String query) {
//		// TODO decision: href, src, background, ...
//		HtmlElement element = query().has(query).is(ElementType.CLICKABLE).findFirst();
//		String href = element.getAttribute("href");
//		URL url = null;
//		try {
//			url = new URL(new URL(getCurrentUrl()), href);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// TODO is windowmanager really responsible for this?
//		String contentType = windowManager.getContentTypeOfURL(url).toLowerCase();
//		
//		Document document = this;
//		if(contentType.endsWith("html")) {
//			// TODO is clicking intended here?
//			element.click();
//		}
//		else if(contentType.endsWith("pdf")) {
//			try {
//				document = new PDFDocument(url);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return document;
//	}

	// -----------------------------------------------------------------------
	// Conversion
	// TODO
	// -----------------------------------------------------------------------

//	public Object getJsonRepresentationOfWebElement(WebElement webElement) {
//		return (new WebElementToJsonConverter()).apply(webElement);
//	}
//
//	public Object getWebElementOfJsonRepresentation(Object json) {
//		return (new JsonToWebElementConverter((RemoteWebDriver) webDriver)).apply(json);
//	}


	// -----------------------------------------------------------------------
	// Take screenshots
	// -----------------------------------------------------------------------
	
	public File getScreenshotAsFile() {
		return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.FILE);
	}
	
	public byte[] getScreenshotAsByteArray() {
		return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
	}
	
	public BufferedImage getScreenshotAsBufferedImage() throws IOException {
		return ImageIO.read(new ByteArrayInputStream(getScreenshotAsByteArray()));
	}
	
	public String getScreenshotAsBase64() {
		return getScreenshotAsBase64(false);
	}
	
	public String getScreenshotAsBase64(boolean withDataPrefix) {
		return (withDataPrefix ? "data:image/png;base64," : "") + ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BASE64);
	}

	// -----------------------------------------------------------------------
	// Getters
	// -----------------------------------------------------------------------

	/**
	 * Gets URL of current page
	 * 
	 * @return URL of current page
	 */
	public String getCurrentUrl() {
		return webDriver.getCurrentUrl();
	}

	/**
	 * Gets HTML source code of current page
	 * 
	 * @return HTML source code of current page
	 */
	public String getPageSource() {
		return webDriver.getPageSource();
	}

	/**
	 * Gets logger instance.
	 * 
	 * @return Logger instance
	 */
	public Logger log() {
		return LOG;
	}

	/**
	 * Gets underlying <a href="http://seleniumhq.org/">Selenium</a> WebDriver instance.
	 * <p>
	 * The <code>WebDriver</code> is internally used to do all interactions with the browser.
	 * In general, it is not necessary to use this manually.
	 * 
	 * @return Selenium WebDriver
	 */
	public RemoteWebDriver getWebDriver() {
		return webDriver;
	}

}
