package com.abmash.core.jquery;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.abmash.core.jquery.command.*;
import com.abmash.core.jquery.command.FilterCSSCommand.CSSAttributeComparator;
import com.abmash.core.query.DirectionType;

public class JQuery {
	
	public enum StringMatcher {
		EXACT, WORD, STARTSWITH, ENDSWITH, CONTAINS, EXISTS
	}
	
	ArrayList<Command> commands = new ArrayList<Command>();

	String selector;
	
	Double weight;

	public JQuery(String selector, Double weight) {
		this.selector = selector != null ? selector : "'*'";
		this.weight = weight != null ? weight : 1;
	}
	
	public JQuery addCommand(Command command) {
		commands.add(command);
		return this;
	}
	
	public JQuery addCommands(ArrayList<Command> commands) {
		this.commands.addAll(commands);
		return this;
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public double getWeight() {
		return weight;
	}
	
	// main commands

	public JQuery find(String selector) {
		return find(new FindCommand(selector));
	}
	
	public JQuery find(FindCommand command) {
		commands.add(command);
		return this;
	}	
	
	public JQuery filter(String selector) {
		return filter(new FilterCommand(selector));
	}
	
	public JQuery filter(FilterCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery not(String selector) {
		return not(new NotCommand(selector));
	}
	
	public JQuery not(NotCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery add(String selector) {
		return add(new AddCommand(selector));
	}
	
	public JQuery add(AddCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery containsText(StringMatcher stringMatcher, String text) {
		return containsText(new ContainsTextCommand(stringMatcher, text));
	}
	
	public JQuery containsText(ContainsTextCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery containsAttribute(StringMatcher stringMatcher, String attributeName, String text) {
		return containsAttribute(new ContainsAttributeCommand(stringMatcher, attributeName, text));
	}
	
	public JQuery containsAttribute(ContainsAttributeCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery has(String selector) {
		return has(new HasCommand(selector));
	}
	
	public JQuery has(HasCommand command) {
		commands.add(command);
		return this;
	}
	
	// extended selector functionality
	
	public JQuery xPath(String selector) {
		return xPath(new XPathCommand(selector));
	}
	
	public JQuery xPath(XPathCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery regex(String selector) {
		return regex(new RegExCommand(selector));
	}
	
	public JQuery regex(RegExCommand command) {
		commands.add(command);
		return this;
	}

	public JQuery filterCSS(String attributeName, CSSAttributeComparator cssAttributeComparator, String value) {
		commands.add(new FilterCSSCommand(attributeName, cssAttributeComparator, value));
		return this;
	}
	
	// commands with optional selectors
	
	public JQuery parent() {
		return parent("");
	}
	
	public JQuery parent(String selector) {
		return parent(new ParentCommand(selector));
	}
	
	public JQuery parent(ParentCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery ancestors() {
		return ancestors("");
	}

	public JQuery ancestors(String selector) {
		return ancestors(new AncestorsCommand(selector));
	}
	
	public JQuery ancestors(AncestorsCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery children() {
		return children("");
	}
	
	public JQuery children(String selector) {
		return children(new ChildrenCommand(selector));
	}
	
	public JQuery children(ChildrenCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery siblings() {
		return siblings("");
	}
	
	public JQuery siblings(String selector) {
		return siblings(new SiblingsCommand(selector));
	}
	
	public JQuery siblings(SiblingsCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery next() {
		return next("");
	}
	
	public JQuery next(String selector) {
		return next(new NextCommand(selector));
	}
	
	public JQuery next(NextCommand command) {
		commands.add(command);
		return this;
	}
	
	public JQuery prev() {
		return prev("");
	}
	
	public JQuery prev(String selector) {
		return prev(new PrevCommand(selector));
	}
	
	public JQuery prev(PrevCommand command) {
		commands.add(command);
		return this;
	}
	
	// closeness commands
	
//	public JQuery above(String selector) {
//		commands.add(new ClosenessCommand(ClosenessType.ABOVE));
//		return this;
//	}	
//	
//	public JQuery below(String selector) {
//		commands.add(new ClosenessCommand(ClosenessType.BELOW));
//		return this;
//	}	
	
	// general methods
	
	public String toString() {
		return toString(0);
	}	
	
	public String toString(int intendationSpaces) {
		String str = StringUtils.repeat(" ", intendationSpaces) + "jQuery(" + selector + ")";
		for(Command command: commands) {
			str += "." + command.toString();
		}
		return str;
	}

	public JQuery clone(double newWeight) {
		JQuery jQuery = new JQuery(selector, newWeight);
		for(Command command: commands) {
			jQuery.addCommand(command);
		}
		return jQuery;
	}
}
