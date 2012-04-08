package com.abmash.core.jquery.command;

import com.abmash.core.jquery.JQuery;

/**
 * TODO
 */
public class JQueryCommand extends Command {
	
	private JQuery jQuery;
	
	public JQueryCommand(JQuery jQuery) {
		super(null);
		this.jQuery = jQuery;
	}
	
	public JQuery getjQuery() {
		return jQuery;
	}
	
	// TODO
//	public String getCommand() {
//		ArrayList<Command> commands = jQuery.getCommands();
//		String cmd = "jQuery()";
//		for(Command command: commands) {
//			cmd += "." + command.getCommand();
//		}
//		return cmd;
//	}
}
