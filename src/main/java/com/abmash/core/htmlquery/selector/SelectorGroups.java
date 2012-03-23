package com.abmash.core.htmlquery.selector;

import java.util.ArrayList;

import com.abmash.api.HtmlElements;

public class SelectorGroups extends ArrayList<SelectorGroup> {
	
	HtmlElements labelElements = new HtmlElements();
	
	@Override
	public boolean add(SelectorGroup e) {
		return e.isEmpty() ? false : super.add(e);
	}

	public void addLabelElements(HtmlElements labelElements) {
		this.labelElements.addAll(labelElements);
	}
	
	public boolean hasLabelElements() {
		return !labelElements.isEmpty();
	}
	
	public HtmlElements getLabelElements() {
		return labelElements;
	}
	
}
