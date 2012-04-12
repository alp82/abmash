package com.abmash.core.jquery.command;


/**
 * Reduce the current set of matched elements to those that match the regex selector.
 */
public class RegExCommand extends Command {
	
	public RegExCommand(String selector) {
		super(selector);
		method = "regex";
	}
}
