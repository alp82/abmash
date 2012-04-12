package com.abmash.core.jquery.command;


/**
 * Gets siblings of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
 */
public class NextCommand extends Command {
	
	public NextCommand(String selector) {
		super(selector);
		method = "next";
	}
	
}
