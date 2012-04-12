package com.abmash.core.jquery.command;


/**
 * Remove all elements from the set of matched elements that match the selector.
 */
public class NotCommand extends Command {
	
	public NotCommand(String selector) {
		super(selector);
		method = "not";
	}
}
