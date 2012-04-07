package com.abmash.core.jquery.command;


/**
 * Reduce the current set of matched elements to those that match the selector or pass the function's test.
 */
public class SelectCommand extends Command {
	
	public SelectCommand(String selector) {
		super(selector, null);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces) + " with selector " + selector;
	}
}
