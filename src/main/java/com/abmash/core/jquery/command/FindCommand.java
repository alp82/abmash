package com.abmash.core.jquery.command;

import com.abmash.core.jquery.JQuery;

/**
 * Find descendants of the current set of matched elements that match the selector.
 */
public class FindCommand extends SelectCommand {
	
	public FindCommand(String selector) {
		super(selector);
		method = "find";
	}
	
}
