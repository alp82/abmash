package com.abmash.core.jquery.command;


/**
 * Reduce the set of matched elements to those that have a descendant that matches the selector or element.
 */
public class HasCommand extends Command {
	
	public HasCommand(String selector) {
		super(selector);
		method = "has";
	}
	
}
