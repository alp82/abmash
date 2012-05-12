package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.REMOVE.core.htmlquery.selector.CssSelector;
import com.abmash.REMOVE.core.htmlquery.selector.DirectMatchSelector;
import com.abmash.REMOVE.core.htmlquery.selector.JQuerySelector;
import com.abmash.REMOVE.core.htmlquery.selector.Selector;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroup;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.api.query.QueryFactory;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.jquery.JQuery.StringMatcher;
import com.abmash.core.jquery.command.FilterCSSCommand.CSSAttributeComparator;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;

public class TypablePredicate extends JQueryPredicate {

	private String text;
	
	public TypablePredicate(String text) {
		this.text = text;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		List<String> inputSelectors = Arrays.asList(
				"input[type=password]", "input[type=text]", "input[type=email]", "input[type=url]",
				"input[type=number]", "input[type=search]", "textarea");
		
		JQuery typableQuery = JQueryFactory.select("'" + StringUtils.join(inputSelectors, ',') + "'", 0).not("':attrMatch(CONTAINS, *, \"datepicker\")'");
		
		if(text != null) {
			// tinymce support
			// TODO add closeness (close to iframe and selection of inner #tinymce element)
			add(JQueryFactory.select("'.mceIframeContainer > iframe'", 150).containsAttribute(StringMatcher.CONTAINS, "*", text).contents().find("'#tinymce'"));
			
			// close to label
			closeTo(
				typableQuery.setWeight(100),
				new DirectionOptions(DirectionType.CLOSETOLABEL).setLimitPerTarget(1).setMaxDistance(500),
				QueryFactory.text(text)
			);
			
			containsText(typableQuery, text);
			containsAttribute(typableQuery, "*", text);
		} else {
			add(typableQuery);
		}
	}
	
}