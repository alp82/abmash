package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public class ChoosablePredicate extends JQueryPredicate {

	private String text;
	
	public ChoosablePredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> inputSelectors = Arrays.asList("select");
		
		if(text != null) {
			// selects with matching option text/values
			add(JQueryFactory.select("'select:has(option:textMatch(CONTAINS, \"" + text + "\")), select:has(option:attrMatch(CONTAINS, *, \"" + text + "\"))'", 150));
			
			// close to label
			closeTo(
				JQueryFactory.select("'" + StringUtils.join(inputSelectors, ',') + "'", 50),
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
