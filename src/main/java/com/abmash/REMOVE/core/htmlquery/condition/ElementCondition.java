package com.abmash.REMOVE.core.htmlquery.condition;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.REMOVE.core.htmlquery.condition.ClosenessCondition.Direction;
import com.abmash.REMOVE.core.htmlquery.selector.CssSelector;
import com.abmash.REMOVE.core.htmlquery.selector.DirectMatchSelector;
import com.abmash.REMOVE.core.htmlquery.selector.JQuerySelector;
import com.abmash.REMOVE.core.htmlquery.selector.Selector;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroup;
import com.abmash.REMOVE.core.htmlquery.selector.XpathSelector;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroup.Type;
import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;

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
		DATEPICKER,
		IMAGE,
		LIST,
		TABLE, 
		INLIST,
		INTABLE, 
		FRAME,
	}
	
	
	private Browser browser;
	private ElementType elementType;
	private ArrayList<String> queryStrings = new ArrayList<String>();
	
	// constructors
	
	public ElementCondition(Browser browser, ElementType elementType) {
		this.browser = browser;
		this.elementType = elementType;
	}
	
	public void addQueries(List<String> queries) {
		for(String query: queries) {
			addQuery(query);
		}
	}
	
	public void addQuery(String query) {
		if(query != null && !query.equals("") && !queryStrings.contains(query)) queryStrings.add(query);
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
		case DATEPICKER:
			selectorsForDatepickers();
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
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS), "input"));
		selectorGroups.add(checkElementAttributes("*", queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS), "input"));

		// find target by case insensitive attribute matches
		/*if(queryStrings != null && !queryStrings.equals("") && !queryStrings.contains("[") && !queryStrings.contains("]") && !queryStrings.contains("(") && !queryStrings.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + queryStrings + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + queryStrings + ")')"));
			selectorGroups.add(group);
		}*/
		
		// if nothing was found just return all elements
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK, 1));
//		}
	}
	
	private void selectorsForText() {
		// TODO bevorzugung von bestimmten attributen innerhalb des jeweiligen elements
		List<String> elementNames = Arrays.asList("strong", "em", "b", "i", "label", "span", "p", "div", "li", "td", "th", "*");
		List<String> attributeNames = Arrays.asList("for", "id", "class", "name", "value", "title", "alt", "*");
		
		// find target by inner text and html attributes which match exactly or partially
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS), "input"));
		selectorGroups.add(checkElementAttributes("*", queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS), "input"));

		// find target by case insensitive attribute matches
		/*if(queryStrings != null && !queryStrings.equals("") && !queryStrings.contains("[") && !queryStrings.contains("]") && !queryStrings.contains("(") && !queryStrings.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + queryStrings + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + queryStrings + ")')"));
			selectorGroups.add(group);
		}*/
		
		// if nothing was found just return all elements
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK, 1));
//		}
	}
	
	private void selectorsForTitles() {
		List<String> elementNames = Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6", "*");
		List<String> attributeNames = Arrays.asList("for", "id", "class", "name", "value", "type", "title", "alt", "*");
		
		// find target by inner text which match exactly or partially
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));

		// find elements with font-size bigger than the default
		// TODO move that to jquery
		// TODO combine css check with text check
//		HtmlElement body = browser.query().tag("body").findFirst();
//		selectorGroups.add(checkElementCssAttribute("*", Arrays.asList(body.getCssValue("font-size")), "font-size", CSSAttributeMatcher.GREATER_THAN));

		// find target by html attributes which match exactly or partially
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));
		
		// find target by tag name selector 
		// TODO tagname selector necessary?
		// TODO which query?
		/*if(queryStrings != null && !queryStrings.equals("")) {
			selectorGroups.add(new SelectorGroup(new TagnameSelector(queryStrings)));
		}*/
		
		// find target by case insensitive attribute matches
		/*if(queryStrings != null && !queryStrings.equals("") && !queryStrings.contains("[") && !queryStrings.contains("]") && !queryStrings.contains("(") && !queryStrings.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + queryStrings + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + queryStrings + ")')"));
			selectorGroups.add(group);
		}*/
		
		// if nothing was found just return all headline elements
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}
	
	private void selectorsForClickables() {
		// TODO with onclick event handler
		List<String> linkNames = Arrays.asList("a", "*[onclick]");
		List<String> inputNames = Arrays.asList("input[type='checkbox']", "input[type='radio']", "input[type='submit']", "input[type='button']", "input[type='image']", "input[type='range']", "input[type='color']", "button");
		List<String> elementNames = new ArrayList<String>(linkNames);
		elementNames.addAll(inputNames);
		List<String> attributeNames = Arrays.asList("title", "alt", "href", "id", "class", "*");

		// find links, checkboxes, radioboxes and buttons
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));

		// find any element by inner text
		selectorGroups.add(checkElementText(linkNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));

		// find clickables by searching for closest fitting label
//		if(!queryStrings.isEmpty()) {
//			HtmlElements labelElements = browser.query().isText().has(queryStrings).limit(2).find();
//			if(!labelElements.isEmpty()) {
//				selectorGroups.addLabelElements(labelElements);
//				selectorGroups.setLabelType(Direction.SELECT);
//				// now add all checkboxes for closest label comparison
//				selectorGroups.add(new SelectorGroup(new JQuerySelector("find('input[type=checkbox], input[type=radio]')", Weight.VERYLOW.getValue()), Type.LABEL));
//			}
//		}

		// TODO elements with parent <a> tags are also clickable 
		if(queryStrings.isEmpty()) {
			// TODO has-parent instead of following-sibling
//			selectors.add(new SelectorGroup(new XpathSelector("//a[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/following-sibling::input")));
		} else {
			// TODO has-parent without text
		}
		
		// find target by case insensitive attribute matches
		/*if(queryStrings != null && !queryStrings.equals("") && !queryStrings.contains("[") && !queryStrings.contains("]") && !queryStrings.contains("(") && !queryStrings.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + queryStrings + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + queryStrings + ")')"));
			selectorGroups.add(group);
		}*/	
		
		// if there were no results just find all clickables
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}
	
	private void selectorsForTypables() {
		// TODO without check and radioboxes
		List<String> elementNames = Arrays.asList(
				"input[type=password]", "input[type=text]", "input[type=email]", "input[type=url]",
				"input[type=number]", "input[type=search]", "textarea", "input");
		List<String> attributeNames = Arrays.asList("id", "name", "value", "class", "type", "title", "*");

		// find target by html attributes which match exactly
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, AttributeMatcher.EXACT));

		// find tinyMce editor iframes
		// TODO move to JavaScript
		Selector tinymceSelector = new CssSelector(".mceIframeContainer iframe[id*='" + queryStrings + "']");
		try {
			// TODO find with rootelements if needed
			if(tinymceSelector.find(browser).isEmpty()) {
				tinymceSelector = new JQuerySelector("find('.mceIframeContainer iframe:attrCaseInsensitive(CONTAINS, id, " + queryStrings + ")')");
			}
			HtmlElements tinymceElements = tinymceSelector.find(browser);
			if(tinymceElements != null && !tinymceElements.isEmpty()) {
				HtmlElement frame = tinymceElements.first();
				browser.frame().switchTo(frame);
				// TODO find with rootelements if needed
//				HtmlElement textarea = browser.query().cssSelector("#tinymce").findFirst();
//				if(textarea instanceof HtmlElement) {
//					textarea.setFrameElement(frame);
//					selectorGroups.add(new SelectorGroup(new DirectMatchSelector(new HtmlElements(textarea))));
//				}
				// TODO switch to previously focused main content 
				browser.window().switchToMainContent();
			}
		} catch (Exception e) {
			// if no element was found, just continue with next selectors
		}
		
		// TODO find inputs with fitting labels by "id" and "for"
		
		// find inputs by searching for closest fitting label
//		if(!queryStrings.isEmpty()) {
//			HtmlElements labelElements = browser.query().isText().has(queryStrings).limit(2).find();
//			if(!labelElements.isEmpty()) {
//				selectorGroups.addLabelElements(labelElements);
//			}
//		}
		
		// find target by html attributes which match partially
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));

		// find labels and other elements containing the query and return the next descendant input element
		for (String query: queryStrings) {
			// TODO combine all query strings!
			// TODO with textarea too
			selectorGroups.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + query.toLowerCase() + "')]/following-sibling::input")));
			selectorGroups.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + query.toLowerCase() + "')]/following-sibling::textarea")));
//			selectors.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::div/descendant::input")));
//			selectors.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::ul/descendant::input")));
//			selectors.add(new SelectorGroup(new XpathSelector("//*[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::div/descendant::input")));
//			selectors.add(new SelectorGroup(new XpathSelector("//*[contains(" + xpathToLowercase + ", '" + textQuery.toLowerCase() + "')]/ancestor::ul/descendant::input")));
		}
		
		// find target by case insensitive attribute matches
		/*if(queryStrings != null && !queryStrings.equals("") && !queryStrings.contains("[") && !queryStrings.contains("]") && !queryStrings.contains("(") && !queryStrings.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + queryStrings + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + queryStrings + ")')"));
			selectorGroups.add(group);
		}*/
		
		// if there were no results just find all inputs
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}
	
	private void selectorsForChoosables() {
		List<String> elementNames = Arrays.asList("select");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "value", "*");

		// find target by text and html attributes
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// find target by option labels
		selectorGroups.add(new SelectorGroup(new JQuerySelector("find('select:has(option:containsCaseInsensitive(" + queryStrings + "))')"), Weight.QUITE.getValue()));
		// TODO option attributes

		// if there were no results just find all selects
		selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		if(queryLimit != 2) alert(selector.command.toSource());

	}
	
	private void selectorsForDatepickers() {
		List<String> elementNames = Arrays.asList("input[type=date], input");
		List<String> attributeNames = Arrays.asList("id", "class", "name");
		
		// find target by option labels
		//selectors.add(new SelectorGroup(new JQuerySelector("find('select:has(option:containsCaseInsensitive(" + textQuery + "))')")));

		// find datepickers by searching for closest fitting label
//		if(!queryStrings.isEmpty()) {
//			HtmlElements labelElements = browser.query().isText().has(queryStrings).limit(2).find();
//			if(!labelElements.isEmpty()) {
//				selectorGroups.addLabelElements(labelElements);
//			}
//		}
		
		// find target by html attributes
		selectorGroups.add(checkElementAttributes(elementNames, Arrays.asList("datepicker"), attributeNames, Arrays.asList(AttributeMatcher.CONTAINS)));
		selectorGroups.add(checkElementAttributes(elementNames, Arrays.asList("calendar"), attributeNames, Arrays.asList(AttributeMatcher.CONTAINS)));
		
		for (String query: queryStrings) {
			// TODO combine all query strings!
			selectorGroups.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + query.toLowerCase() + "')]/following-sibling::input")));
			selectorGroups.add(new SelectorGroup(new XpathSelector("//label[contains(" + xpathToLowercase + ", '" + query.toLowerCase() + "')]/following-sibling::input")));
		}

		// if there were no results just find all datepickers
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}
	
	private void selectorsForImages() {
		List<String> elementNames = Arrays.asList("img");
		List<String> attributeNames = Arrays.asList("id", "title", "alt", "class", "name", "*");

		// find exact image matches
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT)));
		selectorGroups.add(checkElementCssAttribute("*", queryStrings, "background-image", Arrays.asList(CSSAttributeMatcher.EQUAL)));
		
		// find target by html attributes
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// CSS background images with check if their url match the given query strings
		// exists also as fallback without query string matching
		selectorGroups.add(checkElementCssAttribute("*", queryStrings, "background-image", Arrays.asList(CSSAttributeMatcher.CONTAINS)));
		
		// if there were no results just find all images and elements with background images
		SelectorGroup group = new SelectorGroup(Type.FALLBACK);
		group.add(new CssSelector(StringUtils.join(elementNames, ',')));
		// TODO also as fallback if query strings were specified?
		group.addAll(checkElementCssAttribute("*", Arrays.asList("none"), "background-image", Arrays.asList(CSSAttributeMatcher.NOT_EQUAL)));
		selectorGroups.add(group);
	}
	
	private void selectorsForLists() {
		List<String> elementNames = Arrays.asList("ul,ol,dl");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "*");

		// find target by text and html attributes
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// if there were no results just find all lists
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}
	
	private void selectorsForTables() {
		List<String> elementNames = Arrays.asList("table");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "*");

		// find target by text and html attributes
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// if there were no results just find all tables
		if(!queryStrings.isEmpty()) {
			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
		}
	}

	private void selectorsInLists() {
		// TODO dl besteht aus dt und dd (term/descriptions)
		List<String> elementNames = Arrays.asList("li, dt");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "value", "title", "alt", "for", "*");
		
		// find target by inner text and html attributes which match exactly or partially
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));
		
		// find target by case insensitive attribute matches
		/*if(queryStrings != null && !queryStrings.equals("") && !queryStrings.contains("[") && !queryStrings.contains("]") && !queryStrings.contains("(") && !queryStrings.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + queryStrings + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + queryStrings + ")')"));
			selectorGroups.add(group);
		}*/
		
		// if there were no results just find list items
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}

	private void selectorsInTables() {
		List<String> elementNames = Arrays.asList("td,th");
		List<String> attributeNames = Arrays.asList("id", "class", "name", "value", "title", "alt", "for", "*");
		
		// find target by inner text and html attributes which match exactly or partially
		selectorGroups.add(checkElementText(elementNames, queryStrings, Arrays.asList(TextMatcher.EXACT, TextMatcher.CONTAINS)));
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.STARTSWITH, AttributeMatcher.ENDSWITH, AttributeMatcher.CONTAINS)));
		
		// find target by case insensitive attribute matches
		/*if(queryStrings != null && !queryStrings.equals("") && !queryStrings.contains("[") && !queryStrings.contains("]") && !queryStrings.contains("(") && !queryStrings.contains(")")) {
			SelectorGroup group = new SelectorGroup(Type.FALLBACK, 1);
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(EXACT, *, " + queryStrings + ")')"));
			group.add(new JQuerySelector("find('*:attrCaseInsensitive(CONTAINS, *, " + queryStrings + ")')"));
			selectorGroups.add(group);
		}*/
		
		// if there were no results just find all table cells
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}
	
	private void selectorsForFrames() {
		List<String> elementNames = Arrays.asList("frame", "iframe");
		List<String> attributeNames = Arrays.asList("id", "name", "class", "title", "alt", "*");

		// find target by html attributes
		selectorGroups.add(checkElementAttributes(elementNames, queryStrings, attributeNames, Arrays.asList(AttributeMatcher.EXACT, AttributeMatcher.WORD, AttributeMatcher.CONTAINS)));
		
		// if there were no results just find all frames
//		if(!queryStrings.isEmpty()) {
//			selectorGroups.add(new SelectorGroup(new CssSelector(StringUtils.join(elementNames, ',')), Type.FALLBACK));
//		}
	}

	public boolean elementValid(HtmlElement foundElement) {
		if(!super.elementValid(foundElement)) return false;
		
		// TODO move to JavaScript
//		if(elementType == ElementType.TITLE && foundElement.getText().isEmpty()) return false;
			
		return true;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public String toString() {
		return super.toString() + " with type [" + elementType + "]" +
			(!queryStrings.isEmpty() ? " and text queries [" + queryStrings + "]" : "");
	}
	
}
