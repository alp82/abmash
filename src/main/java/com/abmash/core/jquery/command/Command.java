package com.abmash.core.jquery.command;

import org.apache.commons.lang.StringUtils;

public abstract class Command {
	
	String method;
	String selector;
	double weight;
	
	public Command(String selector, Double weight) {
		this.selector = selector;
		this.weight = weight != null ? weight : 1;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public double getWeight() {
		return weight;
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
		return StringUtils.repeat(" ", intendationSpaces) + getClass().getSimpleName();
	}

	
}
