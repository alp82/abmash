package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.jquery.JQuery.StringMatcher;
import com.abmash.core.jquery.command.FilterCSSCommand.CSSAttributeComparator;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public class ImagePredicate extends JQueryPredicate {

	private String text;
	
	public ImagePredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> imageSelectors = Arrays.asList("img");
		if(text != null) {
			// close to label
			closeTo(
				JQueryFactory.select("'" + StringUtils.join(imageSelectors, ',') + "'", 100),
				new DirectionOptions(DirectionType.CLOSETO).setLimitPerTarget(1).setMaxDistance(300),
				QueryFactory.text(text)
			);
			
			containsAttribute("'" + StringUtils.join(imageSelectors, ',') + "'", "*", text);
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(imageSelectors, ',') + "'", 100));
		}
	}
}
