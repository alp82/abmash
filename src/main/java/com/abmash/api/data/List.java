package com.abmash.api.data;
//package com.abmash.api.tabular;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import com.abmash.api.Browser;
//import com.abmash.api.HtmlElement;
//import com.abmash.api.HtmlElements;
//
///**
// * Represents a list on a webpage, use it by calling {@link Browser#getList(String)} or {@link Browser#getList(HtmlElement)}.
// * <p>
// * <strong>Example:</strong>
// * <ul>
// * <li><code>HtmlElement listTitle = browser.query().isTitle().has("Ingredients").findFirst();</code> Returns the first element being a title labeled <em>Ingredients</em></li>
// * <li><code>HtmlElement list = browser.query().isList().below(tableTitle).findFirst();</code> returns the first list below the previously selected title element</li>
// * <li><code>List ingredientList = browser.getList(table);</code> finally returns the list representation.</li> 
// * <li><code>ingredientList.get(0);</code> returns the first list item.</li> 
// * <li><code>ingredientList.getText(1);</code> returns the visible text of the second list item.</li> 
// * </ul>
// * <p>
// * To get a specific item, use {@link #get(int)}. To get the visible text of a specific item, use {@link #getText(int)}. This class is iterable through all list item {@link HtmlElement}s.
// * <p>
// * Lists are basically vectors with n items. Spoken in HTML source code:
// * <pre>{@code
// * <ul>
// *   <li>item 1</td>
// *   <li>item 2</td>
// * </ul>
// * }</pre>
// * <p>
// * Nested lists will be returned as visible text, there is no support for nested list representations yet.
// * 
// * @author Alper Ortac
// */
//public class List implements Iterable<HtmlElement> {
//	
//	private Browser browser;
//	private HtmlElement listElement;
//	
//	private ArrayList<HtmlElement> items = new ArrayList<HtmlElement>();
//	
//	/**
//	 * Creates a list representation of the specified list element in the current page, use it by calling
//	 * {@link Browser#getList(HtmlElement)}.
//	 * 
//	 * @param listElement has to be an {@link HtmlElement} with the tag {@code <ul>}, {@code <ol>} or {@code <dl>}
//	 */
//	public List(HtmlElement listElement) {
//		if(!listElement.getTagName().equals("ul") ||
//			!listElement.getTagName().equals("ol") ||
//			!listElement.getTagName().equals("dl")) {
//			throw new RuntimeException("Element is not a list: " + listElement);
//		}
//		this.browser = listElement.getBrowser();
//		this.listElement = listElement;
//		readItems();
//	}
//	
//	/**
//	 * Creates a list representation of the first list element found which contains the query text, use it by calling
//	 * {@link Browser#getList(String)}.
//	 * 
//	 * @param browser browser instance which contains the list
//	 * @param query text which is contained in the list (visible text or attribute value)
//	 */
//	public List(Browser browser, String query) {
//		this(browser.query().isList().has(query).findFirst());
//	}
//	
//	private void readItems() {
//		// TODO dd of dt elements
//		// TODO multiple dt with 1+ dd
//		HtmlElements itemElements = listElement.query().xPathSelector(".//li|.//dt").find();
//		items = itemElements;
//	}
//	
//	/**
//	 * Iterates through all {@link HtmlElement}s of this list.
//	 * 
//	 * @see java.lang.Iterable#iterator()
//	 */
//	public Iterator<HtmlElement> iterator() {
//		return items.iterator();
//	}
//	
//	/**
//	 * Returns the specified list item.
//	 * <p>
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table productList = browser.getTable("Product")</code> returns the first list which contains
//	 * the text <em>Product</em></li>
//	 * <li><code>productList.get(1)</code> returns the second list item</li>
//	 * </ul>
//	 * 
//	 * @param itemIndex the number of the item to return, starting at 0 - throws an exception if out of bounds
//	 * @return {@link HtmlElement} of the specified list item
//	 */
//	public HtmlElement get(int itemIndex) {
//		if(itemIndex < 0 || itemIndex >= items.size()) {
//			throw new RuntimeException("List item number '" + itemIndex + "' is out of bounds, number of items is '" + items.size() +  "'");
//		}
//		
//		return items.get(itemIndex);
//	}
//	
//	/**
//	 * Returns the visible text of the specified list item.
//	 * <p>
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table productList = browser.getTable("Product")</code> returns the first list which contains
//	 * the text <em>Product</em></li>
//	 * <li><code>productList.getText(1)</code> returns the visible text of the second list item</li>
//	 * </ul>
//	 * 
//	 * @param itemIndex the number of the item to return, starting at 0 - throws an exception if out of bounds
//	 * @return visible text of the specified list item
//	 */
//	public String getText(int itemIndex) {
//		return get(itemIndex).getText();
//	}
//	
//	/**
//	 * Returns the number of items in this list.
//	 * 
//	 * @return row count
//	 */
//	public int getCount() {
//		return items.size();
//	}
//	
//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString() {
//		return items.toString();
//	}
//
//}
