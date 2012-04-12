package com.abmash.api.query;

import java.awt.Color;

import com.abmash.api.HtmlQuery;
import com.abmash.core.color.ColorName;
import com.abmash.core.color.Dominance;
import com.abmash.core.color.Tolerance;
import com.abmash.core.query.BooleanType;
import com.abmash.core.query.DirectionType;
import com.abmash.core.query.predicate.BooleanPredicate;
import com.abmash.core.query.predicate.ClickablePredicate;
import com.abmash.core.query.predicate.ColorPredicate;
import com.abmash.core.query.predicate.DirectionPredicate;
import com.abmash.core.query.predicate.ContainsPredicate;
import com.abmash.core.query.predicate.HeadlinePredicate;
import com.abmash.core.query.predicate.LinkPredicate;
import com.abmash.core.query.predicate.Predicate;
import com.abmash.core.query.predicate.TagPredicate;
import com.abmash.core.query.predicate.TextPredicate;
import com.abmash.core.query.predicate.TypablePredicate;

public class QueryFactory {
	
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

	// Boolean Predicates
	
	public static Predicate and(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.AND, predicates);
	}
	
	public static Predicate or(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.OR, predicates);
	}
	
	public static Predicate not(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.NOT, predicates);
	}
	
	// JQuery Predicates

	public static Predicate contains(String text) {
		return new ContainsPredicate(text);
	}
	
	public static Predicate tag(String name) {
		return new TagPredicate(name);
	}
	
	public static Predicate text(String text) {
		return new TextPredicate(text);
	}
	
	public static Predicate headline() {
		return headline(null);
	}
	
	public static Predicate headline(String text) {
		return new HeadlinePredicate(text);
	}
	
	public static Predicate link() {
		return link(null);
	}
	
	public static Predicate link(String text) {
		return new LinkPredicate(text);
	}
	
	public static Predicate clickable() {
		return clickable(null);
	}
	
	public static Predicate clickable(String text) {
		return new ClickablePredicate(text);
	}
	
	public static Predicate typable() {
		return typable(null);
	}
	
	public static Predicate typable(String text) {
		return new TypablePredicate(text);
	}
	
	// Direction Predicates
	
	/**
	 * Finds elements <strong>above</strong> the elements which are matching the predicates.
	 * <p>
	 * Elements are above if their bottom y coordinate is lower than the top y coordinate of the reference element.
	 * In addition, the element has to be in horizontal bounds of the reference element.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *              ----------
	 *              - <span> -
	 *              ----------
	 *        ---------
	 *        - <div> -
	 *        ---------
	 *   --------
	 *   - <ul> -
	 *   --------
	 * }</pre>
	 * In this example, the {@code <div>} is above the {@code <ul>}, but the {@code <span>} is not because its left border is not
	 * in horizontal bounds of {@code <ul>}. Then again, the {@code <span>} is above the {@code <div>}. 
	 * <p>
	 * Calling <code>find()</code> will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return Predicate with specified direction
	 */
	public static Predicate above(Predicate... predicates) {
		return new DirectionPredicate(DirectionType.ABOVE, 0, predicates);
	}
	
	public static Predicate below(Predicate... predicates) {
		return new DirectionPredicate(DirectionType.BELOW, 0, predicates);
	}	

	public static Predicate leftOf(Predicate... predicates) {
		return new DirectionPredicate(DirectionType.LEFTOF, 0, predicates);
	}	
	
	public static Predicate rightOf(Predicate... predicates) {
		return new DirectionPredicate(DirectionType.RIGHTOF, 0, predicates);
	}
	
	// color predicates
	
	public static Predicate color(ColorName colorName, Tolerance tolerance, Dominance dominance) {
		return color(colorName.getColor(), tolerance.getValue(), dominance.getValue());
	}
	
	public static Predicate color(ColorName colorName, double tolerance, double dominance) {
		return color(colorName.getColor(), tolerance, dominance);
	}
	
	public static Predicate color(Color color, Tolerance tolerance, Dominance dominance) {
		return color(color, tolerance.getValue(), dominance.getValue());
	}
	
	public static Predicate color(Color color, double tolerance, double dominance) {
		return new ColorPredicate(color, tolerance, dominance);
	}
}
