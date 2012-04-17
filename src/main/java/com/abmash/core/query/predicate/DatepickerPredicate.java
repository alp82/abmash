package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public class DatepickerPredicate extends JQueryPredicate {

	private String text;
	
	public DatepickerPredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		// TODO input[type=date] ?
		
		JQuery datePickerJQuery = JQueryFactory.select("'input:attrMatch(CONTAINS, *, \"datepicker\")'", 0);
		if(text != null) {
			// close to label
			closeTo(
				datePickerJQuery.setWeight(100),
				new DirectionOptions(DirectionType.CLOSETOLABEL).setLimitPerTarget(1).setMaxDistance(300),
				QueryFactory.text(text)
			);
			
			containsAttribute(datePickerJQuery, "*", text);
		} else {
			add(datePickerJQuery);
		}
	}
	
}
