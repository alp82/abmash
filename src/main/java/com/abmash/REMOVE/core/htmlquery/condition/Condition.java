package com.abmash.REMOVE.core.htmlquery.condition;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.abmash.REMOVE.core.htmlquery.selector.CssSelector;
import com.abmash.REMOVE.core.htmlquery.selector.JQuerySelector;
import com.abmash.REMOVE.core.htmlquery.selector.Selector;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroup;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroups;
import com.abmash.REMOVE.core.htmlquery.selector.XpathSelector;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;


public abstract class Condition {

	// to make an xpath search case insensitive all upper case chars need to be
	// transformed to lower case before doing the contains() search
	// TODO xpath 2.0 supports lower-case()
	final protected String xpathToLowercase = "translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ', 'abcdefghijklmnopqrstuvwxyzæøå')";

	/*
	 * Weights for selectors
	 */
	protected enum Weight {
		HUGE(32),
		VERYHIGH(16),
		HIGH(8),
		QUITE(4),
		LOW(2),
		VERYLOW(1);
		
		private int value;
		
		private Weight(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
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
		GREATER_THAN, LESS_THAN, HAS_VALUE, CONTAINS, NOT_EQUAL, EQUAL, 
	}

	/*
	 * Matching types for element texts
	 */
	public enum TextMatcher {
		EXACT, CONTAINS
	}
	
	protected SelectorGroups selectorGroups = null;
	
	public String toString() {
		return getType();
	}

	public String getType() {
		return getClass().getSimpleName().toLowerCase().replace("condition", "");
	}

	public boolean isElementFinder() {
		return true;
	}
	
	/**
	 * Get all selectors for this condition
	 * @return all {@link SelectorGroups} for this condition
	 */
	public SelectorGroups getSelectorGroups() {
		// if not already done, build all selector groups
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
	 * @return {@link Selector}
	 */
	protected List<Selector> checkElementAttributes(List<String> mainSelectors, List<String> queries, String attributeName, AttributeMatcher attributeMatcher, String mainSelectorNot) {
		// build the selector
		List<Selector> selectors = new ArrayList<Selector>();
		for (String mainSelector: mainSelectors) {
			
			// build main selector
			String mainSelectorCSS = mainSelector;
			String mainSelectorXPath = mainSelector;
			if(mainSelectorNot != null) {
				mainSelectorCSS += ":not(" + mainSelectorNot + ")";
				mainSelectorXPath += "[not(self::" + mainSelectorNot + ")]";
			}
			
			// always use css selector, except for variable attribute names which need to be handled by xpath selectors
			Boolean useCssSelector = !attributeName.equals("*");
			
			// do not process empty queries other than EXISTS
			if(queries.isEmpty() && attributeMatcher != AttributeMatcher.EXISTS) continue;
			
			// compute weight of this selector
			int weight = 1;
			switch (attributeMatcher) {
			case EXACT:
				weight = Weight.HUGE.getValue();
				break;
			case WORD:
				weight = Weight.VERYHIGH.getValue();
				break;
			case STARTSWITH:
			case ENDSWITH:
				weight = Weight.HIGH.getValue();
				break;
			case CONTAINS:
				weight = Weight.QUITE.getValue();
				break;
			case EXISTS:
				weight = Weight.LOW.getValue();
				break;
			}
			
			// finally add the selector with the given parameters
			String attributeSelectors = "";
			for (String query: queries) attributeSelectors += ":attrCaseInsensitive(" + attributeMatcher.toString() + ", " + attributeName + ", " + query + ")";
			if(useCssSelector) {
				selectors.add(new JQuerySelector("find('" + mainSelectorCSS + attributeSelectors + "')", weight));
			} else {
				selectors.add(new JQuerySelector("xpath('//" + mainSelectorXPath + "').filter('" + attributeSelectors + "')", weight));
			}
			
		}
//		System.out.println(selectors);
		
		return selectors;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, List<String> queries, List<String> attributeNames, AttributeMatcher attributeMatcher, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (String attributeName: attributeNames) {
			selectorGroup.addAll(checkElementAttributes(mainSelectors, queries, attributeName, attributeMatcher, mainSelectorNot));
		}
		return selectorGroup;
	}
	
	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(List<String> mainSelectors, List<String> queries, List<String> attributeNames, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		if(queries.isEmpty()) attributeMatchers = Arrays.asList(AttributeMatcher.EXACT);
		for (AttributeMatcher attributeMatcher: attributeMatchers) {
			selectorGroup.addAll(checkElementAttributes(mainSelectors, queries, attributeNames, attributeMatcher, mainSelectorNot));
		}
		return selectorGroup;
	}

	/**
	 * Check element attributes
	 */
	protected SelectorGroup checkElementAttributes(String mainSelector, List<String> queries, List<String> attributeNames, List<AttributeMatcher> attributeMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		selectorGroup.addAll(checkElementAttributes(Arrays.asList(mainSelector), queries, attributeNames, attributeMatchers, mainSelectorNot));
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
		selectorGroup.addAll(checkElementAttributes(Arrays.asList(mainSelector), queries, Arrays.asList(attributeName), Arrays.asList(attributeMatcher), null));
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
	protected SelectorGroup checkElementCssAttribute(String mainSelector, List<String> queries, String attributeName, CSSAttributeMatcher attributeMatcher) {
		SelectorGroup selectorGroup = new SelectorGroup();

		
		// weight of this selector
		int weight = 1;
		
		for (String query: queries) {
			String sourceValue = "jQuery(this).css('" + attributeName + "')";
			String expression = "find('" + mainSelector + "')";
			
			String comparisonValue = "'" + query + "'";
			String comparisonOperator;
			
			switch (attributeMatcher) {
				case GREATER_THAN:
					comparisonOperator = ">";
					sourceValue = "parseInt(" + sourceValue + ", 10)";
					comparisonValue = "parseInt(" + comparisonValue + ", 10)";
					weight = Weight.HIGH.getValue();
					break;
				case LESS_THAN:
					comparisonOperator = "<";
					sourceValue = "parseInt(" + sourceValue + ", 10)";
					comparisonValue = "parseInt(" + comparisonValue + ", 10)";
					weight = Weight.HIGH.getValue();
					break;
				case HAS_VALUE:
					comparisonOperator = "!==";
					comparisonValue = "''";
					weight = Weight.LOW.getValue();
					break;
				case CONTAINS:
					comparisonOperator = sourceValue + ".contains(" + comparisonValue + ")";
					sourceValue = "";
					comparisonValue = "";
					weight = Weight.QUITE.getValue();
					break;
				case NOT_EQUAL:
					comparisonOperator = "!==";
					weight = Weight.LOW.getValue();
					break;
				case EQUAL:
				default:
					comparisonOperator = "==";
					weight = Weight.HUGE.getValue();
					break;
			}
			
			expression += ".filter(function() {" +
				"return " + sourceValue + " " + comparisonOperator + " " + comparisonValue + ";" +
			"})";
			
			selectorGroup.add(new JQuerySelector(expression, weight));
		}
		
//		System.out.println(selectorGroup);
		return selectorGroup;
	}
	
	protected SelectorGroup checkElementCssAttribute(String mainSelector, List<String> queries, String attributeName, List<CSSAttributeMatcher> attributeMatchers) {
		SelectorGroup selectorGroup = new SelectorGroup();
		for (CSSAttributeMatcher attributeMatcher: attributeMatchers) {
			selectorGroup.addAll(checkElementCssAttribute(mainSelector, queries, attributeName, attributeMatcher));
		}
		return selectorGroup;
	}
	
	/**
	 * Check the inner text of an element. The method looks either for exact or partial matches.
	 */
	protected List<Selector> checkElementText(List<String> mainSelectors, List<String> queries, TextMatcher textMatcher, String mainSelectorNot) {
		List<Selector> selectors = new ArrayList<Selector>();
		
		// compute weight of this selector
		int weight = 1;
		switch (textMatcher) {
		case EXACT:
			weight = Weight.HUGE.getValue();
			break;
		case CONTAINS:
			weight = Weight.QUITE.getValue();
			break;
		}
		
		for (String mainSelector: mainSelectors) {
			
			// build the selector
			String mainSelectorCSS = mainSelector;
//			String mainSelectorXPath = mainSelector;
			if(mainSelectorNot != null) {
				mainSelectorNot = mainSelectorNot.replaceAll("'", "\'");
				mainSelectorCSS += ":not(" + mainSelectorNot + ")";
//				mainSelectorXPath += "[not(" + mainSelectorNot + ")]";
			}
			
			if(queries.isEmpty()) {
				// Warning: mainSelector * and an empty query returns all html elements
				selectors.add(new CssSelector(mainSelectorCSS, weight));
			} else {
				String querySelectors = "";
				switch (textMatcher) {
				case EXACT:
					// element text matches exactly the query string
					for (String query: queries) querySelectors += ":isCaseInsensitive(\"" + query + "\")";
					selectors.add(new JQuerySelector("find('" + mainSelectorCSS + querySelectors + "')", weight));
					break;
				case CONTAINS:
				default:
					// element text contains the query string
					for (String query: queries) querySelectors += ":containsCaseInsensitive(\"" + query + "\")";
					selectors.add(new JQuerySelector("find('" + mainSelectorCSS + querySelectors + "')", weight));
					break;
				}
			}
		}
		
//		browser.log().debug(" find text selector: " + selector);
		// finally return the selector
		return selectors;
	}
	
	/**
	 * Check element text
	 */
	protected SelectorGroup checkElementText(List<String> mainSelectors, List<String> queries, List<TextMatcher> textMatchers, String mainSelectorNot) {
		SelectorGroup selectorGroup = new SelectorGroup();
		if(queries.isEmpty()) textMatchers = Arrays.asList(TextMatcher.EXACT);
		for (TextMatcher textMatcher: textMatchers) {
			selectorGroup.addAll(checkElementText(mainSelectors, queries, textMatcher, mainSelectorNot));
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
	
}
