package com.abmash.core.jquery.command;


/**
 * Gets the immediate parents of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
 */
public class ParentCommand extends Command {
	
	public ParentCommand(String selector) {
		super(selector);
		method = "parent";
	}
	
}
