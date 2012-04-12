package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQueryFactory;

public class LinkPredicate extends JQueryPredicate {

	private String text;
	
	public LinkPredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> linkSelectors = Arrays.asList("a", "button", "*[onclick]");
		if(text != null) {
			containsText("'" + StringUtils.join(linkSelectors, ',') + "'", text);
			containsAttribute("'" + StringUtils.join(linkSelectors, ',') + "'", "*", text);
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(linkSelectors, ',') + "'", 50));
		}
	}
}