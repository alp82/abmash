package com.abmash.core.query.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.api.query.QueryFactory;
import com.abmash.core.htmlquery.selector.CssSelector;
import com.abmash.core.htmlquery.selector.DirectMatchSelector;
import com.abmash.core.htmlquery.selector.JQuerySelector;
import com.abmash.core.htmlquery.selector.Selector;
import com.abmash.core.htmlquery.selector.SelectorGroup;
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
		if(text != null) {
			// close to label
			closeTo(
				JQueryFactory.select("'" + StringUtils.join(inputSelectors, ',') + "'", 100),
				new DirectionOptions(DirectionType.CLOSETO).setLimitPerTarget(1).setMaxDistance(300),
				QueryFactory.contains(text),
				QueryFactory.select(":not(input,textarea)")
			);
			
			containsText("'" + StringUtils.join(inputSelectors, ',') + "'", text);
			containsAttribute("'" + StringUtils.join(inputSelectors, ',') + "'", "*", text);
		} else {
			add(JQueryFactory.select("'" + StringUtils.join(inputSelectors, ',') + "'", 50));
		}
	}
	
	//TODO tinymce croogoo
//	Selector tinymceSelector = new CssSelector(".mceIframeContainer iframe[id*='" + queryStrings + "']");
//	try {
//		// TODO find with rootelements if needed
//		if(tinymceSelector.find(browser).isEmpty()) {
//			tinymceSelector = new JQuerySelector("find('.mceIframeContainer iframe:attrCaseInsensitive(CONTAINS, id, " + queryStrings + ")')");
//		}
//		HtmlElements tinymceElements = tinymceSelector.find(browser);
//		if(tinymceElements != null && !tinymceElements.isEmpty()) {
//			HtmlElement frame = tinymceElements.first();
//			browser.frame().switchTo(frame);
//			// TODO find with rootelements if needed
//			HtmlElement textarea = browser.query().cssSelector("#tinymce").findFirst();
//			if(textarea instanceof HtmlElement) {
//				textarea.setFrameElement(frame);
//				selectorGroups.add(new SelectorGroup(new DirectMatchSelector(new HtmlElements(textarea))));
//			}
//			// TODO switch to previously focused main content 
//			browser.window().switchToMainContent();
//		}
//	} catch (Exception e) {
//		// if no element was found, just continue with next selectors
//	}
}
