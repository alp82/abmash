package com.abmash.core.query.predicate;

import com.abmash.core.jquery.JQueryFactory;

public class TagPredicate extends JQueryPredicate {

	private String name;
	
	public TagPredicate(String name) {
		this.name = name;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		add(JQueryFactory.select("'" + name + "'", 1));
	}
}
