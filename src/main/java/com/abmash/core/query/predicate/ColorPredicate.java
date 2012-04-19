package com.abmash.core.query.predicate;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.ColorOptions;

public class ColorPredicate extends JQueryPredicate {
	
	private ColorOptions options;

	// TODO no predicates needed
	public ColorPredicate(ColorOptions options) {
		super();
		this.options = options;
		buildCommands();
	}
	
	public ColorPredicate(Color color, Double tolerance, Double dominance) {
		this(new ColorOptions(color, tolerance, dominance));
	}
	
	@Override
	public void buildCommands() {
		add(JQueryFactory.select("abmash.getData('elementsForFilteringQuery')", 50).color(options));
	}
	
	public Color getOptions() {
		return options.getColor();
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces, " (color: " + options.getColor().toString() + ", tolerance: " + options.getTolerance() + ", dominance: " + options.getDominance() + ")");
	}

	
}
