package com.abmash.core.jquery;

import com.abmash.api.HtmlElements;

public class JQueryFactory {
	
	public static JQuery select(String selector, double weight) {
		return new JQuery(selector, weight);
	}
	
}
