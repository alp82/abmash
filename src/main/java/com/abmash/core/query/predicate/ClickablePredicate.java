package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public class ClickablePredicate extends LinkPredicate {

	public ClickablePredicate(String text) {
		super(text);
	}

	@Override
	public void buildCommands() {
		super.buildCommands();
		List<String> clickableInputSelectors = Arrays.asList("input[type=checkbox]", "input[type=radio]", "input[type=submit]", "input[type=button]", "input[type=image]", "input[type=range]", "input[type=color]", "button");
		
		JQuery clickableQuery = JQueryFactory.select("'" + StringUtils.join(clickableInputSelectors, ',') + "'", 0); 
				
		if(text != null) {
			containsText(clickableQuery, text);
			containsAttribute(clickableQuery, "*", text);
			
			// close to label
			closeTo(
				clickableQuery.setWeight(50),
				new DirectionOptions(DirectionType.CLOSETOCLICKABLELABEL).setLimitPerTarget(1).setMaxDistance(300),
				QueryFactory.text(text)
			);
		} else {
			add(clickableQuery.setWeight(50));
		}
	}
}
