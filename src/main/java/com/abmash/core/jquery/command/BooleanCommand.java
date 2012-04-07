package com.abmash.core.jquery.command;

import java.util.ArrayList;

import com.abmash.api.query.BooleanType;
import com.abmash.core.jquery.JQuery;

/**
 * TODO
 */
public class BooleanCommand extends Command {
	
	BooleanType type;
	
	ArrayList<JQuery> jQueryList;
	
	public BooleanCommand(BooleanType type) {
		super(null, null);
		this.type = type;
		jQueryList = new ArrayList<JQuery>();
	}
	
	public BooleanCommand(BooleanType type, ArrayList<JQuery> jQueryList) {
		this(type);
		this.jQueryList = jQueryList;
	}

	public void addJQuery(JQuery jQuery) {
		jQueryList.add(jQuery);
	}
	
	public void addJQueryList(ArrayList<JQuery> jQueryList) {
		this.jQueryList.addAll(jQueryList);
	}
	
	public BooleanType getType() {
		return type;
	}
	
	public ArrayList<JQuery> getJQueryList() {
		return jQueryList;
	}
	
	@Override
	public boolean isBooleanCommand() {
		return true;
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces) + "(" + type + ") with jQueryList " + jQueryList;
	}
}