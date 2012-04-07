package com.abmash.api.query.predicate;

import com.abmash.core.jquery.JQueryFactory;

public class LinkPredicate extends Predicate {

	private String name;
	
	public LinkPredicate(String name) {
		this.name = name;
	}

	@Override
	public void buildCommands() {
		jQueryList.add(JQueryFactory.select("'a'", 1));
		jQueryList.add(JQueryFactory.select("'button'", 1));
	}
}
