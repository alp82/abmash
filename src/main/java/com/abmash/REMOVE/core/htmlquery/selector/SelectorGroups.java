package com.abmash.REMOVE.core.htmlquery.selector;

import java.util.ArrayList;

import com.abmash.REMOVE.core.htmlquery.condition.ClosenessCondition.Direction;
import com.abmash.api.HtmlElements;

public class SelectorGroups extends ArrayList<SelectorGroup> {
	
	HtmlElements labelElements = new HtmlElements();
	private Direction labelType = Direction.INPUT;
	
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

	public void setLabelType(Direction type) {
		labelType = type;
	}

	public Direction getLabelType() {
		return labelType;
	}	
}
