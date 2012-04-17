package com.abmash.core.jquery.command;


/**
 * Reduce the current set of matched elements to those that match the selector or pass the function's test.
 */
public class DistinctDescendantsCommand extends Command {
	
	public DistinctDescendantsCommand() {
		super("");
		method = "distinctDescendants";
	}
}
