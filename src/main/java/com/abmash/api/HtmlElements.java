package com.abmash.api;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.abmash.REMOVE.api.HtmlQuery;
import com.abmash.REMOVE.core.htmlquery.condition.ElementCondition.ElementType;
import com.abmash.api.query.Query;
import com.abmash.api.query.QueryFactory;


/**
 * List of {@link HtmlElement} instances.
 * <p> 
 * Find <code>HtmlElements</code> by using the {@link Browser#query(com.abmash.core.query.predicate.Predicate...)} method.
 * The {@link HtmlQuery#find()} method returns <code>HtmlElements</code>. See {@link HtmlQuery} how to find elements.
 * <p>
 * <strong>Example:</strong>
 * <ul>
 * <li><code>HtmlElement myElement = browser.query().isTitle().isClickable().has("today").findFirst();</code> directly searches
 * for clickable titles labeled <em>today</em> and returns the <code>HtmlElement</code></li>
 * <li>See the documentation of {@link HtmlQuery} for more examples</li>
 * </ul>
 * <p>
 * Bulk interactions on all contained elements can be executed by calling the appropriate methods, e.g. {@link #type(String)}
 * or {@link #clear()}.
 * 
 * @author Alper Ortac
 * @see HtmlElement
 */
public class HtmlElements extends ArrayList<HtmlElement> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs new empty list.
	 */
	public HtmlElements() {
		super();
	}
	
	/**
	 * Constructs new list with one element.
	 * 
	 * @param element the first element in the list
	 * @see HtmlElement
	 */
	public HtmlElements(HtmlElement element) {
		super();
		add(element);
	}
	
	/**
	 /**
	 * Constructs new list with multiple Selenium {@code WebElement} instances.
	 * 
	 * @param browser browser instance in which these elements are contained
	 * @param webElements list of Selenium {@link WebElement}
	 * @see HtmlElement
	 */
	public HtmlElements(Browser browser, List<WebElement> webElements) {
		super();
		if(webElements != null && !webElements.isEmpty()) {
			for (WebElement webElement: webElements) {
				RemoteWebElement renderedElement = (RemoteWebElement) webElement;
				// check if element can be interacted with 
				if(renderedElement.isEnabled() && (renderedElement.isDisplayed() || renderedElement.getTagName() == "input" || renderedElement.getTagName() == "textarea")) {
					// TODO ignore duplicates?
					add(new HtmlElement(browser, renderedElement));
				}
			}
		}
	}

	// element methods
	
	/**
	 * Clicks all elements.
	 * <p>
	 * Warning: if clicking an element causes the page to reload, the other clicks will fail and throw an error.
	 * 
	 * @return this {@link HtmlElements}
	 * @see HtmlElement#click()
	 */
	public HtmlElements click() {
		for (HtmlElement element: this) {
			element.click();
		}
		return this;
	}

	/**
	 * Hovers all elements with the mouse.
	 * 
	 * @return this {@link HtmlElements}
	 * @see HtmlElement#hover()
	 */
	public HtmlElements hover() {
		for (HtmlElement element: this) {
			element.hover();
		}
		return this;
	}
	
	/**
	 * Drags all elements to a target element.
	 *
	 * @param targetElement the element to drag the elements to
	 * @return this {@link HtmlElements}
	 * @see HtmlElement#click()
	 */
	public HtmlElements dragTo(HtmlElement targetElement) {
		for (HtmlElement element: this) {
			element.dragTo(targetElement);
		}
		return this;
	}
	
	/**
	 * Clears the entered text in all elements.
	 * 
	 * @see HtmlElement#clear()
	 */
	public void clear() {
		// TODO return type this
		for (HtmlElement element: this) {
			element.clear();
		}
//		return this;
	}

	/**
	 * Enters text in all elements.
	 * 
	 * @param text
	 * @return this {@link HtmlElements}
	 * @see HtmlElement#type(String)
	 */
	public HtmlElements type(String text) {
		for (HtmlElement element: this) {
			element.type(text);
		}
		return this;
	}
	
	/**
	 * Presses key on all elements.
	 * 
	 * @param keyName
	 * @return this {@link HtmlElements}
	 * @see HtmlElement#keyPress(String)
	 */
	public HtmlElements keyPress(String keyName) {
		for (HtmlElement element: this) {
			element.keyPress(keyName);
		}
		return this;
	}
	
	/**
	 * Submits all elements.
	 * 
	 * @return this {@link HtmlElements}
	 * @see HtmlElement#submit()
	 */
	public HtmlElements submit() {
		for (HtmlElement element: this) {
			element.submit();
		}
		return this;
	}

	/**
	 * Gets all inner texts of the elements.
	 * 
	 * @return array of all text strings
	 * @see HtmlElement#getText()
	 */
	public ArrayList<String> getTexts() {
		ArrayList<String> texts = new ArrayList<String>();
		for (HtmlElement element: this) {
			texts.add(element.getText());
		}
		return texts;
	}
	
	/**
	 * Gets all extracted URLs of the elements.
	 * 
	 * @return array of all extracted URLs
	 * @see HtmlElement#getText()
	 */
	public ArrayList<String> getUrls() {
		ArrayList<String> urls = new ArrayList<String>();
		for (HtmlElement element: this) {
			String url = element.getUrl();
			if(url != null) urls.add(url);
		}
		return urls;
	}
	
	// custom methods
	
	/**
	 * Sets the type of all elements.
	 * 
	 * @param types
	 * @see HtmlElement#setTypes(ArrayList)
	 */
	public void setTypes(ArrayList<ElementType> types) {
		for (HtmlElement element: this) {
			element.setTypes(types);
		}
	}
	
	// list methods
	
	/**
	 * Adds an {@link HtmlElement} to the list. If you want to ignore duplicates use {@link #addAndIgnoreDuplicates(HtmlElement)}.
	 * 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(HtmlElement element) {
		return super.add(element);
	}
	
	/**
	 * Adds an {@link HtmlElement} if it not exists in the list yet.
	 * 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean addAndIgnoreDuplicates(HtmlElement element) {
		if(!contains(element)) {
			return super.add(element);
		}
		return true;
	}
	
	/**
	 * Adds all {@link HtmlElements} to the list. If you want to ignore duplicates use {@link #addAllAndIgnoreDuplicates(Collection)}.
	 * 
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends HtmlElement> elements) {
		boolean addValid = true;
		for (HtmlElement element: elements) {
			addValid &= add(element);
		}
		return addValid;
	}
	
	/**
	 * Adds all {@link HtmlElements} not existing in the list yet.
	 * 
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	public boolean addAllAndIgnoreDuplicates(Collection<? extends HtmlElement> elements) {
		boolean addValid = true;
		for (HtmlElement element: elements) {
			addValid &= addAndIgnoreDuplicates(element);
		}
		return addValid;
	}
	
	/**
	 * Checks if the list contains a HtmlElement.
	 * 
	 * @param element
	 * @return true if list contains the element
	 */
	public boolean contains(HtmlElement element) {
		for (HtmlElement htmlElement: this) {
			if(htmlElement.equals(element)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the nth item of the list.
	 * 
	 * @param index number of the item, first item has index one
	 * @return the HtmlElement
	 */
	public HtmlElement nth(int index) {
		return index > 0 && index <= size() ? get(index - 1) : null;
	}
	
	/**
	 * Gets the first item of the list.
	 * 
	 * @return the HtmlElement
	 */
	public HtmlElement first() {
		return nth(1);
	}
	
	/**
	 * Gets the second item of the list.
	 * 
	 * @return the HtmlElement
	 */
	public HtmlElement second() {
		return nth(2);
	}
	
	/**
	 * Gets the third item of the list.
	 * 
	 * @return the HtmlElement
	 */
	public HtmlElement third() {
		return nth(3);
	}
	
	/**
	 * Gets the last item of the list.
	 * 
	 * @return the HtmlElement
	 */
	public HtmlElement last() {
		return nth(size());
	}
	
	public String toString() {
		String elementsString = "[\n";
		for (HtmlElement element: this) {
			elementsString += "\t" + element.toString() + "\n";
		}
		return elementsString + "]";
	}
	
}
