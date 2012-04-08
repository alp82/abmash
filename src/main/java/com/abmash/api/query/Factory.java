package com.abmash.api.query;

import com.abmash.core.query.BooleanPredicate;
import com.abmash.core.query.LinkPredicate;
import com.abmash.core.query.Predicate;
import com.abmash.core.query.TagPredicate;

public class Factory {
	
//	public static final int CLICKABLE = 1;
	
	public static Query query(Predicate... predicates) {
		return new Query(predicates);
	}

	public static Query union(Query... queries) {
		Query unionQuery = new Query(); 
		for (Query query: queries) {
			unionQuery.union(query);
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
