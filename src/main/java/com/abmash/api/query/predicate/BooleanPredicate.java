package com.abmash.api.query.predicate;

import java.util.ArrayList;

import com.abmash.api.query.BooleanType;
import com.abmash.api.query.Query;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryCommandTODO;
import com.abmash.core.jquery.command.BooleanCommand;
import com.abmash.core.jquery.command.Command;

public class BooleanPredicate extends Predicate {
	
	BooleanType type;
	ArrayList<Predicate> predicates = new ArrayList<Predicate>();

	public BooleanPredicate(BooleanType type, Predicate... predicates) {
		this.type = type;
		addPredicates(predicates);
	}
	
	public Predicate addPredicates(Predicate... predicates) {
		if(predicates instanceof Predicate[]) {
			for(Predicate predicate: predicates) {
				this.predicates.add(predicate);
			}
		}
		return this;
	}

	public BooleanType getType() {
		return type;
	}
	
	public ArrayList<Predicate> getPredicates() {
		return predicates;
	}

	@Override
	public void buildCommands() {
		BooleanCommand booleanCommand = new BooleanCommand(type);
		for(Predicate predicate: predicates) {
			predicate.buildCommands();
			for(JQuery jQuery: predicate.getJQueryList()) {
				booleanCommand.addCommands(jQuery.getCommands());
			}
		}
		jQueryList.add(new JQuery(booleanCommand));
	}
	
	@Override
	public String toString() {
		return type + " " + super.toString();
	}
	
}
