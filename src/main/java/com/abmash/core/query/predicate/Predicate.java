package com.abmash.core.query.predicate;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.command.Command;
import com.abmash.core.query.BooleanType;

public abstract class Predicate {

	// Infix notation (usually a lot more readable than the prefix-notation)
	public Predicate and(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.AND, this).addPredicates(predicates);
	}

	public Predicate or(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.OR, this).addPredicates(predicates);
	}

	public Predicate not(Predicate... predicates) {
		return and(new BooleanPredicate(BooleanType.NOT, predicates));
	}
	
	@Override
	public String toString() {
		return toString(0);
	}	
	
	public String toString(int intendationSpaces) {
		return toString(intendationSpaces, "");
	}
	
	public String toString(int intendationSpaces, String additionalInformation) {
		return StringUtils.repeat(" ", intendationSpaces) + getClass().getSimpleName() + additionalInformation + ":";
	}
	
}
