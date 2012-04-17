package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public class SubmittablePredicate extends JQueryPredicate {

	private String text;
	
	public SubmittablePredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> inputSelectors = Arrays.asList("form", "input", "textarea");
		
		if(text != null) {
			containsText("'" + StringUtils.join(inputSelectors, ',') + "'", text);
			containsAttribute("'" + StringUtils.join(inputSelectors, ',') + "'", "*", text);
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(inputSelectors, ',') + "'", 50));
		}
	}
}
