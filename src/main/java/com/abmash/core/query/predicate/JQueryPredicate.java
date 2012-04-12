package com.abmash.core.query.predicate;

import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQuery.StringMatcher;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.jquery.JQueryList;

public abstract class JQueryPredicate extends Predicate {

	protected JQueryList jQueryList = new JQueryList();
	
	public abstract void buildCommands();
	
	public JQueryList getJQueryList() {
		return jQueryList;
	}
	
	protected void add(JQuery jQuery) {
		jQueryList.add(jQuery);
	}

	protected void containsText(String selector, String text) {
		containsText(JQueryFactory.select(selector, 1), text);
	}
	
	protected void containsText(JQuery jQuery, String text) {
		add(jQuery.clone(80).containsText(StringMatcher.EXACT, text));
		add(jQuery.clone(60).containsText(StringMatcher.WORD, text));
		add(jQuery.clone(40).containsText(StringMatcher.STARTSWITH, text));
		add(jQuery.clone(35).containsText(StringMatcher.ENDSWITH, text));
		add(jQuery.clone(20).containsText(StringMatcher.CONTAINS, text));
	}

	protected void containsAttribute(String selector, String attributeName, String text) {
		containsAttribute(JQueryFactory.select(selector, 1), attributeName, text);
	}
	
	protected void containsAttribute(JQuery jQuery, String attributeName, String text) {
		add(jQuery.clone(70).containsAttribute(StringMatcher.EXACT, attributeName, text));
		add(jQuery.clone(50).containsAttribute(StringMatcher.WORD, attributeName, text));
		add(jQuery.clone(30).containsAttribute(StringMatcher.STARTSWITH, attributeName, text));
		add(jQuery.clone(25).containsAttribute(StringMatcher.ENDSWITH, attributeName, text));
		add(jQuery.clone(10).containsAttribute(StringMatcher.CONTAINS, attributeName, text));
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces) + "\n" + jQueryList.toString(intendationSpaces + 2);
	}

}
