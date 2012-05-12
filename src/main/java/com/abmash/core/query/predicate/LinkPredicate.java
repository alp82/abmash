package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;

public class LinkPredicate extends JQueryPredicate {

	protected String text;
	
	public LinkPredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> linkSelectors = Arrays.asList("a");
		
		JQuery linkQuery = JQueryFactory.select("'" + StringUtils.join(linkSelectors, ',') + "'", 0); 

		if(text != null) {
			containsText(linkQuery, text);
			containsAttribute(linkQuery, "*", text);
		} else {
			add(linkQuery.setWeight(50));
		}
	}
}
