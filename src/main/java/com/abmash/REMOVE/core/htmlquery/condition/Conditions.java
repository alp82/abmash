package com.abmash.REMOVE.core.htmlquery.condition;

import java.util.ArrayList;

import com.abmash.api.HtmlElement;

public class Conditions extends ArrayList<Condition> {

	public boolean elementValid(HtmlElement element) {
		for(Condition condition: this) {
			if(!condition.elementValid(element)) return false;
		}
		return true;
	}

	public boolean hasElementFinder() {
		for(Condition condition: this) {
			if(condition.isElementFinder()) return true;
		}
		return false;
	}

}
