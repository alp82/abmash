package com.abmash.core.jquery.command;


/**
 * Reduce the current set of matched elements to those that match the xpath selector.
 */
public class XPathCommand extends Command {
	
	public XPathCommand(String selector) {
		super("'" + selector + "'");
		method = "xpath";
	}
}
