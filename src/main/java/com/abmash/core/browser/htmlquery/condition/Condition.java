package com.abmash.core.browser.htmlquery.condition;


import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.core.browser.htmlquery.selector.CssSelector;
import com.abmash.core.browser.htmlquery.selector.JQuerySelector;
import com.abmash.core.browser.htmlquery.selector.Selector;
import com.abmash.core.browser.htmlquery.selector.SelectorGroup;
import com.abmash.core.browser.htmlquery.selector.Selectors;
import com.abmash.core.browser.htmlquery.selector.XpathSelector;

import java.util.Arrays;
import java.util.List;


public abstract class Condition {

	// to make an xpath search case insensitive all upper case chars need to be
	// transformed to lower case before doing the contains() search
	// TODO xpath 2.0 supports lower-case()
	final protected String xpathToLowercase = "translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ', 'abcdefghijklmnopqrstuvwxyzæøå')";

	/*
	 * Matching types for element attributes
	 */
	public enum AttributeMatcher {
		EXISTS, EXACT, WORD, STARTSWITH, ENDSWITH, CONTAINS,
	}
	
	/*
	 * Matching types for element css attributes
	 */
	public enum CSSAttributeMatcher {
		EQUAL, GREATER_THAN, LESS_THAN, CONTAINS,
	}

	/*
	 * Matching types for element texts
	 */
	public enum TextMatcher {
		EXACT, CONTAINS
	}
	
	protected Selectors selectors = null;

	public boolean isElementFinder() {
		return true;
	}
	
	/**
	 * Get all selectors for this condition
	 * @return
	 */
	public Selectors getSelectors() {
		if(selectors == null) {
			selectors = new Selectors();
			buildSelectors();
		}
		return selectors;
	}
	
	/**
	 * Build all needed selectors for this condition
	 */
	protected abstract void buildSelectors();


	/**
	 * Filter elements after retrieval from the selectors
	 * Default behavior puts all elements to the result set
	 */
	public boolean elementValid(HtmlElement element) {
		return element instanceof HtmlElement;
	}
	
	/**
	 * Sort elements. Default behaviour is no ordering.
	 * @param unsortedElements
	 * @return sorted elements
	 */
	public HtmlElements sortElements(HtmlElements unsortedElements) {
		return unsortedElements;
	}
	
	/**
	 * Check for a specific element attribute. The method looks for different types of matches.
	 * If an attributeName equals to "*" an xpath selector is used instead of a css selector.
	 * @return 
	 */
	protected Selector checkElementAttributes(String mainSelector, String query, String attributeName, AttributeMatcher attributeMatcher, String mainSelectorNot) {
		if((query == null || query.equals("")) && attributeMatcher != AttributeMatcher.EXISTS) return null;
		
		// build main selector
		String mainSelectorCSS = mainSelector;
		String mainSelectorXPath = mainSelector;
		if(mainSelectorNot != null) {
			mainSelectorCSS += ":not(" + mainSelectorNot + ")";
			mainSelectorXPath += "[not(self::" + mainSelectorNot + ")]";
		}
		
		// always use css selector, except for variable attribute names which need to be handled by xpath selectors
		Boolean useCssSelector = !attributeName.equals("*");
		
		// build the selector
		Selector selector;
		switch (attributeMatcher) {
			case EXISTS:
				// attribute value matches exactly the query string
				if(useCssSelector) {
					selector = new CssSelector(mainSelectorCSS + "[" + attributeName + "]");
				} else {
					selector = new XpathSelector("//" + mainSelectorXPath + "[@*]");
				}
				break;
			case EXACT:
				// attribute value matches exactly the query string
				if(useCssSelector) {
					selector = new CssSelector(mainSelectorCSS + "[" + attributeName + "='" + query + "']");
				} else {
					selector = new XpathSelector("//" + mainSelectorXPath + "[@*='" + query + "']");
				}
				break;
			case WORD:
				// attribute value has at least one word matching the query string
				if(useCssSelector) {
					selector = new CssSelector(mainSelectorCSS + "[" + attributeName + "~='" + query + "']");
				} else {
					selector = new XpathSelector("//" + mainSelectorXPath + "[contains(concat(' ', @*, ' '), ' " + query + " ')]");
				}
				break;
			case STARTSWITH:
				// attribute value begins with query string
				if(useCssSelector) {
					selector = new CssSelector(mainSelectorCSS + "[" + attributeName + "^='" + query + "']");
				} else {
					selector = new XpathSelector("//" + mainSelectorXPath + "[@*[starts-with(., '" + query + "')]]");
				}
				break;
			case ENDSWITH:
				// attribute value ends with query string
				if(useCssSelector) {
					selector = new CssSelector(mainSelectorCSS + "[" + attributeName + "$='" + query + "']");
				} else {
					// TODO ends-with produces errors
//					selector = new XpathSelector("//" + mainSelectorXPath + "[@*[ends-with(., '" + query + "')]]");
//					selector = new XpathSelector("//" + mainSelectorXPath + "[@*[substring(., string-length()-" + (query.length()-1) + ") = '" + query + "')]]");
					selector = new XpathSelector("//" + mainSelectorXPath + "[@*[starts-with(., '" + query + "')]]");
					//*[not(self::input)][@*[ends-with(.,'Copyright')]]
					//*[not(self::input)][@*[substring(., string-length() -8) = 'Copyright']]
				}
				break;
			case CONTAINS:
			default:
				// attribute value contains the query string
				if(useCssSelector) {
					selector = new CssSelector(mainSelectorCSS + "[" + attributeName + "*='" + query + "']");
				} else {
					selector = new XpathSelector("//" + mainSelectorXPath + "[@*[contains(., '" + query + "')]]");
				}
				break;

//			selector = new JQuerySelector(rootElement, mainSelector + ":attrCaseInsensitive(" + attributeMatcher + ", " + attributeName + ", " + query + ")"); 
//			browser.log().debug(" find attributes selector: " + selector);
			
			// finally add the selector with the given parameters
		}
		
		return selector;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, String query, List<String> attributeNames, AttributeMatcher attributeMatcher, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (String attributeName: attributeNames) {
			selectorGroup.add(checkElementAttributes(mainSelector, query, attributeName, attributeMatcher, mainSelectorNot));
		}
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, String query, List<String> attributeNames, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (AttributeMatcher attributeMatcher: attributeMatchers) {
			for (String mainSelector: mainSelectors) {
				selectorGroup.addAll(checkElementAttributes(mainSelector, query, attributeNames, attributeMatcher, mainSelectorNot));
			}
		}
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, String query, String attributeName, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(Arrays.asList(mainSelector), query, Arrays.asList(attributeName), attributeMatchers, mainSelectorNot));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, String query, List<String> attributeNames, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(Arrays.asList(mainSelector), query, attributeNames, attributeMatchers, mainSelectorNot));
		return selectorGroup;
	}

	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, String query, List<String> attributeNames, AttributeMatcher attributeMatcher, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, query, attributeNames, Arrays.asList(attributeMatcher), mainSelectorNot));
		return selectorGroup;
	}

	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, String query, List<String> attributeNames, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, query, attributeNames, attributeMatchers, null));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, String query, String attributeName, AttributeMatcher attributeMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelector, query, Arrays.asList(attributeName), attributeMatcher, null));
		return selectorGroup;
	}

	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, String query, String attributeName, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, query, Arrays.asList(attributeName), attributeMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, String query, List<String> attributeNames, AttributeMatcher attributeMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, query, attributeNames, Arrays.asList(attributeMatcher)));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, String query, List<String> attributeNames, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(Arrays.asList(mainSelector), query, attributeNames, attributeMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, String query, List<String> attributeNames, AttributeMatcher attributeMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelector, query, attributeNames, Arrays.asList(attributeMatcher)));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, String query, String attributeName, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelector, query, Arrays.asList(attributeName), attributeMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element css attributes
	 */
	protected Selector checkElementCssAttribute(String mainSelector, String query, String attributeName, CSSAttributeMatcher attributeMatcher) {
		Selector selector;

		String sourceValue = "jQuery(this).css('" + attributeName + "')";
		String comparisonValue = "'" + query + "'";
		String comparisonOperator;
		
		switch (attributeMatcher) {
			case GREATER_THAN:
				comparisonOperator = ">";
				sourceValue = "parseInt(" + sourceValue + ", 10)";
				comparisonValue = "parseInt(" + comparisonValue + ", 10)";
				break;
			case LESS_THAN:
				comparisonOperator = "<";
				sourceValue = "parseInt(" + sourceValue + ", 10)";
				comparisonValue = "parseInt(" + comparisonValue + ", 10)";
				break;
			case CONTAINS:
				comparisonOperator = sourceValue + ".contains(" + comparisonValue + ")";
				sourceValue = "";
				comparisonValue = "";
				break;
			case EQUAL:
			default:
				comparisonOperator = "==";
				break;
		}
		
		String expression = "find('" + mainSelector + "').filter(function() {" +
			"return " + sourceValue + " " + comparisonOperator + " " + comparisonValue + ";" +
		"})";
		selector = new JQuerySelector(expression);
		
		return selector;
	}
	
	/**
	 * Check the inner text of an element. The method looks either for exact or partial matches.
	 */
	protected Selector checkElementText(String mainSelector, String query, TextMatcher textMatcher, String mainSelectorNot) {
		// TODO lower case: "//*[text(" + xpathToLowercase + ", '" + query.toLowerCase() + "')]"
		
		// build the selector
		String mainSelectorCSS = mainSelector;
		String mainSelectorXPath = mainSelector;
		if(mainSelectorNot != null) {
			mainSelectorNot = mainSelectorNot.replaceAll("'", "\'");
			mainSelectorCSS += ":not(" + mainSelectorNot + ")";
			mainSelectorXPath += "[not(" + mainSelectorNot + ")]";
		}
		
		Selector selector = null;
		if(query == null || query.equals("")) {
			// Warning: mainSelector * and an empty query returns all html elements
			selector = new CssSelector(mainSelectorCSS);
		} else {
			switch (textMatcher) {
			case EXACT:
				// element text matches exactly the query string
				// TODO use lower case selector
//				selector = new XpathSelector("//" + mainSelectorXPath + "[.='" + query + "']");
				selector = new JQuerySelector("find('" + mainSelectorCSS + "').filter(function(){ return jQuery.trim(jQuery(this).text()) === '" + query.trim() + "'})");
				break;
			case CONTAINS:
			default:
				// element text contains the query string
//				selector = new XpathSelector("//" + mainSelectorXPath + "[contains(" + xpathToLowercase + ", '" + query.toLowerCase() + "')]");
				selector = new JQuerySelector("find('" + mainSelectorCSS + ":contains(\"" + query + "\")')");
				break;
			}
		}
		
//		browser.log().debug(" find text selector: " + selector);
		// finally return the selector
		return selector;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, String query, List<TextMatcher> textMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (TextMatcher textMatcher: textMatchers) {
			for (String mainSelector: mainSelectors) {
				selectorGroup.add(checkElementText(mainSelector, query, textMatcher, mainSelectorNot));
			}
		}
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, String query, TextMatcher textMatcher, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (String mainSelector: mainSelectors) {
			selectorGroup.add(checkElementText(mainSelector, query, textMatcher, mainSelectorNot));
		}
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, String query, List<TextMatcher> textMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(mainSelectors, query, textMatchers, null));
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, String query, TextMatcher textMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(mainSelectors, query, textMatcher, null));
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(String mainSelector, String query, List<TextMatcher> textMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(Arrays.asList(mainSelector), query, textMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(String mainSelector, String query, TextMatcher textMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(Arrays.asList(mainSelector), query, textMatcher));
		return selectorGroup;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
