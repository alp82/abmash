package com.abmash.REMOVE.core.htmlquery.condition;


import java.awt.Color;

import com.abmash.REMOVE.core.htmlquery.selector.JQuerySelector;
import com.abmash.REMOVE.core.htmlquery.selector.SelectorGroup;


public class ColorCondition extends Condition {
	
	private SelectorGroup group = new SelectorGroup();
	private Color color;
	private Double tolerance;
	private Double dominance;
	
	// constructor
	
	public ColorCondition(Color color, Double tolerance) {
		this.color = color;
		this.tolerance = tolerance;
		this.dominance = null;
	}
	
	public ColorCondition(Color color, Double tolerance, Double dominance) {
		this.color = color;
		this.tolerance = tolerance;
		this.dominance = dominance;
	}

	// condition

	@Override
	protected void buildSelectors() {
		String col = "{ red: " + color.getRed() + ", green: " + color.getGreen() + ", blue: " + color.getBlue() + " }";
		if(dominance == null) {
			group.add(new JQuerySelector("filterIsColor(" + col + "," + tolerance + ")"));
		} else {
			group.add(new JQuerySelector("filterHasColor(" + col + "," + tolerance + "," + dominance + ")"));
		}
		selectorGroups.add(group);
	}
	
	public String toString() {
		return super.toString() + " with color [" + color + "] (tolerance [" + tolerance + "] and dominance [" + dominance + "]";
	}

}
