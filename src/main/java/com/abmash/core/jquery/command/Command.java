package com.abmash.core.jquery.command;

import org.apache.commons.lang.StringUtils;

public abstract class Command {
	
	String method;
	String selector;
	
	public Command(String selector) {
		// TODO throw exception if selector empty or null
		this.selector = selector;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public boolean isEvalCommand() {
		return false;
	}
	
	public boolean isBooleanCommand() {
		return false;
	}
	
	public boolean isClosenessCommand() {
		return false;
	}
	
	public boolean isColorCommand() {
		return false;
	}
	
	/**
	 * Returns the jQuery command as string
	 * @return jQuery command string
	 */		
	public String toString() {
		return toString(2);
	}

	public String toString(int intendationSpaces) {
		String commandString =  method != null ? ": ." + method + "(" + selector + ")" : "";
		return StringUtils.repeat(" ", intendationSpaces) + getClass().getSimpleName() + commandString;
	}

	
}
