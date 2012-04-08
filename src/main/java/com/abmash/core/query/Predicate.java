package com.abmash.core.query;

import java.util.ArrayList;

import com.abmash.api.query.BooleanType;
import com.abmash.core.jquery.JQuery;

public abstract class Predicate {

//	protected ArrayList<Expression> expressions = new ArrayList<Expression>();
//	public abstract ArrayList<Expression> buildExpressions();
	
	protected ArrayList<JQuery> jQueryList = new ArrayList<JQuery>();
	
	public abstract void buildCommands();
	
	public ArrayList<JQuery> getJQueryList() {
		return jQueryList;
	}
	
	// Infix notation (usually a lot more readable than the prefix-notation)
	public Predicate and(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.AND, this).addPredicates(predicates);
	}

	public Predicate or(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.OR, this).addPredicates(predicates);
	}

	public Predicate not(Predicate... predicates) {
		return and(new BooleanPredicate(BooleanType.NOT, predicates));
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " with jQueryList " + jQueryList;
	}

}
