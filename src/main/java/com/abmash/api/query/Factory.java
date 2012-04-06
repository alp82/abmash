package com.abmash.api.query;

import java.util.ArrayList;

import com.abmash.api.query.predicate.*;
import com.abmash.core.jquery.JQuery;

public class Factory {
	
//	public static final int CLICKABLE = 1;
	
	// Varargs implicitly means connecting predicates using Predicate.and()
	public static ArrayList<JQuery> query(Predicate... predicates) {
		return new Query(predicates).build();
	}

	public static Query union(Query... queries) {
		Query unionQuery = new Query();
		for (Query query: queries) {
			unionQuery = unionQuery.union(query);
		}
		return unionQuery;
	}

	// Prefix notation
	public static Predicate and(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.AND, predicates);
	}
	
	public static Predicate or(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.OR, predicates);
	}
	
	public static Predicate not(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.NOT, predicates);
	}
	
	public static Predicate tag(String name) {
		return new TagPredicate(name);
	}
	
	public static Predicate link(String name) {
		return new LinkPredicate(name);
	}
}
