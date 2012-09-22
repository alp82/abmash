package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;

public class TextPredicate extends JQueryPredicate {

	private String text;
	
	public TextPredicate(String text) {
		this(text, 0);
	}

	public TextPredicate(String text, double weight) {
		this.text = text;
		this.weight = weight;
		buildCommands();
	}	
	
	@Override
	public void buildCommands() {
		List<String> textElementNames = Arrays.asList("strong", "em", "b", "i", "label", "span", "p", "div", "li", "td", "th");
		
		JQuery textElementsWithText = JQueryFactory.select("'" + StringUtils.join(textElementNames, ',') + ":visible:not(html, head, head *, input, iframe)'", 10) // all elements but inputs and iframes
			.filter("function() { return jQuery(this).text().trim().length; }"); // only elements with inner text
		
		JQuery otherElementsWithText = JQueryFactory.select("'*:visible:not(html, head, head *, input, iframe, table, " + StringUtils.join(textElementNames, ',') + ")'", 0) // all elements but inputs and iframes
			.filter("function() { return jQuery(this).text().trim().length; }"); // only elements with inner text
		
		if(text != null) {
			containsText(textElementsWithText, text);
			containsAttribute(otherElementsWithText, "*", text);
		} else {
			add(textElementsWithText);
			add(otherElementsWithText);
		}
	}
}
