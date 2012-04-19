package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public class CheckablePredicate extends JQueryPredicate {

	private String text;
	
	public CheckablePredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> inputSelectors = Arrays.asList("input[type=checkbox]", "input[type=radio]");
		
		if(text != null) {
			// close to label
			closeTo(
				JQueryFactory.select("'" + StringUtils.join(inputSelectors, ',') + "'", 100),
				new DirectionOptions(DirectionType.CLOSETOCLICKABLELABEL).setLimitPerTarget(1).setMaxDistance(300),
				QueryFactory.text(text)
			);
			
			containsText("'" + StringUtils.join(inputSelectors, ',') + "'", text);
			containsAttribute("'" + StringUtils.join(inputSelectors, ',') + "'", "*", text);
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(inputSelectors, ',') + "'", 50));
		}
	}
	
}
