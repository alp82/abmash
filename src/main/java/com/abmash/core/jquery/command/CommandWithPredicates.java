package com.abmash.core.jquery.command;

import com.abmash.core.query.predicate.Predicates;


/**
 * Finds children of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
 */
public abstract class CommandWithPredicates extends Command {
	
	Predicates predicates;
	
	public CommandWithPredicates(String selector, Predicates predicates) {
		super(selector);
		this.predicates = predicates;
	}
	
	public Predicates getPredicates() {
		return predicates;
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces) + predicates.toString(intendationSpaces + 2);
	}
	
}
