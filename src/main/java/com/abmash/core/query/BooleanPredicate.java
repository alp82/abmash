package com.abmash.core.query;

import java.util.ArrayList;

import com.abmash.api.query.BooleanType;
import com.abmash.api.query.Query;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.JQueryCommandTODO;
import com.abmash.core.jquery.JQueryFactory;
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
			// TODO there is no distinction between predicate jquery's yet
			booleanCommand.addJQueryList(predicate.getJQueryList());
		}
		jQueryList.add(JQueryFactory.bool(booleanCommand));
	}
	
	@Override
	public String toString() {
		return super.toString() + " (" + type + ")";
	}
	
}
