package com.abmash.core.query.predicate;

import java.util.ArrayList;

public class Predicates extends ArrayList<Predicate> {

	public Predicates() {
		super();
	}
	
	public Predicates(Predicate predicate) {
		this();
		add(predicate);
	}

	public Predicates(Predicate[] predicates) {
		this();
		for(Predicate predicate: predicates) {
			add(predicate);
		}
	}

	public String toString() {
		return toString(0);
	}
	
	public String toString(int intendationSpaces) {
		String str = "";
		for(Predicate predicate: this) {
			str += "\n" + predicate.toString(intendationSpaces);
		}
		return str;
	}

}
