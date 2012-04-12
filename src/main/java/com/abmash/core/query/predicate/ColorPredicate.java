package com.abmash.core.query.predicate;

import java.awt.Color;

import com.abmash.core.query.DirectionType;

public class ColorPredicate extends RecursivePredicate {
	
	private Color color;
	private Double tolerance;
	private Double dominance;

	// TODO no predicates needed
	public ColorPredicate(Color color, Double tolerance, Double dominance, Predicate... predicates) {
		super(predicates);
		this.color = color;
		this.tolerance = tolerance;
		this.dominance = dominance;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Double getTolerance() {
		return tolerance;
	}
	
	public Double getDominance() {
		return dominance;
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces, " (color: " + color.toString() + ", tolerance: " + tolerance + ", dominance: " + dominance + ")");
	}
	
}
