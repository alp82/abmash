package com.abmash.core.jquery.command;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.abmash.api.query.BooleanType;
import com.abmash.core.jquery.JQuery;

/**
 * TODO
 */
public class BooleanCommand extends Command {
	
	BooleanType type;
	
	ArrayList<ArrayList<JQuery>> jQueryLists;
	
	public BooleanCommand(BooleanType type) {
		super(null);
		this.type = type;
		jQueryLists = new ArrayList<ArrayList<JQuery>>();
	}
	
//	public BooleanCommand(BooleanType type, ArrayList<JQuery> jQueryList) {
//		this(type);
//		this.jQueryLists = jQueryList;
//	}

//	public void addJQuery(JQuery jQuery) {
//		jQueryLists.add(jQuery);
//	}
	
	public void addJQueryList(ArrayList<JQuery> jQueryList) {
		this.jQueryLists.add(jQueryList);
	}
	
	public BooleanType getType() {
		return type;
	}
	
	public ArrayList<ArrayList<JQuery>> getJQueryLists() {
		return jQueryLists;
	}
	
	@Override
	public boolean isBooleanCommand() {
		return true;
	}
	
	@Override
	public String toString(int intendationSpaces) {
		String jQueryListString = "";
		for(ArrayList<JQuery> jQueryList: jQueryLists) {
			for(JQuery jQuery: jQueryList) {
				jQueryListString += jQuery.toString(intendationSpaces) + ",";
			}
			jQueryListString = jQueryListString.substring(0, jQueryListString.length() - 1) + ";\n";
		}
		
		return super.toString(intendationSpaces) + " (" + type + "): " + jQueryListString;
	}
}