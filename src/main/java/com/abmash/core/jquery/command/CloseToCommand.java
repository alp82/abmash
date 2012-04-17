package com.abmash.core.jquery.command;

import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.predicate.Predicates;


/**
 * Finds elements close to the elements that match the given predicates. Optionally filtered by direction and distance.
 */
public class CloseToCommand extends CommandWithPredicates {
	
	public CloseToCommand(DirectionOptions options, Predicates predicates) {
		super(options.buildCommandSelector(), predicates);
		method = "closeTo";
	}
	
}
