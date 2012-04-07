package com.abmash.core.jquery;

public class JQueryFactory {
	
	public static JQuery select(String selector, double weight) {
		return new JQuery(weight).find(selector);
	}

}
