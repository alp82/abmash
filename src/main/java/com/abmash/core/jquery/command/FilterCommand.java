package com.abmash.core.jquery.command;

import com.abmash.core.jquery.JQuery;

/**
 * Reduce the current set of matched elements to those that match the selector or pass the function's test.
 */
public class FilterCommand extends SelectCommand {
	
	public FilterCommand(String selector) {
		super(selector);
		method = "filter";
	}
}
