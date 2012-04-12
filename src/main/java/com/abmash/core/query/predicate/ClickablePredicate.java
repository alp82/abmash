package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQueryFactory;

public class ClickablePredicate extends LinkPredicate {

	private String text;
	
	public ClickablePredicate(String text) {
		super(text);
	}

	@Override
	public void buildCommands() {
		super.buildCommands();
		List<String> clickableInputSelectors = Arrays.asList("input[type='checkbox']", "input[type='radio']", "input[type='submit']", "input[type='button']", "input[type='image']", "input[type='range']", "input[type='color']");
		if(text != null) {
			containsText("'" + StringUtils.join(clickableInputSelectors, ',') + "'", text);
			containsAttribute("'" + StringUtils.join(clickableInputSelectors, ',') + "'", "*", text);
			
			// TODO has label
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(clickableInputSelectors, ',') + "'", 50));
		}
	}
}
