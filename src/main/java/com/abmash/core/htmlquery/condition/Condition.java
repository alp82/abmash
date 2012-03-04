package com.abmash.core.htmlquery.condition;


import java.util.Arrays;
import java.util.List;

import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.core.htmlquery.selector.CssSelector;
import com.abmash.core.htmlquery.selector.JQuerySelector;
import com.abmash.core.htmlquery.selector.Selector;
import com.abmash.core.htmlquery.selector.SelectorGroup;
import com.abmash.core.htmlquery.selector.SelectorGroups;
import com.abmash.core.htmlquery.selector.XpathSelector;


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
	
	protected SelectorGroups selectorGroups = null;

	public boolean isElementFinder() {
		return true;
	}
	
	/**
	 * Get all selectors for this condition
	 * @return
	 */
	public SelectorGroups getSelectors() {
		if(selectorGroups == null) {
			selectorGroups = new SelectorGroups();
			buildSelectors();
		}
		return selectorGroups;
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
	protected Selector checkElementAttributes(String mainSelector, List<String> queries, String attributeName, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
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
		Selector selector = null;
		for (AttributeMatcher attributeMatcher: attributeMatchers) {
			// do not process empty queries other than EXISTS
			if(queries.isEmpty() && attributeMatcher != AttributeMatcher.EXISTS) continue;
			
			String attributeSelectors = "";
			switch (attributeMatcher) {
				case EXISTS:
					// attribute value exists, no matter which value it has
					if(useCssSelector) {
						attributeSelectors = "[" + attributeName + "]";
					} else {
						attributeSelectors = "[@*]";
					}
					break;
				case EXACT:
					// attribute value matches exactly the query string
					if(useCssSelector) {
						for (String query: queries) attributeSelectors += "[" + attributeName + "='" + query + "']";
					} else {
						for (String query: queries) attributeSelectors += "[@*='" + query + "']";
					}
					break;
				case WORD:
					// attribute value has at least one word matching the query string
					if(useCssSelector) {
						for (String query: queries) attributeSelectors += "[" + attributeName + "~='" + query + "']";
					} else {
						for (String query: queries) attributeSelectors += "[contains(concat(' ', @*, ' '), ' " + query + " ')]";
					}
					break;
				case STARTSWITH:
					// attribute value begins with query string
					if(useCssSelector) {
						for (String query: queries) attributeSelectors += "[" + attributeName + "^='" + query + "']";
					} else {
						for (String query: queries) attributeSelectors += "[@*[starts-with(., '" + query + "')]]";
					}
					break;
				case ENDSWITH:
					// attribute value ends with query string
					if(useCssSelector) {
						for (String query: queries) attributeSelectors += "[" + attributeName + "$='" + query + "']";
					} else {
						// TODO ends-with produces errors
//						for (String query: queries) attributeSelectors += "[@*[starts-with(., '" + query + "')]]";
						for (String query: queries) attributeSelectors += "[@*[ends-with(., '" + query + "')]]";
						//*[not(self::input)][@*[ends-with(.,'Copyright')]]
						//*[not(self::input)][@*[substring(., string-length() -8) = 'Copyright']]
					}
					break;
				case CONTAINS:
				default:
					// attribute value contains the query string
					if(useCssSelector) {
						for (String query: queries) attributeSelectors += "[" + attributeName + "*='" + query + "']";
					} else {
						for (String query: queries) attributeSelectors += "[@*[contains(., '" + query + "')]]";
					}
					break;
	
			}
			
			// finally add the selector with the given parameters
			if(useCssSelector) {
				selector = new CssSelector(mainSelectorCSS + attributeSelectors);
			} else {
				selector = new XpathSelector("//" + mainSelectorXPath + attributeSelectors);
			}
			
			// TODO use always jquery selector
			// selector = new JQuerySelector(rootElement, mainSelector + ":attrCaseInsensitive(" + attributeMatcher + ", " + attributeName + ", " + query + ")"); 
			//browser.log().debug(" find attributes selector: " + selector);
		}
		
		return selector;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, List<String> queries, List<String> attributeNames, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (String attributeName: attributeNames) {
			selectorGroup.add(checkElementAttributes(mainSelector, queries, attributeName, attributeMatchers, mainSelectorNot));
		}
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, List<String> queries, List<String> attributeNames, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (String mainSelector: mainSelectors) {
			selectorGroup.addAll(checkElementAttributes(mainSelector, queries, attributeNames, attributeMatchers, mainSelectorNot));
		}
		return selectorGroup;
	}

	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, List<String> queries, List<String> attributeNames, AttributeMatcher attributeMatcher, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, queries, attributeNames, Arrays.asList(attributeMatcher), mainSelectorNot));
		return selectorGroup;
	}

	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, List<String> queries, List<String> attributeNames, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, queries, attributeNames, attributeMatchers, null));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, List<String> queries, String attributeName, AttributeMatcher attributeMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelector, queries, Arrays.asList(attributeName), Arrays.asList(attributeMatcher), null));
		return selectorGroup;
	}

	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, List<String> queries, String attributeName, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, queries, Arrays.asList(attributeName), attributeMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, List<String> queries, List<String> attributeNames, AttributeMatcher attributeMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelectors, queries, attributeNames, Arrays.asList(attributeMatcher)));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, List<String> queries, List<String> attributeNames, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(Arrays.asList(mainSelector), queries, attributeNames, attributeMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, List<String> queries, List<String> attributeNames, AttributeMatcher attributeMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelector, queries, attributeNames, Arrays.asList(attributeMatcher)));
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, List<String> queries, String attributeName, List<AttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(mainSelector, queries, Arrays.asList(attributeName), attributeMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element css attributes
	 */
	protected Selector checkElementCssAttribute(String mainSelector, List<String> queries, String attributeName, CSSAttributeMatcher attributeMatcher) {
		Selector selector;

		String sourceValue = "jQuery(this).css('" + attributeName + "')";
		String expression = "find('" + mainSelector + "')";
		
		for (String query: queries) {
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
			
			expression += ".filter(function() {" +
				"return " + sourceValue + " " + comparisonOperator + " " + comparisonValue + ";" +
			"})";
		}
		
		selector = new JQuerySelector(expression);
		
		return selector;
	}
	
	/**
	 * Check the inner text of an element. The method looks either for exact or partial matches.
	 */
	protected Selector checkElementText(String mainSelector, List<String> queries, List<TextMatcher> textMatchers, String mainSelectorNot) {
		// build the selector
		String mainSelectorCSS = mainSelector;
//		String mainSelectorXPath = mainSelector;
		if(mainSelectorNot != null) {
			mainSelectorNot = mainSelectorNot.replaceAll("'", "\'");
			mainSelectorCSS += ":not(" + mainSelectorNot + ")";
//			mainSelectorXPath += "[not(" + mainSelectorNot + ")]";
		}
		
		Selector selector = null;
		for (TextMatcher textMatcher: textMatchers) {
			if(queries.isEmpty()) {
				// Warning: mainSelector * and an empty query returns all html elements
				selector = new CssSelector(mainSelectorCSS);
			} else {
				String querySelectors = "";
				switch (textMatcher) {
				case EXACT:
					// element text matches exactly the query string
					for (String query: queries) querySelectors += ":equallyCaseInsensitive(\"" + query + "\")";
					selector = new JQuerySelector("find('" + mainSelectorCSS + querySelectors + "')");
					break;
				case CONTAINS:
				default:
					// element text contains the query string
					for (String query: queries) querySelectors += ":containsCaseInsensitive(\"" + query + "\")";
					selector = new JQuerySelector("find('" + mainSelectorCSS + querySelectors + "')");
					break;
				}
			}
		}
		
//		browser.log().debug(" find text selector: " + selector);
		// finally return the selector
		return selector;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, List<String> queries, List<TextMatcher> textMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (String mainSelector: mainSelectors) {
			selectorGroup.add(checkElementText(mainSelector, queries, textMatchers, mainSelectorNot));
		}
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, List<String> queries, TextMatcher textMatcher, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (String mainSelector: mainSelectors) {
			selectorGroup.add(checkElementText(mainSelector, queries, Arrays.asList(textMatcher), mainSelectorNot));
		}
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, List<String> queries, List<TextMatcher> textMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(mainSelectors, queries, textMatchers, null));
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, List<String> queries, TextMatcher textMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(mainSelectors, queries, textMatcher, null));
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(String mainSelector, List<String> queries, List<TextMatcher> textMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(Arrays.asList(mainSelector), queries, textMatchers));
		return selectorGroup;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(String mainSelector, List<String> queries, TextMatcher textMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementText(Arrays.asList(mainSelector), queries, textMatcher));
		return selectorGroup;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
