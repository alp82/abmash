package com.abmash.core.jquery.command;

import com.abmash.core.jquery.JQuery.StringMatcher;


/**
 * Reduce the current set of matched elements to those that contain the specified text
 */
public class ContainsTextCommand extends Command {
	
	public ContainsTextCommand(StringMatcher stringMatcher, String text) {
		super("'" + stringMatcher.toString() + "','" + text + "'");
		method = "textMatch";
	}
}
