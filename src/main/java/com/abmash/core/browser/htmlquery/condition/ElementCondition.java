package com.abmash.core.browser.htmlquery.condition;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.core.browser.htmlquery.selector.CssSelector;
import com.abmash.core.browser.htmlquery.selector.DirectMatchSelector;
import com.abmash.core.browser.htmlquery.selector.JQuerySelector;
import com.abmash.core.browser.htmlquery.selector.Selector;
import com.abmash.core.browser.htmlquery.selector.SelectorGroup;
import com.abmash.core.browser.htmlquery.selector.SelectorGroup.Type;
import com.abmash.core.browser.htmlquery.selector.TagnameSelector;
import com.abmash.core.browser.htmlquery.selector.XpathSelector;


public class ElementCondition extends Condition {
	
	/**
	 * Element type for this condition
	 */
	public enum ElementType {
		ALL,
		TEXT,
		TITLE,
		CLICKABLE,
		TYPABLE,
		CHOOSABLE,
		IMAGE,
		LIST,
		TABLE, 
		INLIST,
		INTABLE, 
		FRAME,
	}
	
	
	private Browser browser;
	private ElementType elementType;
	private String textQuery;
	private String attributeQuery;
	
	// constructors
	
	public ElementCondition(Browser browser, ElementType elementType) {
		this.browser = browser;
		this.elementType = elementType;
	}
	
	public void setQuery(String query) {
		this.textQuery = query;
		this.attributeQuery = query;
	}
	
	// condition
	
	@Override
	protected void buildSelectors() {
		switch (elementType) {
		case ALL:
			selectorsForAll();
			break;
		case TEXT:
			selectorsForText();
			break;
		case TITLE:
			selectorsForTitles();
			break;
		case CLICKABLE:
			selectorsForClickables();
			break;
		case TYPABLE:
			selectorsForTypables();
			break;
		case CHOOSABLE:
			selectorsForChoosables();
			break;
		case IMAGE:
			selectorsForImages();
			break;
		case LIST:
			selectorsForLists();
			break;
		case TABLE:
			selectorsForTables();
			break;
		case INLIST:
			selectorsInLists();
			break;
		case INTABLE:
			selectorsInTables();
			break;
		case FRAME:
			selectorsForFrames();
			break;
		}
	}
	
	private void selectorsForAll() {
		List<String> elementNames = Arrays.asList("*");
		List<String> attributeNames = Arrays.asList("*");
		
		// find target by inner text and html attributes which match exactly or partially
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS), "input"));
		selectors.add(checkElementAttributes("*", attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS), "input"));

		// find target by case insensitive attribute matches
		if(attributeQuery != null && !attributeQuery.equals("") && !attributeQuery.contains("[") && !attributeQuery.contains("]") && !attributeQuery.contains("(") && !attributeQuery.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + attributeQuery + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + attributeQuery + ")')"));
			selectors.add(group);
		}	
		
		// if nothing was found just return all elements
		// but only if no text query was given
		if(textQuery != null && !textQuery.equals("")) {
			selectors.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK, 1));
		}
	}
	
	private void selectorsForText() {
		List<String> elementNames = Arrays.asList("strong", "em", "label", "span", "p", "div", "li", "td", "th", "*");
		List<String> attributeNames = Arrays.asList("for", "id", "class", "name", "value", "title", "alt", "*");
		
		// find target by inner text and html attributes which match exactly or partially
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS), "input"));
		selectors.add(checkElementAttributes("*", attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS), "input"));

		// find target by case insensitive attribute matches
		if(attributeQuery != null && !attributeQuery.equals("") && !attributeQuery.contains("[") && !attributeQuery.contains("]") && !attributeQuery.contains("(") && !attributeQuery.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + attributeQuery + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + attributeQuery + ")')"));
			selectors.add(group);
		}	
		
		// if nothing was found just return all elements
		// but only if no text query was given
		if(textQuery != null && !textQuery.equals("")) {
			selectors.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK, 1));
		}
	}
	
	private void selectorsForTitles() {
		List<String> elementNames = Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6");
		List<String> attributeNames = Arrays.asList("for", "id", "class", "name", "value", "type", "title", "alt", "*");
		
		// find target by inner text which match exactly or partially
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));

		// find elements with font-size bigger than the default
		HtmlElement body = browser.query().tag("body").findFirst();
		selectors.add(new SelectorGroup(checkElementCssAttribute("*", body.getCssValue("font-size"), "font-size", CSSAttributeMatcher.GREATER_THAN)));

		// find target by html attributes which match exactly or partially
		selectors.add(checkElementAttributes("*", attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));
		
		// find target by tag name selector 
		// TODO which query?
		if(textQuery != null && !textQuery.equals("")) {
			selectors.add(new SelectorGroup(new TagnameSelector(textQuery)));
		}
		
		// find target by case insensitive attribute matches
		if(attributeQuery != null && !attributeQuery.equals("") && !attributeQuery.contains("[") && !attributeQuery.contains("]") && !attributeQuery.contains("(") && !attributeQuery.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + attributeQuery + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + attributeQuery + ")')"));
			selectors.add(group);
		}
		
		// if nothing was found just return all headline elements
		selectors.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
	}
	
	private void selectorsForClickables() {
		// TODO range sliders: "input[type=range]"
		// TODO date pickers: "input[type=date]"
		// TODO color pickers: "input[type=color]"
		
		List<String> elementNames = Arrays.asList("a");
		List<String> inputNames = Arrays.asList("input[type='checkbox']", "input[type='radio']", "input[type='submit']", "input[type='button']", "input[type='image']", "button");
		List<String> attributeNames = Arrays.asList("id", "value", "name", "class", "title", "alt", "href", "*");

		// find checkboxes, radioboxes and buttons
		selectors.add(checkElementAttributes(inputNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));

		// find checkboxes and radioboxes by searching for closest fitting label
//		if(textQuery != null && !textQuery.equals("")) {
//			// TODO find with rootelements
//			try {
//				HtmlElements labelElements = browser.query().xPathSelector("//*[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]").find();
//				if(!labelElements.isEmpty()) {
//					// has(null) needed so that no loops can occur
//					HtmlElement clickableInput = browser.query().has(null).cssSelector("input[type='checkbox'],input[type='radio']").isClickable().closeToAny(labelElements).findFirst();
//					if(clickableInput instanceof HtmlElement) {
//						selectors.add(new SelectorGroup(new DirectMatchSelector(new HtmlElements(clickableInput))));
//					}
//				}
//			} catch (Exception e) {
//				// if no element was found, just continue with next selectors
//			}
//		}

		// find any element by inner text
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		
		// find target by html attributes
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// TODO elements with parent <a> tags are also clickable 
		if(textQuery != null && !textQuery.equals("")) {
			// TODO has-parent instead of following-sibling
//			selectors.add(new SelectorGroup(new XpathSelector("//a[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/following-sibling::input")));
		} else {
			// TODO has-parent without text
		}
		
		// find target by case insensitive attribute matches
		if(attributeQuery != null && !attributeQuery.equals("") && !attributeQuery.contains("[") && !attributeQuery.contains("]") && !attributeQuery.contains("(") && !attributeQuery.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + attributeQuery + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + attributeQuery + ")')"));
			selectors.add(group);
		}		
		
		// if there were no results just find all clickables
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			group.add(new CssSelector(StringUtils.join(inputNames, ',')));
			selectors.add(group);
		}
	}
	
	private void selectorsForTypables() {
		// TODO without check and radioboxes
		List<String> elementNames = Arrays.asList(
				"input[type=password]", "input[type=text]", "input[type=email]", "input[type=url]",
				"input[type=number]", "input[type=search]", "textarea");
		List<String> attributeNames = Arrays.asList("id", "name", "value", "class", "type", "title", "*");

		// find target by html attributes which match exactly
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, AttributeMatcher.EXACT));

		// find tinyMce editor iframes
		// TODO move to FindClosest 
		Selector tinymceSelector = new CssSelector(".mceIframeContainer iframe[id*='" + attributeQuery + "']");
		try {
			// TODO find with rootelements if needed
			if(tinymceSelector.find(browser).isEmpty()) {
				tinymceSelector = new JQuerySelector("find('.mceIframeContainer iframe:attrCaseInsensitive(CONTAINS, id, " + attributeQuery + ")')");
			}
			HtmlElements tinymceElements = tinymceSelector.find(browser);
			if(tinymceElements != null && !tinymceElements.isEmpty()) {
				HtmlElement frame = tinymceElements.first();
				browser.frame().switchTo(frame);
				// TODO find with rootelements if needed
				HtmlElement textarea = browser.query().cssSelector("#tinymce").findFirst();
				if(textarea instanceof HtmlElement) {
					textarea.setFrameElement(frame);
					selectors.add(new SelectorGroup(new DirectMatchSelector(new HtmlElements(textarea))));
				}
				// TODO switch to previously focused window
				browser.window().switchToMain();
			}
		} catch (Exception e) {
			// if no element was found, just continue with next selectors
		}
		
		// TODO find inputs with fitting labels by "id" and "for"
		
		// find inputs by searching for closest fitting label
		// TODO find with rootelements
		// TODO PERFORMANCE PROBLEMS
//		if(textQuery != null && !textQuery.equals("")) {
//			try {
//				HtmlElements labelElements = browser.query().isText().has(textQuery).find();
//				if(!labelElements.isEmpty()) {
//					// has(null) needed so that no loops can occur
//					HtmlElement typableInput = browser.query().has(null).cssSelector(StringUtils.join(elementNames, ",")).closeTo(labelElements, Direction.INPUT).findFirst();
//					if(typableInput instanceof HtmlElement) {
//						selectors.add(new SelectorGroup(new DirectMatchSelector(new HtmlElements(typableInput))));
//					}
//				}
//			} catch (Exception e) {
//				// if no element was found, just continue with next selectors
//			}
//		}
		
		// find target by html attributes which match partially
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));

		// find labels and other elements containing the query and return the next descendant input element
		// TODO with textarea too
		if(textQuery != null && !textQuery.equals("")) {
			selectors.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/following-sibling::input")));
			selectors.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/following-sibling::textarea")));
//			selectors.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::div/descendant::input")));
//			selectors.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::ul/descendant::input")));
//			selectors.add(new SelectorGroup(new XpathSelector("//*[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::div/descendant::input")));
//			selectors.add(new SelectorGroup(new XpathSelector("//*[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::ul/descendant::input")));
		}
		
		// find target by case insensitive attribute matches
		if(attributeQuery != null && !attributeQuery.equals("") && !attributeQuery.contains("[") && !attributeQuery.contains("]") && !attributeQuery.contains("(") && !attributeQuery.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + attributeQuery + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + attributeQuery + ")')"));
			selectors.add(group);
		}
		
		// if there were no results just find all inputs
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}
	
	private void selectorsForChoosables() {
		List<String> elementNames = Arrays.asList("select");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "value", "*");

		// find target by text and html attributes
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// find target by option labels
		selectors.add(new SelectorGroup(new JQuerySelector("select:has(option:contains('" + textQuery + "'))")));

		// if there were no results just find all selects
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}
	
	private void selectorsForImages() {
		List<String> elementNames = Arrays.asList("img");
		List<String> attributeNames = Arrays.asList("id", "title", "alt", "class", "name", "*");

		// find target by html attributes
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// CSS background images
		// TODO with attributes
		// TODO as fallback (without attributes)
//		selectors.add(new SelectorGroup(checkElementCssAttribute("*", "url(", "background-image", CSSAttributeMatcher.CONTAINS)));
		
		// if there were no results just find all images
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}
	
	private void selectorsForLists() {
		List<String> elementNames = Arrays.asList("ul,ol,dl");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "*");

		// find target by text and html attributes
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// if there were no results just find all lists
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}
	
	private void selectorsForTables() {
		List<String> elementNames = Arrays.asList("table");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "*");

		// find target by text and html attributes
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// if there were no results just find all tables
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}

	private void selectorsInLists() {
		// TODO dl besteht aus dt und dd (term/descriptions)
		List<String> elementNames = Arrays.asList("li,dt");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "value", "title", "alt", "for", "*");
		
		// find target by inner text and html attributes which match exactly or partially
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));
		
		// find target by case insensitive attribute matches
		if(attributeQuery != null && !attributeQuery.equals("") && !attributeQuery.contains("[") && !attributeQuery.contains("]") && !attributeQuery.contains("(") && !attributeQuery.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + attributeQuery + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + attributeQuery + ")')"));
			selectors.add(group);
		}
		
		// if there were no results just find list items
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}

	private void selectorsInTables() {
		List<String> elementNames = Arrays.asList("td,th");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "value", "title", "alt", "for", "*");
		
		// find target by inner text and html attributes which match exactly or partially
		selectors.add(checkElementText(elementNames, textQuery, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));
		
		// find target by case insensitive attribute matches
		if(attributeQuery != null && !attributeQuery.equals("") && !attributeQuery.contains("[") && !attributeQuery.contains("]") && !attributeQuery.contains("(") && !attributeQuery.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + attributeQuery + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + attributeQuery + ")')"));
			selectors.add(group);
		}
		
		// if there were no results just find all table cells
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}
	
	private void selectorsForFrames() {
		List<String> elementNames = Arrays.asList("frame", "iframe");
		List<String> attributeNames = Arrays.asList("id", "name", "class", "title", "alt", "*");

		// find target by html attributes
		selectors.add(checkElementAttributes(elementNames, attributeQuery, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// if there were no results just find all frames
		// but only if no text query was specified
		if(textQuery == null || textQuery.equals("")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK);
			group.add(new CssSelector(StringUtils.join(elementNames, ',')));
			selectors.add(group);
		}
	}

	public boolean elementValid(HtmlElement foundElement) {
		if(!super.elementValid(foundElement)) return false;
		
		if(elementType == ElementType.TITLE && foundElement.getText().isEmpty()) return false;
			
		return true;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public String toString() {
		return super.toString() + " with type \"" + elementType + "\"" +
			(textQuery instanceof String && !textQuery.equals("") ? " and text query \"" + textQuery + "\"" : "") +
			(attributeQuery instanceof String && !attributeQuery.equals("") ? " and attribute query \"" + attributeQuery + "\"" : "");
	}
	
}
