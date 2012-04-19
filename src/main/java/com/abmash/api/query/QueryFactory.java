package com.abmash.api.query;

import java.awt.Color;
import java.util.ArrayList;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.api.HtmlQuery;
import com.abmash.core.color.ColorName;
import com.abmash.core.color.Dominance;
import com.abmash.core.color.Tolerance;
import com.abmash.core.query.BooleanType;
import com.abmash.core.query.DirectionOptions;
import com.abmash.core.query.DirectionType;
import com.abmash.core.query.predicate.BooleanPredicate;
import com.abmash.core.query.predicate.CheckablePredicate;
import com.abmash.core.query.predicate.ChoosablePredicate;
import com.abmash.core.query.predicate.ClickablePredicate;
import com.abmash.core.query.predicate.ColorPredicate;
import com.abmash.core.query.predicate.DatepickerPredicate;
import com.abmash.core.query.predicate.DirectionPredicate;
import com.abmash.core.query.predicate.ContainsPredicate;
import com.abmash.core.query.predicate.ElementPredicate;
import com.abmash.core.query.predicate.FramePredicate;
import com.abmash.core.query.predicate.HeadlinePredicate;
import com.abmash.core.query.predicate.ImagePredicate;
import com.abmash.core.query.predicate.LinkPredicate;
import com.abmash.core.query.predicate.Predicate;
import com.abmash.core.query.predicate.SelectPredicate;
import com.abmash.core.query.predicate.SubmittablePredicate;
import com.abmash.core.query.predicate.TextPredicate;
import com.abmash.core.query.predicate.TypablePredicate;

public class QueryFactory {
	
	public static Query query(Browser browser, Predicate... predicates) {
		return new Query(browser, predicates);
	}
	
	public static Query union(Query... queries) {
		Query unionQuery = null;
		for (Query query: queries) {
			if(unionQuery == null) {
				unionQuery = query;
			} else {
				unionQuery.union(query);
			}
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
	
	// Direct Element Predicates
	public static Predicate elements(HtmlElements elements) {
		return new ElementPredicate(elements);
	}
	
	// JQuery Predicates
	
	public static Predicate select(String name) {
		return new SelectPredicate(name);
	}
	
	public static Predicate contains(String text) {
		return new ContainsPredicate(text);
	}
	
	public static Predicate text(String text) {
		return new TextPredicate(text);
	}
	
	public static Predicate text() {
		return text(null);
	}
	
	public static Predicate headline(String text) {
		return new HeadlinePredicate(text);
	}
	
	public static Predicate headline() {
		return headline(null);
	}
	
	public static Predicate link() {
		return link(null);
	}
	
	public static Predicate link(String text) {
		return new LinkPredicate(text);
	}
	
	public static Predicate image() {
		return image(null);
	}
	
	public static Predicate image(String text) {
		return new ImagePredicate(text);
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
	
	public static Predicate choosable() {
		return choosable(null);
	}
	
	public static Predicate choosable(String text) {
		return new ChoosablePredicate(text);
	}
	
	public static Predicate checkable() {
		return checkable(null);
	}
	
	public static Predicate checkable(String text) {
		return new CheckablePredicate(text);
	}
	
	public static Predicate datepicker() {
		return datepicker(null);
	}
	
	public static Predicate datepicker(String text) {
		return new DatepickerPredicate(text);
	}
	
	public static Predicate submittable() {
		return submittable(null);
	}
	
	public static Predicate submittable(String text) {
		return new SubmittablePredicate(text);
	}
		
	public static Predicate frame(String text) {
		return new FramePredicate(text);
	}
	
	// Direction Predicates
	
	public static Predicate closeTo(DirectionOptions options, Predicate... predicates) {
		return new DirectionPredicate(options, predicates);
	}
	
	public static Predicate closeTo(DirectionOptions options, HtmlElements elements) {
		return closeTo(options, new ElementPredicate(elements));
	}
	
	public static Predicate closeTo(DirectionOptions options, HtmlElement element) {
		return closeTo(options, new ElementPredicate(new HtmlElements(element)));
	}
	
	public static Predicate closeTo(int maxDistance, Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.CLOSETO).setMaxDistance(maxDistance), predicates);
	}
	
	public static Predicate closeTo(int maxDistance, HtmlElements elements) {
		return closeTo(maxDistance, new ElementPredicate(elements));
	}
	
	public static Predicate closeTo(int maxDistance, HtmlElement element) {
		return closeTo(maxDistance, new HtmlElements(element));
	}
	
	public static Predicate closeTo(Predicate... predicates) {
		return closeTo(300, predicates);
	}
	
	public static Predicate closeTo(HtmlElements elements) {
		return closeTo(new ElementPredicate(elements));
	}
	
	public static Predicate closeTo(HtmlElement element) {
		return closeTo(new HtmlElements(element));
	}
	
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
		return closeTo(new DirectionOptions(DirectionType.ABOVE), predicates);
	}
	
	public static Predicate above(HtmlElements elements) {
		return above(new ElementPredicate(elements));
	}
	
	public static Predicate above(HtmlElement element) {
		return above(new HtmlElements(element));
	}
	
	public static Predicate below(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.BELOW), predicates);
	}	
	
	public static Predicate below(HtmlElements elements) {
		return below(new ElementPredicate(elements));
	}
	
	public static Predicate below(HtmlElement element) {
		return below(new HtmlElements(element));
	}
	
	public static Predicate leftOf(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.LEFTOF), predicates);
	}	
	
	public static Predicate leftOf(HtmlElements elements) {
		return leftOf(new ElementPredicate(elements));
	}
	
	public static Predicate leftOf(HtmlElement element) {
		return leftOf(new HtmlElements(element));
	}
	
	public static Predicate rightOf(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.RIGHTOF), predicates);
	}
	
	public static Predicate rightOf(HtmlElements elements) {
		return rightOf(new ElementPredicate(elements));
	}
	
	public static Predicate rightOf(HtmlElement element) {
		return rightOf(new HtmlElements(element));
	}
	
	public static Predicate aboveAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.ABOVE).setDirectionHasToMatchAllTargets(true), predicates);
	}
	
	public static Predicate aboveAll(HtmlElements elements) {
		return aboveAll(new ElementPredicate(elements));
	}
	
	public static Predicate aboveAll(HtmlElement element) {
		return aboveAll(new HtmlElements(element));
	}
	
	public static Predicate belowAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.BELOW).setDirectionHasToMatchAllTargets(true), predicates);
	}	
	
	public static Predicate belowAll(HtmlElements elements) {
		return belowAll(new ElementPredicate(elements));
	}
	
	public static Predicate belowAll(HtmlElement element) {
		return belowAll(new HtmlElements(element));
	}

	public static Predicate leftOfAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.LEFTOF).setDirectionHasToMatchAllTargets(true), predicates);
	}
	
	public static Predicate leftOfAll(HtmlElements elements) {
		return leftOfAll(new ElementPredicate(elements));
	}
	
	public static Predicate leftOfAll(HtmlElement element) {
		return leftOfAll(new HtmlElements(element));
	}
	
	public static Predicate rightOfAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.RIGHTOF).setDirectionHasToMatchAllTargets(true), predicates);
	}
	
	public static Predicate rightOfAll(HtmlElements elements) {
		return rightOfAll(new ElementPredicate(elements));
	}
	
	public static Predicate rightOfAll(HtmlElement element) {
		return rightOfAll(new HtmlElements(element));
	}

	// color predicates
	
	/**
	 * Filters elements that match the desired color. Performs slow if no other predicates are used.
	 * 
	 * @param color
	 * @param tolerance
	 * @param dominance
	 * @return
	 */
	public static Predicate color(Color color, double tolerance, double dominance) {
		return new ColorPredicate(color, tolerance, dominance);
	}
	
	public static Predicate color(Color color, Tolerance tolerance, Dominance dominance) {
		return color(color, tolerance.getValue(), dominance.getValue());
	}
	
	public static Predicate color(ColorName colorName, double tolerance, double dominance) {
		return color(colorName.getColor(), tolerance, dominance);
	}
	
	public static Predicate color(ColorName colorName, Tolerance tolerance, Dominance dominance) {
		return color(colorName.getColor(), tolerance.getValue(), dominance.getValue());
	}
	
	public static Predicate color(Color color) {
		return color(color, Tolerance.MIDHIGH, Dominance.MIDHIGH);
	}

	public static Predicate color(ColorName colorName) {
		return color(colorName.getColor());
	}
	
}
