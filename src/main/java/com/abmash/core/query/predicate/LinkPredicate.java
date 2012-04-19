package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQueryFactory;

public class LinkPredicate extends JQueryPredicate {

	protected String text;
	
	public LinkPredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> linkSelectors = Arrays.asList("a", "*[onclick]");
		
		if(text != null) {
			containsText(JQueryFactory.select("'a'", 50), text);
			containsText(JQueryFactory.select("'*[onclick]'", 0), text);
			containsAttribute(JQueryFactory.select("'a'", 50), "*", text);
			containsAttribute(JQueryFactory.select("'*[onclick]'", 0), "*", text);
		} else {
			add(JQueryFactory.select("'a'", 40));
			add(JQueryFactory.select("'*[onclick]'", 20));
		}
	}
}
