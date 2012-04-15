package com.abmash.core.query.predicate;

import com.abmash.core.jquery.JQueryFactory;

public class ContainsPredicate extends JQueryPredicate {

	private String text;
	
	public ContainsPredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		containsText(JQueryFactory.select("'*:not(html, head, head *)'", 1), text);
	}
}
