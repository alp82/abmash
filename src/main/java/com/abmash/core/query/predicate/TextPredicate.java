package com.abmash.core.query.predicate;

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
		JQuery elementsWithText = JQueryFactory.select("'*:not(input,iframe)'", 50) // all elements but inputs and iframes
			.filter("function() { return jQuery(this).text().trim().length; }") // only elements with inner text
		; 
		if(text != null) {
			containsText(elementsWithText, text);
			containsAttribute(elementsWithText, "*", text);
		} else {
			add(elementsWithText);
		}
	}
}
