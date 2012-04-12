package com.abmash.core.jquery;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class JQueryList extends ArrayList<JQuery> {
	
	public String toString() {
		return toString(0);
	}
	
	public String toString(int intendationSpaces) {
		String str = StringUtils.repeat(" ", intendationSpaces) + "JQueryList:";
		for(JQuery jQuery: this) {
			str += "\n" + jQuery.toString(intendationSpaces + 2);
		}
		return str;
	};
}
