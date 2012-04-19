package com.abmash.core.jquery.command;

import com.abmash.core.query.ColorOptions;


/**
 * Finds elements that match the given color.
 */
public class ColorCommand extends Command {
	
	public ColorCommand(ColorOptions options) {
		super(options.buildCommandSelector());
		method = "filterHasColor";
	}
	
}
