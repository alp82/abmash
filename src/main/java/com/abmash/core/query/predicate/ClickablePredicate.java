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
		List<String> clickablePrimarySelectors = Arrays.asList("input[type=checkbox]", "input[type=radio]", "input[type=submit]", "input[type=button]", "input[type=image]", "input[type=range]", "input[type=color]", "button");
		List<String> clickableSecondarySelectors = Arrays.asList("*[onclick]");
		
		JQuery primaryQuery = JQueryFactory.select("'" + StringUtils.join(clickablePrimarySelectors, ',') + "'", 0); 
		JQuery secondaryQuery = JQueryFactory.select("'" + StringUtils.join(clickableSecondarySelectors, ',') + "'", -500); 
				
		if(text != null) {
			containsText(primaryQuery, text);
			containsAttribute(primaryQuery, "*", text);
			
			containsText(secondaryQuery, text);
			containsAttribute(secondaryQuery, "*", text);

			// close to label
			closeTo(
				primaryQuery.setWeight(50),
				new DirectionOptions(DirectionType.CLOSETOCLICKABLELABEL).setLimitPerTarget(1).setMaxDistance(300),
				new TextPredicate(text)
			);
			
			closeTo(
				secondaryQuery,
				new DirectionOptions(DirectionType.CLOSETOCLICKABLELABEL).setLimitPerTarget(1).setMaxDistance(300),
				new TextPredicate(text)
			);
		} else {
			add(primaryQuery.setWeight(100));
			add(secondaryQuery);
		}
	}
}
