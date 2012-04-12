package com.abmash.core.jquery.command;


/**
 * Reduce the current set of matched elements to those that match the selector or pass the function's test.
 */
public class FilterCommand extends Command {
	
	public FilterCommand(String selector) {
		super(selector);
		method = "filter";
	}
}
