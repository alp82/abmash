package com.abmash.core.jquery.command;

import java.util.ArrayList;

import com.abmash.api.query.BooleanType;

/**
 * TODO
 */
public class BooleanCommand extends Command {
	
	BooleanType type;
	
	ArrayList<Command> commands = new ArrayList<Command>();
	
	public BooleanCommand(BooleanType type) {
		super(null);
		this.type = type;
	}
	
	public void addCommand(Command command) {
		commands.add(command);
	}
	
	public void addCommands(ArrayList<Command> commands) {
		this.commands.addAll(commands);
	}
	
	public BooleanType getType() {
		return type;
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces) + "(" + type + ") with commands " + commands;
	}
}
