package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.jquery.JQuery.StringMatcher;
import com.abmash.core.jquery.command.FilterCSSCommand.CSSAttributeComparator;

public class FramePredicate extends JQueryPredicate {

	private String text;
	
	public FramePredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> headlineSelectors = Arrays.asList("frame, iframe");
		if(text != null) {
			containsText("'" + StringUtils.join(headlineSelectors, ',') + "'", text);
			containsAttribute("'" + StringUtils.join(headlineSelectors, ',') + "'", "*", text);
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(headlineSelectors, ',') + "'", 100));
		}
	}
}
