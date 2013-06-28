package com.abmash.core.query.predicate;


import com.abmash.core.jquery.JQueryFactory;
import com.abmash.core.query.DirectionOptions;

public class DirectionPredicate extends JQueryPredicate {
	
	DirectionOptions options;
	Predicates predicates;

	public DirectionPredicate(DirectionOptions options, Predicate... predicates) {
		super();
		this.options = options;
		this.predicates = new Predicates(predicates);
		buildCommands();
	}
	
	public DirectionOptions getOptions() {
		return options;
	}
	
	public Predicates getPredicates() {
		return predicates;
	}
	
	public void buildCommands() {
		closeTo(
			// TODO bei query nach hinten sortieren
			JQueryFactory.select("abmash.getData('elementsForFilteringQuery')", 50),
			options,
			predicates
		);
	};
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int intendationSpaces) {
		return super.toString(intendationSpaces, " " + options.buildCommandSelector());
	}
	
}
