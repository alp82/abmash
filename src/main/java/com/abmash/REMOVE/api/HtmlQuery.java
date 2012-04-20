package com.abmash.REMOVE.api;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebElement;

import com.abmash.REMOVE.core.htmlquery.condition.ClosenessCondition;
import com.abmash.REMOVE.core.htmlquery.condition.ClosenessCondition.Direction;
import com.abmash.REMOVE.core.htmlquery.condition.ColorCondition;
import com.abmash.REMOVE.core.htmlquery.condition.Condition;
import com.abmash.REMOVE.core.htmlquery.condition.Conditions;
import com.abmash.REMOVE.core.htmlquery.condition.ElementCondition;
import com.abmash.REMOVE.core.htmlquery.condition.ElementCondition.ElementType;
import com.abmash.REMOVE.core.htmlquery.condition.SelectorCondition;
import com.abmash.REMOVE.core.htmlquery.condition.SelectorCondition.QueryType;
import com.abmash.REMOVE.core.htmlquery.condition.TagnameCondition;
import com.abmash.REMOVE.core.htmlquery.selector.Selector;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroup;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroup.Type;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroups;
import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.core.color.ColorName;
import com.abmash.core.color.Dominance;
import com.abmash.core.color.Tolerance;
import com.google.common.collect.Table;


/**
 * Find elements by specifying query conditions for the current active web page, used by calling {@link Browser#query()}.
 * <p>
 * <strong>Examples:</strong>
 * <ul>
 * <li><code>browser.query().has("result").findFirst();</code> searches for elements containing the attribute or
 * inner text <em>result</em></li> 
 * <li><code>browser.query().isTitle().findFirst();</code> searches for title elements, i.e. {@code <h1>}, {@code <h2>}, ... {@code <h6>}
 * and any element with a bigger font-size than the default on the current page</li> 
 * <li><code>browser.query().root(myHtmlElement).isClickable().findFirst();</code> searches for all clickable elements  like links
 * and buttons which are child elements of <code>myHtmlElement</code>
 * <li><code>browser.query().below(myHtmlElement).isImage().has("description").findFirst();</code> searches for all image elements 
 * with are below <code>myHtmlElement</code> and have an attribute or inner text <em>description</em>
 * </ul>
 * <p>
 * <strong>Description:</strong>
 * <p>
 * Create a new query instance by calling <code>browser.query()</code>. Conditions can be chained one after another.
 * To get the matching {@link HtmlElements} or {@link HtmlElement}, call {@link #find()} or {@link #findFirst()}.
 * 
 * @author Alper Ortac
 */
public class HtmlQuery {
	
	private Browser browser;
	private HtmlElements resultElements = null;
	private HtmlElements rootElements = new HtmlElements();
	private HtmlElements elementsToFilter = new HtmlElements();
	private Conditions conditions = new Conditions();
	private ArrayList<HtmlQuery> notQueries = new ArrayList<HtmlQuery>();
	private ArrayList<HtmlQuery> orQueries = new ArrayList<HtmlQuery>();
	private ArrayList<String> queryStrings = new ArrayList<String>();
//	private ArrayList<String> textQueries = new ArrayList<String>();
//	private ArrayList<String> attributeQueries = new ArrayList<String>();
//	private ArrayList<String> cssAttributeQueries = new ArrayList<String>();
	
	private int limit = 0;
//	private OrderType orderBy = null;
	
	private ArrayList<ElementType> elementTypes = new ArrayList<ElementType>();
	
	/**
	 * Creates a new query instance for finding {@link HtmlElements} on the current page.
	 * 
	 * @param browser the browser instance to use for finding elements
	 */
	public HtmlQuery(Browser browser) {
		this.browser = browser;
	}
	
	public String toString() {
		String queryString = "HtmlQuery with following conditions:";
		for (Condition condition: conditions) {
			queryString += "\n  [" + condition + "]";
		}
		return queryString;
	}
	
	// filter by reference elements
	
	/**
	 * Defines root elements from which to search on.
	 * <p>
	 * Each element will be searched separately.
	 * Only descendants will be searched whereas ancestors will be ignored.
	 * 
	 * @param rootElements the root elements
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery childOf(HtmlElements rootElements) {
		if(!(rootElements instanceof HtmlElements)) throw new RuntimeException("Error: root elements for query cannot be null.");
		this.rootElements.addAll(rootElements);
		return this;
	}
	
	/**
	 * Defines a root element from which to search on. All other elements in the document are ignored.
	 * <p>
	 * Only descendants will be searched whereas ancestors will be ignored.
	 * 
	 * @param rootElement the root element
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery childOf(HtmlElement rootElement) {
		if(!(rootElement instanceof HtmlElement)) throw new RuntimeException("Error: root element for query cannot be null.");
		this.rootElements.add(rootElement);
		return this;
	}
	
	/**
	 * Defines subset elements which are used as base for filtering the results.
	 * <p>
	 * TODO examples
	 * 
	 * @param elementsToFilter the elements which are used as subset for filtering
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery subsetOf(HtmlElements elementsToFilter) {
		if(!(elementsToFilter instanceof HtmlElements)) throw new RuntimeException("Error: subset elements for query cannot be null.");
		this.elementsToFilter.addAll(elementsToFilter);
		return this;
	}
	
	// NOT and OR subqueries
	
	/**
	 * Defines a "NOT" subquery. The position of the subquery in the condition chain does not matter and is ignored.
	 * {@link #childOf(HtmlElement)} elements are automatically passed to the subquery.
	 * <p>
	 * Note that the {@link #find()} method of the subquery it automatically called. If only one (or any other amount) of the result
	 * elements shall be returned, use {@code #limit(Integer)}.
	 * 
	 * @param notQuery the {@link HtmlQuery} to be combined as NOT condition
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #limit(Integer)
	 */
	public HtmlQuery not(HtmlQuery notQuery) {
		if(!(notQuery instanceof HtmlQuery)) throw new RuntimeException("Error: NOT query cannot be null.");
		notQueries.add(notQuery);
		return this;
	}
	
	/**
	 * Defines an "OR" subquery. The position of the subquery in the condition chain does not matter and is ignored.
	 * {@link #childOf(HtmlElement)} elements are automatically passed to the subquery.
	 * <p>
	 * Note that the {@link #find()} method of the subquery it automatically called. If only one (or any other amount) of the result
	 * elements shall be returned, use {@code #limit(Integer)}.
	 * 
	 * @param orQuery the {@link HtmlQuery} to be combined as OR condition
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #limit(Integer)
	 */
	public HtmlQuery or(HtmlQuery orQuery) {
		if(!(orQuery instanceof HtmlQuery)) throw new RuntimeException("Error: OR query cannot be null.");
		orQueries.add(orQuery);
		return this;
	}
	
	// text and attributes
	
	// TODO
	// queries with EXIST, EQUAL, etc...
	//
	
	/**
	 * Finds elements which contain the specified text or having an attribute value containing the query string.
	 * <p>
	 * Exact matches have more weight than partial matches. In most cases the query is case-insensitive.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().has("Name").findFirst()} finds the first element which contains the text <em>Name</em></li>
	 * <li>{@code browser.query().isInList().has("April").find()} finds all elements in a list which contain the text <em>April</em></li>
	 * </ul>
	 * 
	 * @param text text or attribute value to search for
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery has(String text) {
		queryStrings.add(text);
		return this;
	}
	
	/**
	 * Finds elements which contain all of the specified texts or having attribute values containing all of the query strings.
	 * <p>
	 * Exact matches have more weight than partial matches. In most cases the query is case-insensitive.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isInList().has(["April", "July", "August"]).find()} finds all elements in a list which contain
	 * the texts <em>April</em>, <em>July</em> and <em>August</em></li>
	 * </ul>
	 * 
	 * @param textList text or attribute values to search for
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery has(ArrayList<String> textList) {
		queryStrings.addAll(textList);
		return this;
	}
	
	/**
	 * Finds elements with containing text.
	 * 
	 * @param query
	 * @return this {@link ElementQuery} instance
	 */
//	public ElementQuery containsText(String query) {
//		textQueries.add(query);
//		return this;
//	}
	
	/**
	 * Finds elements with specific attribute values.
	 * 
	 * @param query
	 * @return this {@link ElementQuery} instance
	 */
//	public ElementQuery attribute(String query) {
//		attributeQueries.add(query);
//		return this;
//	}
	
	// element types and closeness
	
	/**
	 * Finds elements by their tag name.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().tag("a").findFirst()} finds the first link element on the current page</li>
	 * <li>{@code browser.query().has("Today").tag("p").find()} finds all paragraph elements which contain the text <em>Today</em></li>
	 * </ul>
	 * 
	 * @param name element tag name
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery tag(String name) {
		conditions.add(new TagnameCondition(name));
		return this;
	}
	
	/**
	 * Finds elements of the specified type.
	 * 
	 * Possible element types are:
	 * 	ALL				all elements on the page
	 * 	TEXT			elements with text in them
	 * 	TITLE			headlines and elements with bigger font size
	 * 	CLICKABLE		clickable elements like links or buttons
	 * 	TYPABLE			input elements which can be used to enter text
	 * 	CHOOSABLE		input elements like drop downs or select lists
	 * 	DATEPICKER		datepicker elements for selecting date and time
	 * 	IMAGE			images
	 * 	LIST			lists
	 * 	TABLE			tables
	 * 	INLIST			items in a list
	 * 	INTABLE			cells in a table
	 * 	FRAME			embedded frames and iframes
	 * 
	 * Example: browser.query().is(ElementType.CLICKABLE).find()
	 *
	 * @param elementType
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	private HtmlQuery is(ElementType elementType) {
		conditions.add(new ElementCondition(browser, elementType));
		elementTypes.add(elementType);
		return this;
	}
	
	/**
	 * Finds text elements, which is the most general search criteria. It prefers text passages,
	 * paragraphs, blocks, list items or table cells, but returns any element if there are no other matches.
	 * <p>
	 * {@code isText()} should usually be used in combination with {@link HtmlQuery#has(String)}.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isText().has("Hello").find()} finds all elements containing the text <em>Hello</em></li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery isText() {
		return is(ElementType.TEXT);
	}

	/**
	 * Finds title elements, which are all elements with headline tags({@code <h1>}, {@code <h2>}, ..., {@code <h6>})
	 * and elements with a bigger font size than the default one.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isTitle().find()} finds all title elements</li>
	 * <li>{@code browser.query().isTitle().isClickable().find()} finds all clickable title elements</li>
	 * <li>{@code browser.query().isTitle().isClickable().has("Article").find()} finds all clickable title elements
	 * containing the text <em>Article</em></li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery isTitle() {
		return is(ElementType.TITLE);
	}
	
	/**
	 * Finds clickable elements like links and buttons.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isClickable().find()} finds all clickable elements like links or buttons,
	 * see also {@link Browser#click(String)}</li>
	 * <li>{@code HtmlElements clickablesInTable = browser.query().isClickable().isInTable().find()} finds all clickable elements in table cells</li>
	 * <li>{@code ArrayList<String> urls = clickablesInTable.getUrls()} extracts all URLs from the clickable items, see also
	 * {@link HtmlElement#getUrl()} and {@link HtmlElements#getUrls()}.</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see Browser#click(String)
	 */
	public HtmlQuery isClickable() {
		return is(ElementType.CLICKABLE);
	}

	/**
	 * Finds typable elements like input fields, text areas or rich-text-editors like tinyMCE, which can be used
	 * to enter text.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isTypable().find()} finds all typable elements which can be used
	 * to enter text, see also {@link Browser#type(String, String)}</li>
	 * <li>{@code HtmlElement input = browser.query().isTypable().has("address").findFirst()} finds text input labeled <em>address</em></li>
	 * <li>{@code input.type("Miller Street 123").submit()} enters the text <em>Miller Street 123</em> in the text field and submits the form, see
	 * also {@link HtmlElement#type(String)}, {@link HtmlElement#submit()} is optional.</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see Browser#type(String, String)
	 */
	public HtmlQuery isTypable() {
		return is(ElementType.TYPABLE);
	}

	/**
	 * Finds choosable elements, which are usually drop-down boxes or input boxes with multiple selectable items.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isChoosable().find()} finds all choosable elements, see also {@link Browser#choose(String, String)}</li>
	 * <li>{@code HtmlElement language = browser.query().isChoosable().has("Language").findFirst()} finds choosable labeled <em>Language</em></li>
	 * <li>{@code language.select("English").submit()} selects the language item labeled <em>English</em> and submit the form, see
	 * also {@link HtmlElement#choose(String)}, {@link HtmlElement#submit()} is optional.</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery isChoosable() {
		return is(ElementType.CHOOSABLE);
	}

	/**
	 * Finds date picker elements, which are usually drop-down boxes or calendar widgets.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>TODO</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery isDatepicker() {
		return is(ElementType.DATEPICKER);
	}
	
	/**
	 * Finds image elements. Images defined as background image can not be found properly at the moment.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isImage().find()} finds all image elements on the current page</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	// TODO image size
	public HtmlQuery isImage() {
		return is(ElementType.IMAGE);
	}
	
	/**
	 * Finds list elements, which are the root elements of lists, usually {@code <ul>}, {@code <ol>} or {@code <dl>} elements.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code HtmlElement list = browser.query().isList().has("Country").findFirst()} finds first list element labeled or
	 * containing the text <em>Country</em>, see also {@link Browser#getList(String)}</li>
	 * <li>{@code List countryList = browser.getList(list)} returns the list representation of that element, see also
	 * {@link Browser#getList(HtmlElement)}</li>
	 * <li>{@code list.get(0)} gets the first item of that list, see also {@link List#get(int)}</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see Browser#getList(HtmlElement)
	 * @see Browser#getList(String)
	 * @see List
	 */
	public HtmlQuery isList() {
		return is(ElementType.LIST);
	}
	
	/**
	 * Finds elements in lists, which are usually {@code <li>} or {@code <dt>} elements.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isInList().has("Name").find()} finds all elements in a list,
	 * which are labeled <em>Name</em></li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see Browser#getList(HtmlElement)
	 * @see Browser#getList(String)
	 * @see List
	 */
	public HtmlQuery isInList() {
		return is(ElementType.INLIST);
	}

	
	/**
	 * Finds table elements, which are the root elements of tables, usually {@code <table>} elements.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code HtmlElement table = browser.query().isTable().has("Version").findFirst()} finds first table element labeled
	 * or containing the text <em>Version</em>, see also {@link Browser#getTable(String)}</li>
	 * <li>{@code Table countryTable = browser.getTable(table)} returns the table representation of that element, see also
	 * {@link Browser#getTable(HtmlElement)}</li>
	 * <li>{@code table.getCell(0,0)} gets the cell in the upper left corner of that table, see also {@link Table#getCell(int, int)}</li>
	 * <li>see {@link Table} for more examples how to handle Table representations</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see Browser#getTable(HtmlElement)
	 * @see Browser#getTable(String)
	 * @see Table
	 */
	public HtmlQuery isTable() {
		return is(ElementType.TABLE);
	}
	
	/**
	 * Finds elements in tables, which are usually {@code <td>} or {@code <th>} elements.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code browser.query().isInTable().has("$").find()} finds all elements in a table,
	 * which contain the character <em>$</em></li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see Browser#getTable(HtmlElement)
	 * @see Browser#getTable(String)
	 * @see Table
	 */
	public HtmlQuery isInTable() {
		return is(ElementType.INTABLE);
	}
	
	/**
	 * Finds frame elements, which are parts of the page with separated content, usually {@code <frame>} and {@code <iframe>} elements.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li>{@code HtmlElement frame = browser.query().isFrame().findFirst()} finds the first frame on the page, see also {@link Browser#frame()} for switching directly (set focus) to a frame</li>
	 * <li>{@code browser.frame().switchTo(frame)} sets focus on the selected frame. all browser interactions will take place in this frame from now on</li>
	 * </ul>
	 * 
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see Browser#frame()
	 */
	public HtmlQuery isFrame() {
		return is(ElementType.FRAME);
	}
	
	/**
	 * Finds elements visually close to the given reference elements.
	 * <p>
	 * The Direction determines if found elements will be filtered out in case they are visually at another location.
	 * The found elements will be automatically ordered by closeness to the referenceElements.
	 * The distance is calculated by the euclidean measure. 
	 * 
	 * @param referenceElements
	 * @param direction see {@link Direction} for a description of all possible values
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery closestTo(HtmlElements referenceElements, Direction direction) {
		conditions.add(new ClosenessCondition(referenceElements, direction));
		return this;
	}
	
	/**
	 * Finds elements visually close to the given reference element with the specified label.
	 * <p>
	 * The found elements will be automatically ordered by closeness to the referenceElements. 
	 * The distance is calculated by the euclidean measure. 
	 * 
	 * @param query the visible label or hidden attribute of the element
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery closestTo(String query) {
		return null;
//		return closestTo(new HtmlElements(browser.query().has(query).findFirst()), Direction.CLOSE);
	}
	
	/**
	 * Finds elements visually close to the given reference element.
	 * <p>
	 * The found elements will be automatically ordered by closeness to the referenceElements. 
	 * The distance is calculated by the euclidean measure. 
	 * 
	 * @param referenceElement elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery closestTo(HtmlElement referenceElement) {
		return closestTo(new HtmlElements(referenceElement), Direction.CLOSE);
	}
	
	/**
	 * Finds elements visually close to the given reference elements. 
	 * <p>
	 * The found elements will be automatically ordered by closeness to the referenceElements. 
	 * The distance is calculated by the euclidean measure. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery closestToAny(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.CLOSE);
	}
	
	/**
	 * Finds elements <strong>above</strong> the element with the specified label.
	 * <p>
	 * Elements are above if their bottom y coordinate is lower than the top y coordinate of the reference element.
	 * In addition, the element has to be in horizontal bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *              ----------
	 *              - <span> -
	 *              ----------
	 *        ---------
	 *        - <div> -
	 *        ---------
	 *   --------
	 *   - <ul> -
	 *   --------
	 * }</pre>
	 * In this example, the {@code <div>} is above the {@code <ul>}, but the {@code <span>} is not because its left border is not
	 * in horizontal bounds of {@code <ul>}. Then again, the {@code <span>} is above the {@code <div>}. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param query the visible label or hidden attribute of the element
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery above(String query) {
		return null;
//		return closestTo(new HtmlElements(browser.query().has(query).findFirst()), Direction.ABOVE);
	}
	
	/**
	 * Finds elements <strong>above</strong> the reference element.
	 * <p>
	 * Elements are above if their bottom y coordinate is lower than the top y coordinate of the reference element.
	 * In addition, the element has to be in horizontal bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *              ----------
	 *              - <span> -
	 *              ----------
	 *        ---------
	 *        - <div> -
	 *        ---------
	 *   --------
	 *   - <ul> -
	 *   --------
	 * }</pre>
	 * In this example, the {@code <div>} is above the {@code <ul>}, but the {@code <span>} is not because its left border is not
	 * in horizontal bounds of {@code <ul>}. Then again, the {@code <span>} is above the {@code <div>}. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param referenceElement elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery above(HtmlElement referenceElement) {
		return closestTo(new HtmlElements(referenceElement), Direction.ABOVE);
	}
	
	/**
	 * Finds elements visually <strong>above any</strong> of the given reference elements.
	 * <p>
	 * Elements are above if their bottom y coordinate is lower than the top y coordinate of any reference element.
	 * In addition, the element has to be in horizontal bounds of any reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *             ----------
	 *             - <span> -
	 *             ----------
	 *        ---------   ---------
	 *        - <div> -   - <div> -
	 *        ---------   ---------
	 *   --------            --------
	 *   - <ul> -            - <ul> -
	 *   --------            --------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s are above the {@code <ul>}s,
	 * but the {@code <span>} is not because its left and right border is not in horizontal bounds of any of the {@code <ul>}s.
	 * Then again, the {@code <span>} is above from both of the {@code <div>}s, if they are the reference elements. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #above(HtmlElement)
	 */
	public HtmlQuery aboveAny(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.ABOVE);
	}

	
	/**
	 * Finds elements visually <strong>above all</strong> of the given reference elements.
	 * <p>
	 * Elements are above if their bottom y coordinate is lower than the top y coordinate of any reference element.
	 * In addition, the element has to be in horizontal bounds of any reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *             ----------
	 *             - <span> -
	 *             ----------
	 *        ---------   ---------
	 *        - <div> -   - <div> -
	 *        ---------   ---------
	 *   --------            --------
	 *   - <ul> -            - <ul> -
	 *   --------            --------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s and also the {@code <span>} are not
	 * above them, because they are not above from at least one of the {@code <ul>}s. But if the {@code <div>}s are the reference
	 * elements, the {@code <span>} is above both of them. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #above(HtmlElement)
	 */
	public HtmlQuery aboveAll(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.ABOVE_ALL);
	}
	
	/**
	 * Finds elements <strong>below</strong> the element with the specified label.
	 * <p>
	 * Elements are below if their top y coordinate is higher than the bottom y coordinate of the reference element.
	 * In addition, the element has to be in horizontal bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------
	 *   - <ul> -
	 *   --------
	 *        ---------
	 *        - <div> -
	 *        ---------
	 *              ----------
	 *              - <span> -
	 *              ----------
	 * }</pre>
	 * In this example, the {@code <div>} is below the {@code <ul>}, but the {@code <span>} is not because its left border is not
	 * in horizontal bounds of {@code <ul>}. Then again, the {@code <span>} is below the {@code <div>}. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param query the visible label or hidden attribute of the element
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery below(String query) {
		return null;
//		return closestTo(new HtmlElements(browser.query().has(query).findFirst()), Direction.BELOW);
	}
	
	/**
	 * Finds elements <strong>below</strong> the reference element.
	 * <p>
	 * Elements are below if their top y coordinate is higher than the bottom y coordinate of the reference element.
	 * In addition, the element has to be in horizontal bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------
	 *   - <ul> -
	 *   --------
	 *        ---------
	 *        - <div> -
	 *        ---------
	 *              ----------
	 *              - <span> -
	 *              ----------
	 * }</pre>
	 * In this example, the {@code <div>} is below the {@code <ul>}, but the {@code <span>} is not because its left border is not
	 * in horizontal bounds of {@code <ul>}. Then again, the {@code <span>} is below the {@code <div>}. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param referenceElement elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery below(HtmlElement referenceElement) {
		return closestTo(new HtmlElements(referenceElement), Direction.BELOW);
	}
	
	/**
	 * Finds elements visually <strong>below any</strong> of the given reference elements.
	 * <p>
	 * Elements are below if their top y coordinate is higher than the bottom y coordinate of the reference element.
	 * In addition, the element has to be in horizontal bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------            --------
	 *   - <ul> -            - <ul> -
	 *   --------            --------
	 *        ---------   ---------
	 *        - <div> -   - <div> -
	 *        ---------   ---------
	 *             ----------
	 *             - <span> -
	 *             ----------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s are below the {@code <ul>}s,
	 * but the {@code <span>} is not because its left and right border is not in horizontal bounds of any of the {@code <ul>}s.
	 * Then again, the {@code <span>} is below from both of the {@code <div>}s, if they are the reference elements. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #below(HtmlElement)
	 */
	public HtmlQuery belowAny(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.BELOW);
	}

	
	/**
	 * Finds elements visually <strong>below all</strong> of the given reference elements.
	 * <p>
	 * Elements are below if their top y coordinate is higher than the bottom y coordinate of the reference element.
	 * In addition, the element has to be in horizontal bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------            --------
	 *   - <ul> -            - <ul> -
	 *   --------            --------
	 *        ---------   ---------
	 *        - <div> -   - <div> -
	 *        ---------   ---------
	 *             ----------
	 *             - <span> -
	 *             ----------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s and also the {@code <span>} are not
	 * above them, because they are not above from at least one of the {@code <ul>}s. But if the {@code <div>}s are the reference
	 * elements, the {@code <span>} is above both of them. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #below(HtmlElement)
	 */
	public HtmlQuery belowAll(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.BELOW_ALL);
	}

	/**
	 * Finds elements <strong>left to</strong> the element with the specified label.
	 * <p>
	 * Elements are left if their right x coordinate is lower than the left x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                    ----------
	 *   --------         - <span> -
	 *   - <ul> -         ----------
	 *   --------   ---------
	 *              - <div> -
	 *              ---------
	 * }</pre>
	 * In this example, the {@code <ul>} is left to the {@code <span>} and to the {@code <div>}, but the {@code <div>} is
	 * not left to the {@code <span>}, because its top border is not in vertical bounds of {@code <span>}. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param query the visible label or hidden attribute of the element
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery leftTo(String query) {
		return null;
//		return closestTo(new HtmlElements(browser.query().has(query).findFirst()), Direction.LEFT);
	}
	
	/**
	 * Finds elements <strong>left to</strong> the reference element.
	 * <p>
	 * Elements are left if their right x coordinate is lower than the left x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                    ----------
	 *   --------         - <span> -
	 *   - <ul> -         ----------
	 *   --------   ---------
	 *              - <div> -
	 *              ---------
	 * }</pre>
	 * In this example, the {@code <ul>} is left to the {@code <span>} and to the {@code <div>}, but the {@code <div>} is
	 * not left to the {@code <span>}, because its top border is not in vertical bounds of {@code <span>}. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param referenceElement elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery leftTo(HtmlElement referenceElement) {
		return closestTo(new HtmlElements(referenceElement), Direction.LEFT);
	}
	
	/**
	 * Finds elements visually <strong>left to any</strong> of the given reference elements.
	 * <p>
	 * Elements are left if their right x coordinate is lower than the left x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .                          
	 *                            --------
	 *                            - <ul> -
	 *                ---------   --------
	 *                - <div> -
	 *    ----------  ---------
	 *    - <span> -
	 *    ---------- ----------
	 *                - <div> -
	 *                ---------   --------
	 *                            - <ul> -
	 *                            --------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s are left to the {@code <ul>}s,
	 * but the {@code <span>} is not because its top and bottom border is not in vertical bounds of any of the {@code <ul>}s.
	 * Then again, the {@code <span>} is left to both of the {@code <div>}s, if they are the reference elements. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #leftTo(HtmlElement)
	 */
	public HtmlQuery leftToAny(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.LEFT);
	}

	
	/**
	 * Finds elements visually <strong>left to all</strong> of the given reference elements.
	 * <p>
	 * Elements are left if their right x coordinate is lower than the left x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                            --------
	 *                            - <ul> -
	 *                ---------   --------
	 *                - <div> -
	 *    ----------  ---------
	 *    - <span> -
	 *    ---------- ----------
	 *                - <div> -
	 *                ---------   --------
	 *                            - <ul> -
	 *                            --------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s and also the {@code <span>} are not
	 * left to them, because they are not left to at least one of the {@code <ul>}s. But if the {@code <div>}s are the reference
	 * elements, the {@code <span>} is left to both of them. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #leftTo(HtmlElement)
	 */
	public HtmlQuery leftToAll(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.LEFT_ALL);
	}

	/**
	 * Finds elements <strong>right to</strong> the element with the specified label.
	 * <p>
	 * Elements are right if their left x coordinate is higher than the right x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                       ----------
	 *   --------            - <span> -
	 *   - <ul> -            ----------
	 *   --------  ---------
	 *             - <div> -
	 *             ---------
	 * }</pre>
	 * In this example, the {@code <span>} and the {@code <div>} are right to the {@code <ul>}, but the {@code <span>}
	 * is not right to the {@code <div>} because its bottom border is not in vertical bounds of the reference element. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param query the visible label or hidden attribute of the element
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery rightTo(String query) {
		return null;
//		return closestTo(new HtmlElements(browser.query().has(query).findFirst()), Direction.RIGHT);
	}
	
	/**
	 * Finds elements <strong>right to</strong> the reference element.
	 * <p>
	 * Elements are right if their left x coordinate is higher than the right x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                       ----------
	 *   --------            - <span> -
	 *   - <ul> -            ----------
	 *   --------  ---------
	 *             - <div> -
	 *             ---------
	 * }</pre>
	 * In this example, the {@code <span>} and the {@code <div>} are right to the {@code <ul>}, but the {@code <span>}
	 * is not right to the {@code <div>} because its bottom border is not in vertical bounds of the reference element. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param referenceElement elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery rightTo(HtmlElement referenceElement) {
		return closestTo(new HtmlElements(referenceElement), Direction.RIGHT);
	}
	
	/**
	 * Finds elements visually <strong>right to any</strong> of the given reference elements.
	 * <p>
	 * Elements are right if their left x coordinate is higher than the right x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------
	 *   - <ul> -
	 *   --------   ---------
	 *              - <div> -
	 *              ---------  ----------
	 *                         - <span> -
	 *              ---------  ----------
	 *              - <div> -
	 *   --------   ---------
	 *   - <ul> -
	 *   --------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s are right to the {@code <ul>}s,
	 * but the {@code <span>} is not because its top and bottom borders are not in vertical bounds of any of the {@code <ul>}s.
	 * Then again, the {@code <span>} is right to both of the {@code <div>}s, if they are the reference elements. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #rightTo(HtmlElement)
	 */
	public HtmlQuery rightToAny(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.RIGHT);
	}

	
	/**
	 * Finds elements visually <strong>right to all</strong> of the given reference elements.
	 * <p>
	 * Elements are right if their left x coordinate is higher than the right x coordinate of the reference element.
	 * In addition, the element has to be in vertical bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------
	 *   - <ul> -
	 *   --------   ---------
	 *              - <div> -
	 *              ---------  ----------
	 *                         - <span> -
	 *              ---------  ----------
	 *              - <div> -
	 *   --------   ---------
	 *   - <ul> -
	 *   --------
	 * }</pre>
	 * If the {@code <ul>}s are the reference elements in this example, the {@code <div>}s and also the {@code <span>} are not
	 * right to them, because they are not right to at least one of the {@code <ul>}s. But if the {@code <div>}s are the reference
	 * elements, the {@code <span>} is right to both of them. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * any of the reference elements. 
	 * 
	 * @param referenceElements elements which are taken as reference for measuring the distance
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 * @see #rightTo(HtmlElement)
	 */
	public HtmlQuery rightToAll(HtmlElements referenceElements) {
		return closestTo(referenceElements, Direction.RIGHT_ALL);
	}
	
	// color selectors
	
	/**
	 * Checks if image is covered by the specified color, considering the given tolerance.
	 * 
	 * @see HtmlQuery#isColor(Color, double)
	 * @return true if image is covered by the color
	 */
	public HtmlQuery isColor(String color, Tolerance tolerance) {
		ColorName colorName = null;
		try {
			// try to find the right color name
			colorName = ColorName.valueOf(color.toUpperCase());
		} catch (Exception e) {
			return this;
		}
		
		return isColor(colorName.getColor(), tolerance.getValue());
	}
	
	/**
	 * Checks if image is covered by the specified color, considering the given tolerance.
	 * 
	 * @see HtmlQuery#isColor(Color, double)
	 * @return true if image is covered by the color
	 */
	public HtmlQuery isColor(Color color, Tolerance tolerance) {
		return isColor(color, tolerance.getValue());
	}

	/**
	 * Checks if image is covered by the specified color, considering the given tolerance.
	 * 
	 * @param color the color value to check
	 * @param tolerance the lower the value, the lower the tolerance regarding the color
	 * distance. <code>1</code> returns always true, <code>0</code> is only true if the
	 * image is completely filled with that exact color
	 * @return true if image is covered by the color
	 */
	public HtmlQuery isColor(Color color, double tolerance) {
		conditions.add(new ColorCondition(color, tolerance));
		return this;
	}
	
	/**
	 * Checks if image contains the specified color, considering the given dominance and tolerance.
	 * 
	 * @see HtmlQuery#hasColor(Color, double, double)
	 * @return true if image is covered by the color
	 */
	public HtmlQuery hasColor(String color, double tolerance, double dominance) {
		ColorName colorName = null;
		try {
			// try to find the right color name
			colorName = ColorName.valueOf(color.toUpperCase());
		} catch (Exception e) {
			return this;
		}
		
		return hasColor(colorName.getColor(), tolerance, dominance);
	}

	/**
	 * Checks if image contains the specified color, considering the given dominance and tolerance.
	 * 
	 * @see HtmlQuery#hasColor(Color, double, double)
	 * @return true if image is covered by the color
	 */
	public HtmlQuery hasColor(Color color, Tolerance tolerance, double dominance) {
		return hasColor(color, tolerance.getValue(), dominance);
	}
	
	/**
	 * Checks if image contains the specified color, considering the given dominance and tolerance.
	 * 
	 * @see HtmlQuery#hasColor(Color, double, double)
	 * @return true if image is covered by the color
	 */
	public HtmlQuery hasColor(Color color, double tolerance, Dominance dominance) {
		return hasColor(color, tolerance, dominance.getValue());
	}

	/**
	 * Checks if image contains the specified color, considering the given dominance and tolerance.
	 * 
	 * @see HtmlQuery#hasColor(Color, double, double)
	 * @return true if image is covered by the color
	 */
	public HtmlQuery hasColor(Color color, Tolerance tolerance, Dominance dominance) {
		return hasColor(color, tolerance.getValue(), dominance.getValue());
	}
	
	/**
	 * Checks if image contains the specified color, considering the given dominance and tolerance.
	 * 
	 * @param color the color value to check, see also {@link ColorName#getColor()}
	 * @param dominance the lower the value, the lower the dominance of the color in the
	 * image within the tolerance parameter. <code>0</code> is always passed through,
	 * <code>1</code> is only true if the image has exclusively colors within the tolerance
	 * range
	 * @param tolerance the lower the value, the lower the tolerance regarding the color
	 * distance. <code>1</code> is always passed through, <code>0</code> is only true if the
	 * image has enough dominant pixels with that exact color
	 * @return true if image contains the color
	 */	
	public HtmlQuery hasColor(Color color, double tolerance, double dominance) {
		conditions.add(new ColorCondition(color, tolerance, dominance));
		return this;
	}

	// custom selectors

	/**
	 * Uses CSS selector to find elements.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * cssSelector("div#content > p.info > strong[name=author]")
	 * </pre>
	 * 
	 * @param query the CSS selector expression
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery cssSelector(String query) {
		conditions.add(new SelectorCondition(query, QueryType.CSS));
		return this;
	}
	
	/**
	 * Uses Xpath selector to find elements.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>
	 * xPathSelector("//div[id=content]/p[class=info]/strong[name=author]")
	 * </pre>
	 * 
	 * @param query the xPath selector expression
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery xPathSelector(String query) {
		conditions.add(new SelectorCondition(query, QueryType.XPATH));
		return this;
	}
	
	/**
	 * Uses JQuery selector to find elements.
	 * <p>
	 * <strong>Example:</strong>
	 * <pre>
	 * jQuerySelector("find(div button:only-child)")
	 * </pre>
	 * 
	 * @param query the jQuery selector expression
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery jQuerySelector(String query) {
		conditions.add(new SelectorCondition(query, QueryType.JQUERY));
		return this;
	}

	// post processing of results
	
	/**
	 * Limits the result set to the specified amount.
	 * 
	 * @param count maximum number of results, no limit if null
	 * @return this {@link HtmlQuery} instance, which can be used to add more search criteria like this or to finally
	 * execute the query with {@link #find()} or {@link #findFirst()}
	 */
	public HtmlQuery limit(Integer count) {
		limit = count;
		return this;
	}

	/**
	 * Orders the result set by the specified order type.
	 * 
	 * @param orderType
	 * @return this {@link ElementQuery} instance
	 */
//	public Finder orderBy(OrderType orderType) {
//		orderBy = orderType;
//		return this;
//	}
	
	// find logic
	
	/**
	 * Finds best matching element which match the given search conditions. Waits for the element to appear.
	 * <p>
	 * Returns {@code null} if no element was found after the waiting time.
	 * 
	 * @return the {@link HtmlElement} result
	 */
	public HtmlElement findFirstWithWait() {
		limit = 1;
		resultElements = findWithWait();
		return resultElements instanceof HtmlElements && !resultElements.isEmpty() ? resultElements.first() : null;
	}
	
	/**
	 * Finds best matching element which match the given search conditions.
	 * <p>
	 * Returns {@code null} if no element was found.
	 * 
	 * @return the {@link HtmlElement} result
	 */
	public HtmlElement findFirst() {
		limit = 1;
		resultElements = find();
		return resultElements instanceof HtmlElements && !resultElements.isEmpty() ? resultElements.first() : null;
	}
	
	/**
	 * Finds all elements which match the given search conditions. Waits for the element to appear.
	 * <p>
	 * Returns {@code null} if no element was found after the waiting time.
	 * 
	 * @return the {@link HtmlElements} result set
	 */
	public HtmlElements findWithWait() {
		doFind();
		if(resultElements == null || resultElements.isEmpty()) {
//			try {
//				browser.waitFor().query(this);
				doFind();
//			} catch (TimeoutException e) {
				// element not found
//				browser.log().info("Query: element not found for query " + this.toString());
//			}
		}
		return resultElements;
	}

	/**
	 * Finds all elements which match the given search conditions.
	 * <p>
	 * Returns {@code null} if no element was found.
	 * 
	 * @return the {@link HtmlElements} result set
	 */
	public HtmlElements find() {
		return doFind();
	}
	
	private HtmlElements doFind() {
		resultElements = null;
			
		// if no condition is set, add the "all elements" matcher
		if(elementsToFilter.isEmpty() && (conditions.isEmpty() || !conditions.hasElementFinder())) {
			conditions.add(new ElementCondition(browser, ElementType.ALL));
		}
			
		Map<String, HtmlElements> referenceElements = new HashMap<String, HtmlElements>(); 
		Map<String, HtmlElements> labelElements = new HashMap<String, HtmlElements>(); 
		
		JSONArray jsonConditions = new JSONArray();
		JSONArray jsonClosenessConditions = new JSONArray();
		JSONArray jsonColorConditions = new JSONArray();
		boolean hasColorConditions = false;
		
		// building queries, which will be executed as javascript/jquery commands
		try {
			for(Condition condition: conditions) {
				// TODO remove isElementFinder
				if(condition.isElementFinder()) {
					if(condition instanceof ElementCondition) {
						if(!queryStrings.isEmpty()) {
							// TODO queries for frame is "frame name"
							((ElementCondition) condition).addQueries(queryStrings);
						}
					}
					
					JSONObject jsonCondition = new JSONObject();
					JSONArray jsonSelectorGroups = new JSONArray();
					JSONArray jsonLabelSelectorGroups = new JSONArray();
					JSONArray jsonFallbackSelectorGroups = new JSONArray();
					SelectorGroups selectorGroups = condition.getSelectorGroups();
					
					for(SelectorGroup selectorGroup: selectorGroups) {
						Type selectorGroupType = selectorGroup.getType(); 
						JSONObject jsonSelectorGroup = new JSONObject();
						JSONArray jsonSelectors = new JSONArray();
						for(Selector selector: selectorGroup) {
							JSONObject jsonSelector = new JSONObject();
							jsonSelector.put("weight", selector.getWeight());
							jsonSelector.put("command", selector.getExpressionAsJQueryCommand());
							jsonSelectors.put(jsonSelector);
						}
						if(selectorGroup.hasReferenceElements()) {
							String referenceId = Long.toString(System.currentTimeMillis()) + Double.toString(Math.random());
							jsonSelectorGroup.put("referenceId", referenceId);
							referenceElements.put(referenceId, selectorGroup.getReferenceElements());
						}
						jsonSelectorGroup.put("weight", selectorGroup.getWeight());
						jsonSelectorGroup.put("limit", selectorGroup.getLimit());
						jsonSelectorGroup.put("selectors", jsonSelectors);
						
						if(selectorGroupType.equals(Type.NORMAL)) {
							jsonSelectorGroups.put(jsonSelectorGroup);
						} else if(selectorGroupType.equals(Type.LABEL)) {
							jsonLabelSelectorGroups.put(jsonSelectorGroup);
						} else if(selectorGroupType.equals(Type.FALLBACK)) {
							jsonFallbackSelectorGroups.put(jsonSelectorGroup);
						}
					}
					
					if(selectorGroups.hasLabelElements()) {
						String labelId = Long.toString(System.currentTimeMillis()) + Double.toString(Math.random());
						jsonCondition.put("labelId", labelId);
						jsonCondition.put("labelType", selectorGroups.getLabelType().toString().toLowerCase());
						labelElements.put(labelId, selectorGroups.getLabelElements());
					}
					
					if(condition instanceof ElementCondition) {
						jsonCondition.put("elementType", ((ElementCondition) condition).getElementType().toString().toLowerCase());
					}
					jsonCondition.put("type", condition.getType());
					jsonCondition.put("selectorGroups", jsonSelectorGroups);
					jsonCondition.put("labelSelectorGroups", jsonLabelSelectorGroups);
					jsonCondition.put("fallbackSelectorGroups", jsonFallbackSelectorGroups);
					
					if(condition instanceof ClosenessCondition) {
						jsonCondition.put("isClosenessCondition", "true");
						jsonClosenessConditions.put(jsonCondition);
					} else if(condition instanceof ColorCondition) {
						hasColorConditions = true;
						jsonCondition.put("isColorCondition", "true");
						jsonColorConditions.put(jsonCondition);
					} else {
						jsonConditions.put(jsonCondition);
					}
				}
			}
//			System.out.println(jsonConditions.toString(2));
//			System.out.println(jsonClosenessConditions.toString(2));
//			System.out.println(jsonColorConditions.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// send screenshot if color queries requested
		if(hasColorConditions) {
			try {
				byte[] pageAsPNGByteArray = ((TakesScreenshot) browser.getWebDriver()).getScreenshotAs(OutputType.BYTES);
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(pageAsPNGByteArray));
				
				// encode image as base64 string
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, "png", baos);
				baos.flush();
				pageAsPNGByteArray = baos.toByteArray();
				baos.close();
				
				// TODO find way to take only one screenshot
//				String pageAsBase64PNG = "data:image/png;base64," + Base64.encodeBase64URLSafeString(pageAsPNGByteArray);
				String pageAsBase64PNG = "data:image/png;base64," + ((TakesScreenshot) browser.getWebDriver()).getScreenshotAs(OutputType.BASE64);
				
				// TODO example base64 png
//				pageAsBase64PNG = "data:image/png;base64," + "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";

				if(pageAsBase64PNG.length() >= 500000) {
					for (int i = 0; i < pageAsBase64PNG.length(); i += 500000) {
						browser.javaScript("abmash.buildImageDataForPageScreenshot(arguments[0])", pageAsBase64PNG.substring(i, Math.min(i + 499999, pageAsBase64PNG.length())));
					}
					browser.javaScript("abmash.updatePageScreenshot(arguments[0], arguments[1])", image.getWidth(), image.getHeight());
				} else {
					browser.javaScript("abmash.buildImageDataForPageScreenshot(arguments[0]); abmash.updatePageScreenshot(arguments[1], arguments[2])", pageAsBase64PNG, image.getWidth(), image.getHeight());
				}
				
//				browser.javaScript("abmash.updatePageScreenshot(arguments[0], arguments[1], arguments[2])", pageAsBase64PNG, image.getWidth(), image.getHeight());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!elementsToFilter.isEmpty()) {
			System.out.println(elementsToFilter);
		}
		
		// sending queries to jquery executor
//		String queryId = Long.toString(System.currentTimeMillis()) + Double.toString(Math.random());
		String script = "return abmash.query(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5], arguments[6], arguments[7]);";
		@SuppressWarnings("unchecked")
		ArrayList<RemoteWebElement> seleniumElements = (ArrayList<RemoteWebElement>) browser.javaScript(
				script,
//				queryId,
				jsonConditions.toString(),
				jsonClosenessConditions.toString(),
				jsonColorConditions.toString(),
				rootElements,
				elementsToFilter,
				referenceElements,
				labelElements,
				limit
		).getReturnValue();
		
		// converting selenium elements to abmash elements
		HtmlElements tempElements = new HtmlElements();
		for(RemoteWebElement seleniumElement: seleniumElements) {
			HtmlElement element = new HtmlElement(browser, seleniumElement);
			// if filter elements are used, check if the elements match
			if(elementsToFilter.isEmpty() || elementsToFilter.contains(element)) {
				tempElements.add(new HtmlElement(browser, seleniumElement));
			}
		}
//		System.out.println("tempElements: " + tempElements + " -- " + conditions);
		
		// or queries
		for(HtmlQuery orQuery: orQueries) {
			orQuery.childOf(rootElements);
			HtmlElements orElements = orQuery.find();
			for(HtmlElement orElement: orElements) {
				// if filter elements are used, check if the elements match
				if(elementsToFilter.isEmpty() || elementsToFilter.contains(orElement)) {
					tempElements.add(orElement);
				}
			}
		}
		
		// not queries
		for(HtmlQuery notQuery: notQueries) {
			// process not query if result is not empty already
			if(!tempElements.isEmpty()) {
				notQuery.childOf(rootElements);
				HtmlElements notElements = notQuery.find();
				
				HtmlElements subQueryElements = new HtmlElements();
				for (HtmlElement tempElement: tempElements) {
					// add only elements which are not returned by last not query
					if(!notElements.contains(tempElement)) {
						subQueryElements.add(tempElement);
					}
				}
				
				// set temp elements to narrowed down results
				tempElements = subQueryElements;
			}
		}
		
		// filter out invalid matches
		HtmlElements unsortedElements = new HtmlElements();
		for (HtmlElement tempElement: tempElements) {
			if(conditions.elementValid(tempElement)) {
				tempElement.setWindowName(browser.window().getCurrentWindowName());
				unsortedElements.add(tempElement);
			}
		}
//		System.out.println("unsortedElements: " + unsortedElements + " -- " + conditions);
		
		// fetch and store data for elements
		// this needs to be done before sorting because of better performance
//		unsortedElements.fetchDataForCache();
		
		if(!elementTypes.isEmpty()) {
			// set element types if specified by query
			unsortedElements.setTypes(elementTypes);
		}
		
		// sort results
		// TODO sorting completely in jquery
		// TODO no sorting of OR'ed queries atm
		HtmlElements sortedElements = unsortedElements; 
		for (Condition condition: conditions) {
			// TODO what to do if there are several and/or different sorts?
			if(condition instanceof ClosenessCondition) {
				sortedElements = condition.sortElements(unsortedElements);
				break;
			}
		}
		
		// limit results
		HtmlElements limitedElements = new HtmlElements();
		for (HtmlElement sortedElement: sortedElements) {
			if(limit == 0 || limitedElements.size() < limit) {
				sortedElement.setWindowName(browser.window().getCurrentWindowName());
				// eliminate duplicates
				limitedElements.addAndIgnoreDuplicates(sortedElement);
			}
		}
		
		// finalize results
		resultElements = limitedElements;
		
//		System.out.println(resultElements + " -- " + conditions);
		return resultElements;
	}

//	public HtmlElements findOld() {
//		if(!(resultElements instanceof HtmlElements)) {
//			// find elements
//			browser.log().debug("Query: Searching...");
//			HtmlElements unsortedElements = doFind();
//			
////			System.out.println("elements after ANDs: " + unsortedElements);
//
//			// sort result
//			HtmlElements sortedElements = sortElements(unsortedElements);
//
////			System.out.println("elements after filtering and sorting: " + sortedElements);
//
//			if(unsortedElements.size() > 0) browser.log().debug("Query: Result found" +
//				(sortedElements.size() > 1 ? " and sorted" : ""));
//			
//			// check OR queries to add them to the result set
//			for (HtmlQuery orQuery: orQueries) {
//				for (HtmlElement orElement: orQuery.find()) {
//					// do not add the element if it is contained in the elements that need to be filtered out
//					if(!notElements.contains(orElement)) sortedElements.addAndIgnoreDuplicates(orElement);
//				}
//			}
//			
////			System.out.println("elements after ORs: " + unsortedElements);
//			
//			// limit result
//			HtmlElements limitedElements = new HtmlElements();
//			if(limit > 0 && sortedElements.size() > limit) {
//				for (int i = 0; i < limit; i++) {
//					limitedElements.add(sortedElements.get(i));
//				}
//			} else {
//				limitedElements = sortedElements;
//			}
//			resultElements = limitedElements;
//		}
//		
//		// log if result is empty
//		if(!(resultElements instanceof HtmlElements) || resultElements.isEmpty()) {
//			browser.log().debug("Query: No element found for " + this);
//			resultElements = null;
//		} else {
//			resultElements.fetchDataForCache();
//		}
//		
//		return resultElements;
//	}
//
//
//	/**
//	 * Searches for all matching elements
//	 * 
//	 * @return
//	 */
//	private HtmlElements doFind() {
//		HtmlElements matchingElements = new HtmlElements();
//		
//		// if query specified without element type, tag or selector, search for any element
//		boolean selectAnyElement = true;
//		for (Condition condition: conditions) {
//			// TODO tag() or select() with has() does not work properly
//			if(queryStrings.isEmpty()) {
//				if(condition instanceof ElementCondition || condition instanceof TagnameCondition || condition instanceof SelectorCondition) {
//					selectAnyElement = false;
//					break;
//				}
//			} else {
//				if(condition instanceof ElementCondition) {
//					selectAnyElement = false;
//					break;
//				}
//			}
//		}
//		
//		// if no condition specified search for any text element
//		if(selectAnyElement || conditions.isEmpty()) {
//			conditions.add(new ElementCondition(browser, ElementType.ALL));
//		}
//		
//		// if no query specified search for anything
//		if(queryStrings.isEmpty()) queryStrings.add("");
//		
//		// TODO when fallback conditions needed, use them one after another, or for all (inclusively the already processed conditions)
//		for (Condition condition: conditions) {
//			if(condition.isElementFinder()) {
//				HtmlElements matchingElementsForCondition = new HtmlElements();
//				
//				// check all regular selectors
//				for (String query: queryStrings) {
//					// TODO queries for frame is framename
//					if(condition instanceof ElementCondition) {
//						((ElementCondition) condition).addQuery(query);
//					}
//					
//					// get selectors
//					SelectorGroups selectors = condition.getSelectorGroups();
//
//					// get matching elements
//					HtmlElements matchingElementsForConditionQuery = matchingElementsForConditionQuery(condition, selectors);
//					matchingElementsForCondition.addAll(matchingElementsForConditionQuery);
//					matchingElements = matchingElements(matchingElements, matchingElementsForConditionQuery);
////					System.out.println("Checking condition: " + condition);
////					System.out.println(" with selectors: " + selectors);
////					System.out.println("All elements only for condition: " + matchingElementsForConditionQuery);
////					System.out.println("All ANDed elements after condition: " + matchingElements);
//					
//					// skip to fallback selectors if result is already empty
//					if(matchingElements.isEmpty()) break;
//				}
//			}
////			browser.log().debug("Elements for " + condition + ": {}", matchingElements);
//		}
//		
//		// set element types if specified by query
//		if(!elementTypes.isEmpty()) {
//			matchingElements.setTypes(elementTypes);
//		}
//		
//		
//		// return matching elements or empty list
//		return matchingElements;
//	}
//	
//	private HtmlElements matchingElementsForConditionQuery(Condition condition, SelectorGroups selectorGroups) {
//		// selectors are processed OR-wise
//		HtmlElements matchingElementsForConditionQuery = new HtmlElements();
//		for (SelectorGroup selectorGroup: selectorGroups) {
//			// process each group
//			HtmlElements groupElements = new HtmlElements();
//			for (Selector selector: selectorGroup) {
//				// if elements found, do not use fallback selectors
//				// TODO ensure correct order of non-fallback and fallback selectors
//				if(matchingElementsForConditionQuery.size() > 0 && selectorGroup.getType() == Type.FALLBACK) break;
//				
//				HtmlElements selectorElements = selectAndFilterElements(selector, condition);
//				groupElements.addAllAndIgnoreDuplicates(selectorElements); // selector matches
//				
//				// continue with next group if enough elements are matching already
//				if(groupElements.size() >= selectorGroup.getLimit()) break;
//			}
//			matchingElementsForConditionQuery.addAllAndIgnoreDuplicates(groupElements); // selector matches
//		}
//		
////		System.out.println("Elements for condition: " + matchingElementsForConditionQuery);
//		return matchingElementsForConditionQuery;
//	}
//		
//	private HtmlElements matchingElements(HtmlElements matchingElements, HtmlElements matchingElementsForConditionQuery) {
//		if(matchingElements.isEmpty()) {
//			// first condition result is taken as reference
//			matchingElements = matchingElementsForConditionQuery;
//		} else {
//			// next results are compared AND-wise to last result
//			HtmlElements temporaryElements = new HtmlElements();
//			for (HtmlElement matchingElementForCondition: matchingElementsForConditionQuery) {
//				// throw away elements which are not returned in at least one condition
//				// also ignore elements that need to be filtered out
//				if(matchingElements.contains(matchingElementForCondition)) {
//					temporaryElements.add(matchingElementForCondition);
//				}
//			}
//			matchingElements = temporaryElements;
//		}
//		return matchingElements;
//	}
//	
//	private HtmlElements selectAndFilterElements(Selector selector, Condition condition) {
//		HtmlElements foundElements = new HtmlElements();
//		
//		HtmlElements conditionElements = new HtmlElements();
//		if(!rootElements.isEmpty()) {
//			for (HtmlElement rootElement: rootElements) {
//				try {
//					conditionElements.addAll(selector.find(browser, rootElement));
//				} catch (Exception e) {
//					System.err.println("Find error: skipped selector [" + selector + "] for condition [" + condition + "] on root element [" + rootElement + "]: " + e.getMessage());
////					e.printStackTrace();
////					System.exit(0);
//				}
//			}
//		} else {
//			try {
//				conditionElements.addAll(selector.find(browser));
//			} catch (RuntimeException e) {
//				System.err.println("Find error: skipped selector [" + selector + "] for condition [" + condition + "]: " + e.getMessage());
////				e.printStackTrace();
////				System.exit(0);
//			}
//		}
//		
//		if(!conditionElements.isEmpty()) {
////			System.out.println(" ### FOUND ELEMENTS: " + conditionElements.size() + " for " + selector + " in " + condition);
//			for (HtmlElement foundElement: conditionElements) {
//				// TODO set label of typables
//				// TODO make sure that the attribute is set in foundElement which is finally returned after filtering out duplicates
////					if(condition instanceof ElementCondition &&  ((ElementCondition) condition).getElementType() == ElementType.TYPABLE) foundElement.setLabel();
//				if(condition.elementValid(foundElement) && !notElements.contains(foundElement)) {
//					foundElement.setWindowName(browser.window().getCurrentWindowName());
//					foundElements.add(foundElement);
//				}
//			}
//		}
//		
//		return foundElements;
//	}
//	
//	private HtmlElements sortElements(HtmlElements unsortedElements) {
//		// if unsorted elements are empty fill it with all elements if no other conditions were specified
//		boolean closenessWithAllElements = false;
//		if(unsortedElements.isEmpty()) {
//			closenessWithAllElements = true;
//			for (Condition condition: conditions) {
//				if(!(condition instanceof ClosenessCondition)) closenessWithAllElements = false;
//			}
//		}
//		
//		HtmlElements sortedElements = unsortedElements;
//		for (Condition condition: conditions) {
////			System.out.println("unsorted elements: " + sortedElements + " (condition: " + condition + ")");
//			if(condition instanceof ClosenessCondition) {
//				if(closenessWithAllElements && sortedElements.isEmpty()) {
//					sortedElements = browser.query().cssSelector("*").find();
//				}
//				
//				// TODO what to do if there are several and/or different sorts?
//				sortedElements = condition.sortElements(sortedElements);
//			}
////			System.out.println("sorted elements: " + sortedElements + " (condition: " + condition + ")");
//		}
//		return sortedElements;
//	}


}
