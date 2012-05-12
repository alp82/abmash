package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.jquery.JQuery.StringMatcher;
import com.abmash.core.jquery.command.FilterCSSCommand.CSSAttributeComparator;

public class HeadlinePredicate extends JQueryPredicate {

	private String text;
	
	public HeadlinePredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> headlineSelectors = Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6");
		JQuery elementsWithBiggerFontSize = JQueryFactory.select("'*:not(input,iframe)'", 50) // all elements but inputs and iframes
			.filterCSS("font-size", CSSAttributeComparator.GREATER_THAN, "jQuery(document.body).css('font-size')") // elements with bigger font size
			.filter("function() { return jQuery(this).text().trim().length; }") // only elements with inner text
		; 
		if(text != null) {
			containsText("'" + StringUtils.join(headlineSelectors, ',') + "'", text);
			containsAttribute("'" + StringUtils.join(headlineSelectors, ',') + "'", "*", text);
			containsText(elementsWithBiggerFontSize, text);
			containsAttribute(elementsWithBiggerFontSize, "*", text);
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(headlineSelectors, ',') + "'", 100));
			add(elementsWithBiggerFontSize);
		}
	}
}
