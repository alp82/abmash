package com.abmash.core.query.predicate;

import com.abmash.core.jquery.JQueryFactory;

public class XPathPredicate extends JQueryPredicate {

	private String selector;
	
	public XPathPredicate(String selector) {
		this.selector = selector;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		add(JQueryFactory.select(null, 100).xPath(selector));
	}
}
