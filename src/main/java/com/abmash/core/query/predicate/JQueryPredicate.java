package com.abmash.core.query.predicate;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQuery.StringMatcher;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.jquery.JQueryList;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public abstract class JQueryPredicate extends Predicate {

	protected JQueryList jQueryList = new JQueryList();
	
	public abstract void buildCommands();
	
	public JQueryList getJQueryList() {
		return jQueryList;
	}
	
	protected void add(JQuery jQuery) {
		jQueryList.add(jQuery);
	}
	
	protected void closeTo(JQuery jQuery, DirectionOptions options, Predicate... predicates) {
		closeTo(jQuery, options, new Predicates(predicates));
	}
	
	protected void closeTo(JQuery jQuery, DirectionOptions options, Predicates predicates) {
		add(jQuery.clone().closeTo(options, predicates));
	}

	protected void containsText(String selector, String text) {
		containsText(JQueryFactory.select(selector, 0), text);
	}
	
	protected void containsText(JQuery jQuery, String text) {
		// TODO CACHE CLONED JQUERY RESULTS
		add(jQuery.clone(jQuery.getWeight() + 800).containsText(StringMatcher.EXACT, text));
		add(jQuery.clone(jQuery.getWeight() + 60).containsText(StringMatcher.WORD, text));
		add(jQuery.clone(jQuery.getWeight() + 40).containsText(StringMatcher.STARTSWITH, text));
		add(jQuery.clone(jQuery.getWeight() + 35).containsText(StringMatcher.ENDSWITH, text));
		add(jQuery.clone(jQuery.getWeight() + 20).containsText(StringMatcher.CONTAINS, text));
	}

	protected void containsAttribute(String selector, String attributeName, String text) {
		containsAttribute(JQueryFactory.select(selector, 0), attributeName, text);
	}
	
	protected void containsAttribute(JQuery jQuery, String attributeName, String text) {
		add(jQuery.clone(jQuery.getWeight() + 700).containsAttribute(StringMatcher.EXACT, attributeName, text));
		add(jQuery.clone(jQuery.getWeight() + 50).containsAttribute(StringMatcher.WORD, attributeName, text));
		add(jQuery.clone(jQuery.getWeight() + 30).containsAttribute(StringMatcher.STARTSWITH, attributeName, text));
		add(jQuery.clone(jQuery.getWeight() + 25).containsAttribute(StringMatcher.ENDSWITH, attributeName, text));
		add(jQuery.clone(jQuery.getWeight() + 10).containsAttribute(StringMatcher.CONTAINS, attributeName, text));
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return toString(intendationSpaces, "");
	}
	
	@Override
	public String toString(int intendationSpaces, String additionalInformation) {
		return super.toString(intendationSpaces, additionalInformation) + "\n" + jQueryList.toString(intendationSpaces + 2);
	}
}
