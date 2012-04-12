package com.abmash.core.jquery.command;


/**
 * Find descendants of the current set of matched elements that match the selector.
 */
public class FindCommand extends Command {
	
	public FindCommand(String selector) {
		super(selector);
		method = "find";
	}
	
}
