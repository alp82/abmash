package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;

public class TextPredicate extends JQueryPredicate {

	private String text;
	
	public TextPredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> textElementNames = Arrays.asList("strong", "em", "b", "i", "label", "span", "p", "div", "li", "td", "th");
		
		JQuery textElementsWithText = JQueryFactory.select("'" + StringUtils.join(textElementNames, ',') + ":not(html, head, head *, input, iframe)'", 100) // all elements but inputs and iframes
			.filter("function() { return jQuery(this).text().trim().length; }") // only elements with inner text
		;
		
		JQuery otherElementsWithText = JQueryFactory.select("'*:not(html, head, head *, input, iframe, " + StringUtils.join(textElementNames, ',') + ")'", 50) // all elements but inputs and iframes
			.filter("function() { return jQuery(this).text().trim().length; }") // only elements with inner text
		;
		
		if(text != null) {
			containsText(textElementsWithText, text);
			containsAttribute(otherElementsWithText, "*", text);
		} else {
			add(textElementsWithText);
			add(otherElementsWithText);
		}
	}
}
