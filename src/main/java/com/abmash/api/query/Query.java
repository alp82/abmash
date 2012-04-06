package com.abmash.api.query;

import java.util.ArrayList;

import com.abmash.api.query.predicate.BooleanPredicate;
import com.abmash.api.query.predicate.Predicate;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryCommandTODO;
import com.abmash.core.jquery.command.Command;

public class Query {

	private Predicate[] predicates;
	private ArrayList<JQuery> jQueryList;
	
	public Query(Predicate... predicates) {
		this.predicates = predicates;
		jQueryList = new ArrayList<JQuery>();
	}

	// TODO
	public Query union(Query... queries) {
		return null;
	}
	
	public ArrayList<JQuery> build() {
		ArrayList<String> selectors = new ArrayList<String>();
		for(Predicate predicate: predicates) {
			predicate.buildCommands();
//			ArrayList<Command> predicateCommands;
//			if(predicate instanceof BooleanPredicate) {
////				Predicate[] queryPredicates = new Predicate[((BooleanPredicate) predicate).getPredicates().size()];
////				Query query = new Query(((BooleanPredicate) predicate).getPredicates().toArray(queryPredicates));
////				predicateCommands = query.build().getCommands();
//
//				switch(((BooleanPredicate) predicate).getType()) {
//				case OR:
//					JQuery jQuery = predicate.getJQuery();
//					for(Command command: predicateCommands) {
//						System.out.println(command);
//						jQuery.add(command);
//					}
//					jQueryList.add(jQuery);
//					break;
//					
//				case NOT:
//					for(Command command: predicateCommands) {
//						jQuery.not(command);
//					}
//					break;
//					
//				case AND:
//				default:
//					jQuery.add(predicateCommands.remove(0));
//					for(Command command: predicateCommands) {
//						jQuery.filter(command);
//					}
//					break;
//				}
//			} else {
//			}
			JQuery jQuery = new JQuery();
			for(JQuery predicateJQuery: predicate.getJQueryList()) {
				for(Command command: predicateJQuery.getCommands()) {
					jQuery.addCommand(command);
				}
			}
			jQueryList.add(jQuery);
		}
		
		return jQueryList;
	}
	
}
