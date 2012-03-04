package com.abmash.core.htmlquery.condition;

import com.abmash.core.htmlquery.selector.SelectorGroup;
import com.abmash.core.htmlquery.selector.TagnameSelector;

public class TagnameCondition extends Condition {
	
	private String name;
	
	// constructors
	
	public TagnameCondition(String name) {
		this.name = name;
	}

	// condition
	
	@Override
	protected void buildSelectors() {
		selectorGroups.add(new SelectorGroup(new TagnameSelector(name)));
	}

	public String toString() {
		return super.toString() + " with name [" + name + "]";
	}
	
}
