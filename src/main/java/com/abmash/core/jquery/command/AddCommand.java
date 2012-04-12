package com.abmash.core.jquery.command;


/**
 * Add all elements that match the selector to the set of matched elements.
 */
public class AddCommand extends Command {
	
	public AddCommand(String selector) {
		super(selector);
		method = "add";
	}
}
