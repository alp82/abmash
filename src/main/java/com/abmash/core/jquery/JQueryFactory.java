package com.abmash.core.jquery;

public class JQueryFactory {
	
	public static JQuery select(String selector) {
		return new JQuery().find(selector);
	}

}
