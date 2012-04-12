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

}
