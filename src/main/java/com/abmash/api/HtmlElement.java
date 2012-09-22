package com.abmash.api;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebElement;

import com.abmash.REMOVE.api.HtmlQuery;
import com.abmash.REMOVE.core.htmlquery.condition.ElementCondition.ElementType;
import com.abmash.api.query.Query;
import com.abmash.api.query.QueryFactory;
import com.abmash.core.browser.interaction.Clear;
import com.abmash.core.browser.interaction.Click;
import com.abmash.core.browser.interaction.Click.ClickType;
import com.abmash.core.browser.interaction.DragTo;
import com.abmash.core.browser.interaction.Hover;
import com.abmash.core.browser.interaction.KeyHold;
import com.abmash.core.browser.interaction.KeyPress;
import com.abmash.core.browser.interaction.KeyRelease;
import com.abmash.core.browser.interaction.Select;
import com.abmash.core.browser.interaction.Select.SelectMethod;
import com.abmash.core.browser.interaction.Submit;
import com.abmash.core.browser.interaction.Submit.SubmitMethod;
import com.abmash.core.browser.interaction.Type;
import com.abmash.core.element.Element;
import com.abmash.core.element.Location;
import com.abmash.core.element.Size;


/**
 * Representation of an HTML element on the web page.
 * <p> 
 * The {@link HtmlQuery#findFirst()} method returns an <code>HtmlElement</code>.
 * See {@link HtmlQuery} how to find elements and {@link Browser} how to directly
 * find and interact with elements.
 * <p>
 * <strong>Examples:</strong>
 * <ul>
 * <li><code>HtmlElement saveButton = browser.click("Save");</code> directly searches
 * for the button labeled <em>Save</em> and returns the <code>HtmlElement</code></li>
 * <li><code>HtmlElement inputField = browser.type("Username", "Jack");</code> directly searches
 * for the input field labeled <em>Username</em>, enters the specified text and returns
 * the <code>HtmlElement</code></li>
 * <li><code>HtmlElement myElement = browser.query().isTitle().isClickable().has("today").findFirst();</code> directly searches
 * for clickable titles labeled <em>today</em> and returns the <code>HtmlElement</code></li>
 * </ul>
 * <p>
 * @author Alper Ortac
 * @see Browser
 * @see HtmlQuery
 * @see HtmlElements
 */
public class HtmlElement extends Element {

	// TODO fallback for htmlunit
	private Browser browser;
	private RemoteWebElement seleniumElement;
	private HtmlElements referenceElements = null;
	
	// window and frame
	private String windowName = null;
	private HtmlElement frameElement = null;
	
	// element attributes
	private ArrayList<ElementType> types = new ArrayList<ElementType>();

	private String id;
	private ArrayList<String> attributeNames;
	private Map<String, String> attributes = new HashMap<String, String>();
	// TODO attribute values cache
//	private Object jsonRepresentation;
	
	private Location location;
	private Size size;
	private String tagName;
	private String text;
	private String sourceText;
	private String uniqueSelector;
	
	/**
	 * Creates a new <code>HtmlElement</code> instance and caches most important data.
	 * <p>
	 * Cached element data is the tag name and inner text.
	 * If the page is refreshed or another URL is opened, that data is still
	 * available. But, interacting with the element like clicking or typing in will
	 * throw an Exception.
	 * 
	 * @param browser
	 * @param seleniumElement
	 */
	public HtmlElement(Browser browser, RemoteWebElement seleniumElement) {
		this.browser = browser;
		this.seleniumElement = seleniumElement;
		this.id = seleniumElement.getId();
		// TODO make this optional
		//this.jsonRepresentation = browser.getJsonRepresentationOfWebElement(element);

		// TODO pre-cache important data
//		getTagName();
//		getText();
//		getTextWithSource();
//		getUniqueSelector();
//		getAttributeNames();
	}
	
	
	// default getters
	
	/**
	 * Gets the associated browser instance for this element.
	 * 
	 * @return associated browser instance
	 */
	public Browser getBrowser() {
		return browser;
	}

	/**
	 * Gets the internal id of this element. The same element will have a different id when the page is reloaded.
	 * 
	 * The id is looking like in this example: {390f457d-406b-458a-b4a6-dfebb24aae36}
	 *
	 * @return internal id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the associated Selenium element for this element.
	 * 
	 * @return selenium WebElement
	 */
	public RemoteWebElement getSeleniumElement() {
		switchToElementWindow();
		return seleniumElement;
	}
	
	// actions
	
	/**
	 * Clicks on element.
	 * <p>
	 * Note that if the page reloads after a click, all found {@link HtmlElement} instances may lose their validity.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement button = browser.query().isClickable().has("submit").findFirst();
	 * button.click();
	 * }</pre>
	 *  
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement click() {
		new Click(browser, this, ClickType.CLICK).execute();
		return this;
	}

	/**
	 * Doubleclicks on element.
	 * <p>
	 * Note that if the page reloads after a click, all found {@link HtmlElement} instances may lose their validity.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement file = browser.query().isClickable().has("file").findFirst();
	 * file.doubleClick();
	 * }</pre>
	 *  
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement doubleClick() {
		new Click(browser, this, ClickType.DOUBLECLICK).execute();
		return this;
	}

	/**
	 * Rightclicks on element to open the context menu.
	 * <p>
	 * Note that if the page reloads after a click, all found {@link HtmlElement} instances may lose their validity.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement item = browser.query().isClickable().has("item").findFirst();
	 * item.rightClick();
	 * }</pre>
	 *  
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement rightClick() {
		new Click(browser, this, ClickType.RIGHTCLICK).execute();
		return this;
	}
	
	/**
	 * Hovers on element with the mouse.
	 * <p>
	 * The "mouseover" event will be fired on this element.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement menuLink = browser.query().isClickable().has("menu").findFirst();
	 * menuLink.hover();
	 * }</pre>
	 *  
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement hover() {
		new Hover(browser, this).execute();
		return this;
	}
	
	/**
	 * Drags an element with the mouse on another element.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElements products = browser.query().isClickable().has("item").find();
	 * HtmlElement shoppingCart = browser.query().has("cart").findFirst();
	 * products.dragTo(shoppingCart);
	 * }</pre>
	 *  
	 * @param targetElement the element to drag the element to
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement dragTo(HtmlElement targetElement) {
		new DragTo(browser, this, targetElement).execute();
		return this;
	}
	
	/**
	 * Clears entered text in element.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement input = browser.query().isTypable().has("name").findFirst();
	 * input.clear();
	 * }</pre>
	 *  
	 * @return this {@link HtmlElement}
	 */
	// TODO check if possible
	public HtmlElement clear() {
		new Clear(browser, this).execute();
		return this;
	}

	/**
	 * Enters text in element.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement input = browser.query().isTypable().has("name").findFirst();
	 * input.type("John");
	 * }</pre>
	 *  
	 * @param text
	 * @return this {@link HtmlElement}
	 */
	// TODO check if possible
	public HtmlElement type(String text) {
		new Type(browser, this, text).execute();
		return this;
	}
	
	/**
	 * Chooses option/item from select field
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement select = browser.query().isChoosable().has("language").findFirst();
	 * input.choose("english");
	 * }</pre>
	 * 
	 * @param optionQuery text which represents the select option/item
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement choose(String optionQuery) {
		new Select(browser, this, optionQuery, SelectMethod.SELECT).execute();
		return this;
	}
	
	/**
	 * Deselects option/item from select field
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement select = browser.query().isSelectable().has("ingredients").findFirst();
	 * input.unchoose("tomatoes");
	 * }</pre>
	 * 
	 * @param optionQuery text which represents the select option/item
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement unchoose(String optionQuery) {
		new Select(browser, this, optionQuery, SelectMethod.DESELECT).execute();
		return this;
	}

	/**
	 * Selects the specified date.
	 * 
	 * TODO
	 * 
	 * @param dateTime
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement chooseDate(DateTime dateTime) {
		// TODO detect datepicker type
		// TODO detect date format in input field
		// TODO detect time format and input field
		// TODO detect if sendkeys work for one or the other
		
		// TODO convert date to string 08/22/2013 and check if sendkeys worked
		//type(dateTime.getDateString());
		click();
		// TODO Ajax Wait condition
		
		// TODO year
		
		// TODO exception handler if no element was found
		// TODO first try with month name
		browser.query(QueryFactory.choosable("datepicker"), QueryFactory.below(this)).findFirst().choose(String.valueOf(dateTime.getMonthOfYear() - 1));
		// TODO exception handler if no element was found
		browser.query(
			QueryFactory.clickable(String.valueOf(dateTime.getDayOfMonth())),
			QueryFactory.select("*:attrMatch(CONTAINS, *, \"datepicker\") > a"),
			QueryFactory.below(this)
		).findFirst().click();
		// TODO time
		DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm aa");
		// TODO exception handler if no element was found
		browser.query(
			QueryFactory.choosable("time"),
			QueryFactory.rightOf(this)
		).findFirst().choose(fmt.print(dateTime));
		
		return this;
	}
	
	/**
	 * Submits the form linked to this element.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement input = browser.query().isTypable().has("name").findFirst();
	 * input.submit();
	 * }</pre>
	 * <p>
	 * Examples for forms are search inputs, contact forms or login pages. Submitting causes the form to be sent to the
	 * server and usually reloads the web page or parts of the page.
	 * <p>
	 * Note that if the page reloads after the submit, all found {@link HtmlElement} instances may lose their validity.
	 * 
	 * @return this {@link HtmlElement}
	 */
	public HtmlElement submit() {
		// TODO if element is not an input, choose the best input[type=submit] 
		new Submit(browser, this, SubmitMethod.FORM).execute();
		return this;
	}
	
	/**
	 * Presses key on element. See {@link Keys} for a complete list of possible key names.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement input = browser.query().isTypable().has("name").findFirst();
	 * input.keyPress("enter");
	 * }</pre>
	 * <p>
	 * Note that {@code keyPress()} and {@code keyHold().keyRelease()} are equivalent.
	 * 
	 * @param keyName the name of the key, case-insensitive
	 * @return this {@link HtmlElement}
	 * @see Keys
	 * @see #keyHold(String)
	 * @see #keyRelease(String)
	 */
	public HtmlElement keyPress(String keyName) {
		new KeyPress(browser, this, keyName).execute();
		return this;
	}
	
	/**
	 * Holds key on element. See {@link Keys} for a complete list of possible key names.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement item = browser.query().isTypable().has("item").findFirst();
	 * item.keyHold("ctrl");
	 * }</pre>
	 * <p>
	 * Note that {@code keyPress()} and {@code keyHold().keyRelease()} are equivalent.
	 * 
	 * @param keyName the name of the key, case-insensitive
	 * @return this {@link HtmlElement}
	 * @see Keys
	 * @see #keyPress(String)
	 * @see #keyRelease(String)
	 */
	public HtmlElement keyHold(String keyName) {
		new KeyHold(browser, this, keyName).execute();
		return this;
	}
	
	/**
	 * Releases key on element. See {@link Keys} for a complete list of possible key names.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>{@code
	 * HtmlElement item = browser.query().isTypable().has("item").findFirst();
	 * item.keyHold("ctrl");
	 * }</pre>
	 * <p>
	 * Note that {@code keyPress()} and {@code keyHold().keyRelease()} are equivalent.
	 * 
	 * @param keyName the name of the key, case-insensitive
	 * @return this {@link HtmlElement}
	 * @see Keys
	 * @see #keyPress(String)
	 * @see #keyHold(String)
	 */
	public HtmlElement keyRelease(String keyName) {
		new KeyRelease(browser, this, keyName).execute();
		return this;
	}

	/**
	 * Performs an action on this element. The second parameter is needed for some of them.
	 * <p>
	 * <strong>List of actions:</strong>
	 * <ul>
	 * <li><code>click</code>: performs a click action. <code>param</code> is optional, if set to "right" it will perform a right click.</li>
	 * <li><code>dblclick</code>: performs a double click action. <code>param</code> is not needed here.</li>
	 * <li><code>type</code>: performs a type action. <code>param</code> is mandatory and indicates the text to type in.</li>
	 * <li><code>keypress</code>: performs a key press action. <code>param</code> is mandatory and indicates the key code.</li>
	 * <li><code>keydown</code>: performs a key hold action. <code>param</code> is mandatory and indicates the key code.</li>
	 * <li><code>keyup</code>: performs a key release action. <code>param</code> is mandatory and indicates the key code.</li>
	 * </ul>
	 * @param type the action to perform on this element
	 * @param param additional parameter, may be not needed, optional or mandatory. see list above for details 
	 */
	public void triggerAction(String type, String param) {
		if(type.equals("click") && param.toLowerCase() != "right") this.click();
		if(type.equals("click") && param.toLowerCase() == "right") this.rightClick();
		if(type.equals("dblclick")) this.doubleClick();
		if(type.equals("type")) this.type(param);
		if(type.equals("keypress")) this.keyPress(param);
		if(type.equals("keydown")) this.keyHold(param);
		if(type.equals("keyup")) this.keyRelease(param);
	}
	
	// TODO more form stuff
//	public FormManager formStuff() {
//		/*
//		 * TODO formStuff enter text press key set select option check/toggle
//		 * checkbox/radiobox submit form
//		 */
//		return this;
//	}
	
	// selectors
	
	/**
	 * Creates an {@link HtmlQuery} to find elements below this one.
	 * 
	 * @return HtmlQuery object
	 * @see HtmlQuery
	 * @see HtmlQuery#below(HtmlElement)
	 */
	public Query below() {
		return browser.query(QueryFactory.below(this));
	}
	
	/**
	 * Creates an {@link HtmlQuery} to find elements above this one.
	 * 
	 * @return HtmlQuery object
	 * @see HtmlQuery
	 * @see HtmlQuery#above(HtmlElement)
	 */
	public Query above() {
		return browser.query(QueryFactory.above(this));
	}	
	
	/**
	 * Creates an {@link HtmlQuery} to find elements left to this one.
	 * 
	 * @return HtmlQuery object
	 * @see HtmlQuery
	 * @see HtmlQuery#leftTo(HtmlElement)
	 */
	public Query leftTo() {
		return browser.query(QueryFactory.leftOf(this));
	}	
	
	/**
	 * Creates an {@link HtmlQuery} to find elements right to this one.
	 * 
	 * @return HtmlQuery object
	 * @see HtmlQuery
	 * @see HtmlQuery#rightTo(HtmlElement)
	 */
	public Query rightTo() {
		return browser.query(QueryFactory.rightOf(this));
	}
	
	// setters
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}
	
	public void setAttributeNames(ArrayList<String> attributeNames) {
		this.attributeNames = attributeNames;
	}
	
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public void setUniqueSelector(String uniqueSelector) {
		this.uniqueSelector = uniqueSelector;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setSize(Size size) {
		this.size = size;
	}
	
	// TODO jquery command ?
	
	/**
	 * Gets the parent of this element in the document structure.
	 * <p>
	 * Although the term parent refers to the document source code, mostly it also reflects the visual parent of this element.
	 * 
	 * @return the parent of this element
	 */
	public HtmlElement getParent() {
		String script = "return jQuery(arguments[0]).parent().get(0);";
		// TODO error handling if result is empty
		RemoteWebElement webElement = (RemoteWebElement) evaluateJavaScript(script);
		return new HtmlElement(browser, webElement);
	}	
	
	/**
	 * Gets the siblings of this element in the document structure.
	 * <p>
	 * Although the term sibling refers to the document source code, mostly it also reflects the visual siblings of this element.
	 * 
	 * @return the siblings of this element
	 */
	public HtmlElements getSiblings() {
		String script = "return jQuery(arguments[0]).siblings().get();";
		// TODO error handling if result is empty
		ArrayList<RemoteWebElement> webElements =  (ArrayList<RemoteWebElement>) evaluateJavaScript(script);
		HtmlElements siblings = new HtmlElements();
		for (RemoteWebElement webElement: webElements) {
			siblings.add(new HtmlElement(browser, webElement));
		}
		return siblings;
	}
	
	/**
	 * Gets the children of this element in the document structure.
	 * <p>
	 * Although the term children refers to the document source code, mostly it also reflects the visual children of this element.
	 * 
	 * @return the children of this element
	 */
	public HtmlElements getChildren() {
		String script = "return jQuery(arguments[0]).children().get();";
		// TODO error handling if result is empty
		ArrayList<RemoteWebElement> webElements =  (ArrayList<RemoteWebElement>) evaluateJavaScript(script);
		HtmlElements children = new HtmlElements();
		for (RemoteWebElement webElement: webElements) {
			children.add(new HtmlElement(browser, webElement));
		}
		return children;
	}
	
	// custom getters and setters
	
	/**
	 * Gets element types. These are set when an element was found.
	 * 
	 * @return array of <code>ElementType</code> 
	 * @see ElementType
	 */
	public ArrayList<ElementType> getTypes() {
		return types;
	}
	
	/**
	 * Sets element types. Done when an element was found.
	 */
	public void setTypes(ArrayList<ElementType> types) {
		this.types = types;
	}
	
	/**
	 * Builds unique CSS selector for this element.
	 * 
	 * @return unique css selector
	 */
	public String getUniqueSelector() {
		if(uniqueSelector == null) {
			String script = "return jQuery(arguments[0]).getPath();";
			// TODO error handling if result is empty
			uniqueSelector = (String) evaluateJavaScript(script);
		}
		return uniqueSelector;
	}

	/**
	 * Gets the names of all attributes of this element.
	 * 
	 * @return all attribute names of this element
	 */
	public ArrayList<String> getAttributeNames() {
		if(attributeNames == null) {
			switchToElementWindow();
			String script =  "return jQuery(arguments[0]).getAttributeNames();";
			try {
				attributeNames = (ArrayList<String>) evaluateJavaScript(script);
			} catch (Exception e) {
				attributeNames = new ArrayList<String>();
			}
		}
		return attributeNames;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	/**
	 * Gets the location of this element. The location has an X and a Y coordinate.
	 * 
	 * @return element location
	 */
	public Location getLocation() {
		if(location == null) {
			switchToElementWindow();
			Point loc = seleniumElement.getLocation();
			location = new Location(loc.x, loc.y);
		}
		return location;
	}

	/**
	 * Gets the size of this element. The size has a width and an height.
	 * 
	 * @return element size
	 */
	public Size getSize() {
		if(size == null) {
			switchToElementWindow();
			Dimension sz = seleniumElement.getSize();
			size = new Size(sz.width, sz.height);
		}
		return size;
	}
	
	/**
	 * Gets the tag name of this element.
	 * <p>
	 * <strong>Example:</strong> Given the following HTML source
	 * <pre>{@code
	 * <a href="index.html">Link text</a>
	 * }</pre>
	 * <code>getText()</code> returns "a".
	 * <p>
	 * @return the tag name of the element
	 */
	public String getTagName() {
		// TODO tag name as jquery command + cached data?
		if(tagName == null) {
			switchToElementWindow();
			try {
				tagName = seleniumElement.getTagName().toLowerCase();
			} catch (Exception e) {
				tagName = "";
			}
		}
		return tagName;
	}
		
	/**
	 * Gets visible text of element.
	 * <p>
	 * Containing HTML source will be omitted. To get the containing HTML source too, use {@link #getTextWithSource()} instead.
	 * <p>
	 * <strong>Example:</strong> Given the following HTML source
	 * <pre>{@code
	 * <a href="path/to/index.html">Link <strong>text</strong></a>
	 * }</pre>
	 * <code>getText()</code> returns "Link text".
	 * <p>
	 * @return the visible text of the element
	 */
	@Override
	public String getText() {
		if(text == null) {
			switchToElementWindow();
			try {
				text = seleniumElement.getText();
			} catch (Exception e) {
				text = "";
			}
		}
		return text;
	}
	
	/**
	 * Gets inner text of element.
	 * <p>
	 * Containing HTML source will be returned to. To omit any HTML tags use {@link #getText()} instead.
	 * <p>
	 * <strong>Example:</strong> Given the following HTML source
	 * <pre>{@code
	 * <a href="path/to/index.html">Link <strong>text</strong></a>
	 * }</pre>
	 * <code>getTextWithSource()</code> returns "Link <strong>text</strong>".
	 * <p>
	 * @return the inner text of the element
	 */
	public String getTextWithSource() {
		if(sourceText == null) {
			switchToElementWindow();
			try {
				sourceText = (String) evaluateJavaScript("return jQuery(arguments[0]).html()");
//				System.out.println(sourceText);
			} catch (Exception e) {
				sourceText = "";
//				System.out.println(e);
			}
		}
		return sourceText;
	}
	
	/**
	 * Gets specified line of the inner text of this element.
	 * 
	 * @param lineNumber
	 * @return text line
	 */
	public String getTextLine(int lineNumber) {
		String lines[] = getText().split("\\r?\\n");
		return lines[lineNumber-1];
	}
	
	/**
	 * Gets first line of the inner text of this element.
	 * 
	 * @return first text line
	 */
	public String getTextFirstLine() {
		return getTextLine(1);
	}
	

	/**
	 * Extracts an URL out of this element, which are mostly used in {@code <a>} and {@code <img>} elements.
	 * 
	 * @return String of the extracted URL, null if no URL was found
	 */
	public String getUrl() {
		String url;
		if((url = getAttribute("href")) != null ||
		   (url = getAttribute("src")) != null) {
			return url;
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the value for the specified attribute name.
	 * <p>
	 * <strong>Example:</strong> Given the following HTML source
	 * <pre>{@code
	 * <div id="content" class="container" />
	 * }</pre>
	 * <code>getAttribute("class")</code> returns <em>container</em>
	 * <p>
	 * @param name the attribute name
	 * @return the value of the attribute, null if attribute not exists
	 */
	public String getAttribute(String name) {
//		switchToElementWindow();
//		return seleniumElement.getAttribute(name);
		return attributes.get(name);
	}
	
	/**
	 * Gets value of a CSS attribute.
	 * 
	 * @param cssAttribute the name of the CSS attribute
	 * @return the value of the CSS attribute
	 */
	public String getCssValue(String cssAttribute) {
		return seleniumElement.getCssValue(cssAttribute);
//		String script = "return jQuery(arguments[0]).css('" + cssAttribute + "');";
//		// TODO error handling if result is empty
//		return (String) evaluateJavaScript(script);
	}
	
	// additional element references

//	/**
//	 * Get the table representation of this HTML element. Works only for {@code <table>} elements.
//	 * Throws an exception if element is not a table.
//	 * 
//	 * @return {@link Table} representation
//	 */
//	public Table getTable() {
//		// TODO surrounding table
//		return new Table(this);
//	}
	
	/**
	 * Gets reference elements, which are needed for internal closeness calculations.
	 * 
	 * @return reference elements
	 */
	public HtmlElements getReferenceElements() {
		return referenceElements;
	}
	
	/**
	 * Sets reference elements, which are needed for internal closeness calculations.
	 * 
	 * @param referenceElements
	 */
	public void setReferenceElements(HtmlElements referenceElements) {
		this.referenceElements = referenceElements;
	}
	
	/**
	 * Sets reference element, which is needed for internal closeness calculations.
	 * 
	 * @param referenceElement
	 */
	public void setReferenceElements(HtmlElement referenceElement) {
		this.referenceElements = new HtmlElements(referenceElement);
	}

	// frames and windows
	
	/**
	 * Gets name of window which contains this element.
	 * 
	 * @return window name
	 */
	public String getWindowName() {
		return windowName;
	}

	/**
	 * Sets name of window which contains this element.
	 * 
	 * @param windowName
	 */
	// TODO detect automatically
	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	/**
	 * Gets frame element if this element is located in a frame or iframe.
	 * 
	 * @return frame element
	 */
	public HtmlElement getFrameElement() {
		return frameElement;
	}

	/**
	 * Sets frame element if this element is located in a frame or iframe.
	 * 
	 * @param frameElement
	 */
	// TODO detect automatically
	public void setFrameElement(HtmlElement frameElement) {
		this.frameElement = frameElement;
	}

	/**
	 * Checks if element is located in a frame or iframe.
	 * 
	 * @return true if element is located in frame or iframe
	 */
	public boolean isLocatedInFrame() {
		return frameElement != null;
	}
	
	
	/**
	 * Switches to the window in which this element is located.
	 */
	public void switchToElementWindow() {
		if(isLocatedInFrame()) {
			browser.frame().switchTo(frameElement);
		} else if(windowName instanceof String) {
			browser.window().switchToWindow(windowName);
		} else {
			browser.window().switchToMainWindow();
		}
	}

//	public Object getJsonRepresentation() {
//		return jsonRepresentation;
//	}
	
	/**
	 * Evaluates JavaScript on this element.
	 * 
	 * @param script
	 * @return evaluated return value of JavaScript
	 */
	private Object evaluateJavaScript(String script) {
		switchToElementWindow();
		return browser.javaScript(script, this).getReturnValue();
	}
	
	// TODO extract label/title (input, image)
	
	/**
	 * Extracts date from this element. Currently only the US locale is supported.
	 * <p>
	 * <strong>Example:</strong> given the following HTML element
	 * <pre>{@code
	 * <div>Last updated: 5. May 2012</div>
	 * }</pre>
	 * the following code will extract the date
	 * <pre>{@code
	 * Date lastUpdated = browser.query().has("Last updated").findOne().extractDate();
	 * }</pre>
	 * 
	 * @return Date object
	 * @throws ParseException 
	 */
	public DateTime extractDate() throws ParseException {
		// TODO Use Joda DateTime functions and patterns
		DateTime dateTime = null;
		boolean dateFound = false;
	    
	    String year = null;
	    String month = null;
	    String monthName = null;
	    String day = null;
	    String hour = null;
	    String minute = null;
	    String second = null;
	    String ampm = null;
	    
	    String regexDelimiter = "[-:\\/.,]";
	    String regexDay = "((?:[0-2]?\\d{1})|(?:[3][01]{1}))";
	    String regexMonth = "(?:([0]?[1-9]|[1][012])|(Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Sept|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?))";
	    String regexYear = "((?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))";
	    String regexHourMinuteSecond = "(?:(?:\\s)((?:[0-1][0-9])|(?:[2][0-3])|(?:[0-9])):([0-5][0-9])(?::([0-5][0-9]))?(?:\\s?(am|AM|pm|PM))?)?";
	    String regexEndswith = "(?![\\d])";

	    // DD/MM/YYYY
	    String regexDateEuropean =
	    	regexDay + regexDelimiter + regexMonth + regexDelimiter + regexYear + regexHourMinuteSecond + regexEndswith;

	    // MM/DD/YYYY
	    String regexDateAmerican =
	    	regexMonth + regexDelimiter + regexDay + regexDelimiter + regexYear + regexHourMinuteSecond + regexEndswith;
	    	
	    // YYYY/MM/DD
	    String regexDateTechnical =
	    	regexYear + regexDelimiter + regexMonth + regexDelimiter + regexDay + regexHourMinuteSecond + regexEndswith;
	    
		// the inner text of the element will be checked for dates
	    String text = getText();
	    	
	    // see if there are any matches
	    Matcher m = checkDatePattern(regexDateEuropean, text);
	    if (m.find()) {
	        day = m.group(1);
	        month = m.group(2);
	        monthName = m.group(3);
	        year = m.group(4);
	        hour = m.group(5);
	        minute = m.group(6);
	        second = m.group(7);
	        ampm = m.group(8);
	        dateFound = true;
	    }
	    
	    if(!dateFound) {
		    m = checkDatePattern(regexDateAmerican, text);
		    if (m.find()) {
		        month = m.group(1);
		        monthName = m.group(2);
		        day = m.group(3);
		        year = m.group(4);
		        hour = m.group(5);
		        minute = m.group(6);
		        second = m.group(7);
		        ampm = m.group(8);
		        dateFound = true;
		    }
		}
	    
	    if(!dateFound) {
		    m = checkDatePattern(regexDateTechnical, text);
		    if (m.find()) {
		    	year = m.group(1);
		        month = m.group(2);
		        monthName = m.group(3);
		        day = m.group(4);
		        hour = m.group(5);
		        minute = m.group(6);
		        second = m.group(7);
		        ampm = m.group(8);
		        dateFound = true;
		    }
		}
				
		// construct date object if date was found
		if(dateFound) {
			String dateFormatPattern = "";
			String dayPattern = "";
			String dateString = "";
			
			if(day != null) {
				dayPattern = "d" + (day.length() == 2 ? "d" : "");
			}
			
			if(day != null && month != null && year != null) {
				dateFormatPattern = "yyyy MM " + dayPattern;
				dateString = year + " " + month + " " + day;
			} else if(monthName != null) {
				if(monthName.length() == 3) dateFormatPattern = "yyyy MMM " + dayPattern;
				else dateFormatPattern = "yyyy MMMM " + dayPattern;
				dateString = year + " " + monthName + " " + day;
			}
			
			if(hour != null && minute != null) {
				//TODO ampm
				dateFormatPattern += " hh:mm";
				dateString += " " + hour + ":" + minute;
				if(second != null) {
					dateFormatPattern += ":ss";
					dateString += ":" + second;
				}
			}
			
			if(!dateFormatPattern.equals("") && !dateString.equals("")) {
				//TODO support different locales
				SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern.trim(), Locale.US);
				dateTime = new DateTime(dateFormat.parse(dateString.trim()));
			}
		}
			    
		return dateTime;
	}
	
	private Matcher checkDatePattern(String regex, String text) {
	    Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    return p.matcher(text);
	}
	
	/**
	 * Checks if element is equal to other element
	 * 
	 * @param otherElement {@code HtmlElement} to compare with
	 * @return true if elements are equal
	 */
	public boolean equals(HtmlElement otherElement) {
		return getId().equals(otherElement.getId());
	}
	
	/**
	 * Outputs string representation of element.
	 * 
	 * @see com.abmash.core.element.Element#toString()
	 */
	@Override
	public String toString() {
		String attributeString = "";
		// retrieval of attribute names
		for (String attributeName: attributes.keySet()) {
			attributeString += " " + attributeName + "=\"" + attributes.get(attributeName) + "\"";
		}
		return "<" + getTagName() + attributeString + ">" + getText().replace("\n", " ").substring(0, Math.min(50, getText().length())) + "</" + getTagName() + ">";
	}
	
}