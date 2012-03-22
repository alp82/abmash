package com.abmash.core.htmlquery.selector;

import java.util.ArrayList;

public class SelectorGroups extends ArrayList<SelectorGroup> {
	
	@Override
	public boolean add(SelectorGroup e) {
		return e.isEmpty() ? false : super.add(e);
	}

}
