package com.abmash.core.jquery.command;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.JQuery;

public abstract class Command {
	
	String method;
	String selector;
	
	public Command(String selector) {
		this.selector = selector;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public String getCommand() {
		String command = "";
		if(selector instanceof String) {
			if(method instanceof String) {
				command = method + "(" + selector + ")";
			} else {
				command = selector;
			}
		}
		return command;
	}
	
	/**
	 * Returns the jQuery command as string
	 * @return jQuery command string
	 */		
	public String toString() {
		return toString(2);
	}

	public String toString(int intendationSpaces) {
		return StringUtils.repeat(" ", intendationSpaces) + getClass().getSimpleName();
	}

	
}
