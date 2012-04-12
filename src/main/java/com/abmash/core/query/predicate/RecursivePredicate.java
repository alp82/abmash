package com.abmash.core.query.predicate;

import java.util.ArrayList;

public abstract class RecursivePredicate extends Predicate {
	
	Predicates predicates = new Predicates();

	public RecursivePredicate(Predicate... predicates) {
		addPredicates(predicates);
	}
	
	public Predicate addPredicates(Predicate... predicates) {
		if(predicates instanceof Predicate[]) {
			for(Predicate predicate: predicates) {
				this.predicates.add(predicate);
			}
		}
		return this;
	}
	
	public Predicates getPredicates() {
		return predicates;
	}
	
	@Override
	public String toString(int intendationSpaces, String additionalInformation) {
		String str = super.toString(intendationSpaces, additionalInformation);
		for(Predicate predicate: predicates) {
			str += "\n" + predicate.toString(intendationSpaces + 2);
		}
		return str;
	}
}
