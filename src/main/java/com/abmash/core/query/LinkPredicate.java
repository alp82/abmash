package com.abmash.core.query;

import com.abmash.core.jquery.JQueryFactory;

public class LinkPredicate extends Predicate {

	private String name;
	
	public LinkPredicate(String name) {
		this.name = name;
	}

	@Override
	public void buildCommands() {
		jQueryList.add(JQueryFactory.select("'a'", 3).contains(name));
		jQueryList.add(JQueryFactory.select("'a'", 2));
		jQueryList.add(JQueryFactory.select("'button'", 1));
	}
}
