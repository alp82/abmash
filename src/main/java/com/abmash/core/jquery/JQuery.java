package com.abmash.core.jquery;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;

import com.abmash.REMOVE.core.htmlquery.condition.ColorCondition;
import com.abmash.core.jquery.command.*;
import com.abmash.core.jquery.command.FilterCSSCommand.CSSAttributeComparator;
import com.abmash.core.query.ColorOptions;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;
import com.abmash.core.query.predicate.Predicate;
import com.abmash.core.query.predicate.Predicates;

public class JQuery {
	
	public enum StringMatcher {
		EXACT, WORD, STARTSWITH, ENDSWITH, CONTAINS, EXISTS
	}
	
	ArrayList<Command> commands = new ArrayList<Command>();

	String selector;
	
	Double weight;

	public JQuery(String selector, Double weight) {
		this.selector = selector != null && !selector.equals("") ? selector : "'*:not(html,head,head *)'";
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
	
	public JQuery contents() {
		commands.add(new ContentsCommand());
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
	
	public JQuery distinctDescendants() {
		commands.add(new DistinctDescendantsCommand());
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
	
	// closeness and direction commands

	public JQuery closeTo(DirectionOptions options, Predicates predicates) {
		commands.add(new CloseToCommand(options, predicates));
		return this;
	}
	
	public JQuery closeToLabel(DirectionOptions options, Predicates predicates) {
		return closeTo(options.setType(DirectionType.CLOSETOLABEL), predicates);
	}	
	
	public JQuery closeToClickableLabel(DirectionOptions options, Predicates predicates) {
		return closeTo(options.setType(DirectionType.CLOSETOCLICKABLELABEL), predicates);
	}	
	
	public JQuery above(DirectionOptions options, Predicates predicates) {
		return closeTo(options.setType(DirectionType.ABOVE), predicates);
	}	
	
	public JQuery below(DirectionOptions options, Predicates predicates) {
		return closeTo(options.setType(DirectionType.BELOW), predicates);
	}	
	
	public JQuery leftOf(DirectionOptions options, Predicates predicates) {
		return closeTo(options.setType(DirectionType.LEFTOF), predicates);
	}	
	
	public JQuery rightOf(DirectionOptions options, Predicates predicates) {
		return closeTo(options.setType(DirectionType.RIGHTOF), predicates);
	}	
	
	// color commands
	
	public JQuery color(ColorOptions options) {
		commands.add(new ColorCommand(options));
		return this;
	}

	public JQuery color(Color color, double tolerance, double dominance) {
		return color(new ColorOptions(color, tolerance, dominance));
	}
	
	// general methods
	
	public JQuery setWeight(double weight) {
		this.weight = weight;
		return this;
	}
	
	public String toString() {
		return toString(0);
	}	
	
	public String toString(int intendationSpaces) {
		String str = StringUtils.repeat(" ", intendationSpaces) + weight + ":" + "jQuery(" + selector + ")";
		for(Command command: commands) {
			str += "." + command.toString(intendationSpaces);
		}
		return str;
	}
	
	public JQuery clone() {
		return clone(weight);
	}
	
	public JQuery clone(double newWeight) {
		JQuery jQuery = new JQuery(selector, newWeight);
		for(Command command: commands) {
			jQuery.addCommand(command);
		}
		return jQuery;
	}
}
