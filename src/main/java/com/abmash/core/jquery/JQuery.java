package com.abmash.core.jquery;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.command.*;

public class JQuery {
	
	ArrayList<Command> commands = new ArrayList<Command>();
	
	public JQuery() {
	}
	
	public JQuery(Command command) {
		addCommand(command);
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	public void addCommand(Command command) {
		this.commands.add(command);
	}
	
	public void addCommands(ArrayList<Command> commands) {
		this.commands.addAll(commands);
	}
	
	public JQuery find(String selector) {
		return find(new FindCommand(selector));
	}
	
	public JQuery find(Command command) {
		return find(new FindCommand(command.getCommand()));
	}
	
	public JQuery find(FindCommand command) {
		commands.add(command);
		return this;
	}	
	
	public JQuery filter(String selector) {
		return filter(new FilterCommand(selector));
	}
	
	public JQuery filter(Command command) {
		return filter(new FilterCommand(command.getCommand()));
	}
	
	public JQuery filter(FilterCommand command) {
		if(commands.isEmpty()) find(command.getSelector());
		commands.add(command);
		return this;
	}
	
	public JQuery not(String selector) {
		return not(new NotCommand(selector));
	}
	
	public JQuery not(Command command) {
		return not(new NotCommand(command.getCommand()));
	}
	
	public JQuery not(NotCommand command) {
		if(commands.isEmpty()) find(command.getSelector());
		commands.add(command);
		return this;
	}
	
	public JQuery add(String selector) {
		return add(new AddCommand(selector));
	}
	
	public JQuery add(Command command) {
		return add(new AddCommand(command.getCommand()));
	}
	
	public JQuery add(AddCommand command) {
		if(commands.isEmpty()) find(command.getSelector());
		commands.add(command);
		return this;
	}
	

//	public void merge(JQuery jQueryToMerge) {
//		commands.add(new JQueryCommand(jQueryToMerge));
//	}
	
	// TODO
//	public String build() {
//		String jQuery = ""; 
//		for(Command command: commands) {
//			jQuery += command.getCommand();
//		}
//		return jQuery;
//	}
	
	public String toString() {
		return toString(0);
	}	
	
	public String toString(int intendationSpaces) {
		String jQueryAsString = "\n";
		for(Command command: commands) {
			jQueryAsString += StringUtils.repeat(" ", intendationSpaces) + command.toString(intendationSpaces + 2) + "\n";
		}
		return jQueryAsString;
	}
	
}
