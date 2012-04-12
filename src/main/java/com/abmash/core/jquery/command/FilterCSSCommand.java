package com.abmash.core.jquery.command;

/**
 * Reduce the set of matched elements to those that have the desired css values.
 */
public class FilterCSSCommand extends FilterCommand {
	
	public enum CSSAttributeComparator {
		GREATER_THAN, LESS_THAN, HAS_VALUE, CONTAINS, NOT_EQUAL, EQUAL, 
	}
	
	/**
	 * 
	 * @param attributeName the name of the css attribute
	 * @param cssAttributeComparator
	 * @param value a concrete value or jQuery command
	 */
	public FilterCSSCommand(String attributeName, CSSAttributeComparator cssAttributeComparator, String value) {
		super(null);
		
		String cssFilterSelector = "";

		String sourceValue = "jQuery(this).css('" + attributeName + "')";
		String comparisonOperator;
		String comparisonValue = value;
		
		switch (cssAttributeComparator) {
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
			case HAS_VALUE:
				comparisonOperator = "!==";
				comparisonValue = "''";
				break;
			case CONTAINS:
				comparisonOperator = sourceValue + ".contains(" + comparisonValue + ")";
				sourceValue = "";
				comparisonValue = "";
				break;
			case NOT_EQUAL:
				comparisonOperator = "!==";
				break;
			case EQUAL:
			default:
				comparisonOperator = "==";
				break;
		}
		
		cssFilterSelector += "function() {" +
			"return " + sourceValue + " " + comparisonOperator + " " + comparisonValue + ";" +
		"}";
		
		selector = cssFilterSelector;
	}
	
}
