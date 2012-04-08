package com.abmash.core.jquery;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.command.*;

public class JQuery {
	
	ArrayList<Command> commands = new ArrayList<Command>();

	String selector;
	
	Double weight;

	public JQuery(String selector, Double weight) {
		this.selector = selector != null ? selector : "'*'";
		this.weight = weight != null ? weight : 1;
	}
	
//	public JQuery(Command command) {
//		this(null, null);
//		addCommand(command);
//	}
	
	public JQuery addCommand(Command command) {
		commands.add(command);
		return this;
	}
	
	public JQuery addCommands(ArrayList<Command> commands) {
		this.commands.addAll(commands);
		return this;
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	public double getWeight() {
		return weight;
	}

	public JQuery find(String selector) {
		return find(new FindCommand(selector));
	}
	
	public JQuery find(Command command) {
		return find(new FindCommand(command.getSelector()));
	}
	
	public JQuery find(FindCommand command) {
		commands.add(command);
		return this;
	}	
	
	public JQuery filter(String selector) {
		return filter(new FilterCommand(selector));
	}
	
	public JQuery filter(Command command) {
		return filter(new FilterCommand(command.getSelector()));
	}
	
	public JQuery filter(FilterCommand command) {
//		if(commands.isEmpty()) return find(command.getSelector());
		commands.add(command);
		return this;
	}
	
	public JQuery not(String selector) {
		return not(new NotCommand(selector));
	}
	
	public JQuery not(Command command) {
		return not(new NotCommand(command.getSelector()));
	}
	
	public JQuery not(NotCommand command) {
//		if(commands.isEmpty()) return find(command.getSelector());
		commands.add(command);
		return this;
	}
	
	public JQuery add(String selector) {
		return add(new AddCommand(selector));
	}
	
	public JQuery add(Command command) {
		return add(new AddCommand(command.getSelector()));
	}
	
	public JQuery add(AddCommand command) {
//		if(commands.isEmpty()) return find(command.getSelector());
		commands.add(command);
		return this;
	}
	
	public JQuery contains(String text) {
		return contains(text, false);
	}

	public JQuery contains(String text, boolean caseSensitive) {
		String containsSelector = caseSensitive ? "contains" : "containsCaseInsensitive";
		return filter("':" + containsSelector + "(" + text + ")'");
	}
	
	public JQuery containsAttribute(String text) {
		String containsSelector = "attrCaseInsensitive";
		return filter("':" + containsSelector + "(" + text + ")'");
	}
	
//	public void merge(JQuery jQueryToMerge) {
//		commands.add(new JQueryCommand(jQueryToMerge));
//	}
	
//	public String build() {
//		String jQuery = ""; 
//		for(Command command: commands) {
//			String selector = command.getSelector();
//			String method = command.getMethod();
//			if(selector instanceof String && method instanceof String) {
//				jQuery += "." + method + "(" + selector + ")";
//			}
//		}
//		return jQuery;
//	}
	
	public String getSelector() {
		return selector;
	}
	
	public String toString() {
		return toString(0);
	}	
	
	public String toString(int intendationSpaces) {
		String jQueryAsString = "\n" + StringUtils.repeat(" ", intendationSpaces + 2);
		jQueryAsString += !selector.equals("'*'") || !(commands.size() == 1 && commands.get(0).isBooleanCommand())  ? "jQuery(" + selector + ")\n" : "\n";
		for(Command command: commands) {
			jQueryAsString += StringUtils.repeat(" ", intendationSpaces) + command.toString(intendationSpaces + 2) + "\n";
		}
		return jQueryAsString.substring(0, jQueryAsString.length() - 1);
	}
}
