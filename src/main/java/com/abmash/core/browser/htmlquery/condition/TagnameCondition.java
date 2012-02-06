package com.abmash.core.browser.htmlquery.condition;

import com.abmash.core.browser.htmlquery.selector.SelectorGroup;
import com.abmash.core.browser.htmlquery.selector.TagnameSelector;

public class TagnameCondition extends Condition {
	
	private String name;
	
	// constructors
	
	public TagnameCondition(String name) {
		this.name = name;
	}

	// condition
	
	@Override
	protected void buildSelectors() {
		selectors.add(new SelectorGroup(new TagnameSelector(name)));
	}

	public String toString() {
		return super.toString() + " with name \"" + name + "\"";
	}
	
}
