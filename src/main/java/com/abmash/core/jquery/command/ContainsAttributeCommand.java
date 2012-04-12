package com.abmash.core.jquery.command;

import com.abmash.core.jquery.JQuery.StringMatcher;


/**
 * Reduce the current set of matched elements to those that contain the specified attribute value
 */
public class ContainsAttributeCommand extends Command {
	
	public ContainsAttributeCommand(StringMatcher stringMatcher, String attributeName, String text) {
		super("'" + stringMatcher.toString() + "','" + attributeName + "','" + text + "'");
		method = "attrMatch";
	}
}
