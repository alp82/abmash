package com.abmash.core.jquery.command;


/**
 * Gets the ancestors of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
 */
public class AncestorsCommand extends Command {
	
	public AncestorsCommand(String selector) {
		super(selector);
		method = "parents";
	}
	
}
