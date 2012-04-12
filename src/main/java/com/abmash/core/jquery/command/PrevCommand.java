package com.abmash.core.jquery.command;


/**
 * Gets the immediately preceding sibling of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
 */
public class PrevCommand extends Command {
	
	public PrevCommand(String selector) {
		super(selector);
		method = "prev";
	}
	
}
