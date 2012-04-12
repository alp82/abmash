package com.abmash.core.query.predicate;

import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.BooleanType;

public class BooleanPredicate extends RecursivePredicate {
	
	BooleanType type;

	public BooleanPredicate(BooleanType type, Predicate... predicates) {
		super(predicates);
		this.type = type;
	}
	
	public BooleanType getType() {
		return type;
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
