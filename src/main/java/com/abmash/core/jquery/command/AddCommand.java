package com.abmash.core.jquery.command;

import com.abmash.core.jquery.JQuery;

/**
 * Add all elements that match the selector to the set of matched elements.
 */
public class AddCommand extends SelectCommand {
	
	public AddCommand(String selector) {
		super(selector);
		method = "add";
	}
}
