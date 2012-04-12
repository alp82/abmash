package com.abmash.core.query.predicate;

import com.abmash.core.query.DirectionType;

public class DirectionPredicate extends RecursivePredicate {
	
	// TODO degree?
	DirectionType type;
	int distance;

	public DirectionPredicate(DirectionType type, int distance, Predicate... predicates) {
		super(predicates);
		this.type = type;
		this.distance = distance;
	}
	
	public DirectionType getType() {
		return type;
	}
	
	public int getDistance() {
		return distance;
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces, " (" + type.toString() + ")");
	}
	
}
