package com.abmash.core.jquery.command;


/**
 * Finds children of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
 */
public class ChildrenCommand extends Command {
	
	public ChildrenCommand(String selector) {
		super(selector);
		method = "children";
	}
	
}
