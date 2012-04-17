package com.abmash.core.query.predicate;

import com.abmash.core.jquery.JQueryFactory;

public class SelectPredicate extends JQueryPredicate {

	private String name;
	
	public SelectPredicate(String name) {
		this.name = name;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		add(JQueryFactory.select(name != null && !name.equals("") ? "'" + name + "'" : null, 100));
	}
}
