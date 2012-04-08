package com.abmash.core.jquery;

import com.abmash.core.jquery.command.BooleanCommand;

public class JQueryFactory {
	
	public static JQuery select(String selector, double weight) {
		return new JQuery(selector, weight);
	}
	
	public static JQuery bool(BooleanCommand booleanCommand) {
		return new JQuery(null, null).addCommand(booleanCommand);
	}
}
