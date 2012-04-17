package com.abmash.core.query.predicate;

import com.abmash.api.HtmlElements;
import com.abmash.core.jquery.JQueryFactory;

public class ElementPredicate extends Predicate {
	
	private HtmlElements elements;

	// TODO no predicates needed
	public ElementPredicate(HtmlElements elements) {
		super();
		this.elements = elements;
	}
	
	public HtmlElements getElements() {
		return elements;
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces, " (elements: " + elements + ")");
	}

	
}
