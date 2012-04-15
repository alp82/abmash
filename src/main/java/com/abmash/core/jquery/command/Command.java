package com.abmash.core.jquery.command;

import org.apache.commons.lang.StringUtils;

public abstract class Command {
	
	String method;
	String selector;
	
	public Command(String selector) {
		// TODO throw exception if selector empty or null
		// but not for optional selectors like .parent()
		this.selector = selector;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getSelector() {
		return selector;
	}
	
	/**
	 * Returns the jQuery command as string
	 * @return jQuery command string
	 */		
	public String toString() {
		return toString(0);
	}

	public String toString(int intendationSpaces) {
		return method + "(" + selector + ")";
	}

	
}
