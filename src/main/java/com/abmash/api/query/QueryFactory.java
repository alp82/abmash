package com.abmash.api.query;

import java.awt.Color;
import java.util.ArrayList;

import com.abmash.REMOVE.api.HtmlQuery;
import com.abmash.REMOVE.core.htmlquery.condition.ClosenessCondition.Direction;
import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
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
import com.abmash.core.query.predicate.XPathPredicate;

/**
 * Create new query predicates. Use a static import in your class:
 * <pre>
 * <code>import static com.abmash.api.query.QueryFactory.*;</code>
 * </pre>
 * @author Alper Ortac
 */
public class QueryFactory {
	
	/**
	 * Creates a new query to find elements in the specified browser and with the given predicates, used by calling {@link Browser#query(Predicate...)}.
	 * <p>
	 * <strong>Examples:</strong>
	 * <ul>
	 * <li><code>browser.query(contains("result")).findFirst();</code> searches for elements containing the attribute or
	 * inner text <em>result</em></li> 
	 * <li><code>browser.query(headline()).findFirst();</code> searches for title elements, i.e. {@code <h1>}, {@code <h2>}, ... {@code <h6>}
	 * and any element with a bigger font-size than the default on the current page</li> 
	 * <li><code>browser.query(clickable()).findFirst();</code> searches for all clickable elements like links and buttons</li> 
	 * <li><code>browser.query(image("description"), below(typable("mail")).findFirst();</code> searches for all image elements with the label
	 * <em>description</em> which are below typable elements labeled <em>mail</em></li> 
	 * </ul>
	 * <p>
	 * <strong>Description:</strong>
	 * <p>
	 * Create a new query instance by calling <code>browser.query()</code>. Predicates can be chained and arbitrarily nested one after another.
	 * To get the matching {@link HtmlElements} or {@link HtmlElement}, call {@link Query#find()} or {@link Query#findFirst()}.
	 * 
	 * @param browser
	 * @param predicates
	 * @return the new query object with all predicates
	 */
	public static Query query(Browser browser, Predicate... predicates) {
		return new Query(browser, predicates);
	}
	
	/**
	 * Creates a union of multiple queries by merging their results.
	 * 
	 * @param queries the queries for the union
	 * @return new {@link Query} with merged results 
	 */
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

	// -------------------------------------------
	//  Boolean predicates
	// -------------------------------------------
	
	/**
	 * Defines a boolean "and" predicate. Elements are returned if all given predicates match.
	 * 
	 * @param predicates all of the predicates need to match to return an element
	 * @return a boolean "and" predicate which can be arbitrarily nested with other predicates
	 */
	public static Predicate and(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.AND, predicates);
	}
	
	
	/**
	 * Defines a boolean "or" predicate. Elements are returned if at least on of the given predicates match.
	 * 
	 * @param predicates at least one of the predicates need to match to return an element
	 * @return a boolean "or" predicate which can be arbitrarily nested with other predicates
	 */
	public static Predicate or(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.OR, predicates);
	}
	
	/**
	 * Defines a boolean "not" predicate. All elements that match the given predicates will be removed from the result set.
	 * <p>
	 * TODO describe standalone "not" predicates
	 * 
	 * @param predicates all elements that match the predicates will be removed from the result set
	 * @return a boolean "not" predicate which can be arbitrarily nested with other predicates
	 */
	public static Predicate not(Predicate... predicates) {
		return new BooleanPredicate(BooleanType.NOT, predicates);
	}
	
	// -------------------------------------------
	//  Element and document structure predicates
	// -------------------------------------------
	
	/**
	 * Returns the given {@link HtmlElements} as predicate result.
	 * 
	 * @param elements the elements are directly passed to the result set
	 * @return the Predicate returning the provided {@link HtmlElements}
	 */
	public static Predicate elements(HtmlElements elements) {
		return new ElementPredicate(elements);
	}
	
	/**
	 * Returns the given {@link HtmlElements} as predicate result.
	 * 
	 * @param elements the elements are directly passed to the result set
	 * @return the Predicate returning the provided {@link HtmlElements}
	 */
//	public static Predicate insideOf(HtmlElements elements) {
//		return new ElementPredicate(elements);
//	}
	
	// -------------------------------------------
	//  JQuery predicates
	// -------------------------------------------
	
	/**
	 * Finds elements that match the given selector (CSS or jQuery).
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * select("div#content > p.info:visible > strong[name=author]")
	 * </pre>
	 * 
	 * @param selector the jQuery/CSS selector
	 * @return {@link SelectPredicate}
	 */
	public static Predicate select(String selector) {
		return new SelectPredicate(selector);
	}
	
	/**
	 * Finds elements that match the given XPath selector.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * xpath("//div[id=content]/p[class=info]/strong[name=author]")
	 * </pre>
	 * 
	 * @param selector the XPath selector
	 * @return {@link XPathPredicate}
	 */
	public static Predicate xPath(String selector) {
		return new XPathPredicate(selector);
	}
	
	/**
	 * Finds elements that contain a specific text or attribute value.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * contains("contact me")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link ContainsPredicate}
	 */
	public static Predicate contains(String text) {
		return new ContainsPredicate(text);
	}
	
	/**
	 * Finds text elements that contain a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * text("lorem ipsum")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link TextPredicate}
	 */
	public static Predicate text(String text) {
		return new TextPredicate(text);
	}
	
	/**
	 * Finds all elements that contain text.
	 * 
	 * @see QueryFactory#text(String)
	 * @return {@link TextPredicate}
	 */
	public static Predicate text() {
		return text(null);
	}
	
	/**
	 * Finds title/headline elements that contain a specific text and have a bigger font size than average elements.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * headline("contents")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link HeadlinePredicate}
	 */		
	public static Predicate headline(String text) {
		return new HeadlinePredicate(text);
	}
	
	/**
	 * Finds all elements that are titles/headlines or have a bigger font size than average elements.
	 * 
	 * @see QueryFactory#headline(String)
	 * @return {@link HeadlinePredicate}
	 */	
	public static Predicate headline() {
		return headline(null);
	}
	
	/**
	 * Finds link elements that contain a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * link("follow me")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link LinkPredicate}
	 */	
	public static Predicate link(String text) {
		return new LinkPredicate(text);
	}
	
	/**
	 * Finds all link elements.
	 * 
	 * @see QueryFactory#link(String)
	 * @return {@link LinkPredicate}
	 */		
	public static Predicate link() {
		return link(null);
	}
	
	/**
	 * Finds clickable elements that contain a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * clickable("search")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link ClickablePredicate}
	 */			
	public static Predicate clickable(String text) {
		return new ClickablePredicate(text);
	}
	
	/**
	 * Finds all clickable elements.
	 * 
	 * @see QueryFactory#clickable(String)
	 * @return {@link ClickablePredicate}
	 */		
	public static Predicate clickable() {
		return clickable(null);
	}
	
	/**
	 * Finds input elements that can be used to enter text and are labeled with a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * typable("email")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link TypablePredicate}
	 */
	public static Predicate typable(String text) {
		return new TypablePredicate(text);
	}
	
	/**
	 * Finds all input elements that can be used to enter text.
	 * 
	 * @see QueryFactory#typable(String)
	 * @return {@link TypablePredicate}
	 */
	public static Predicate typable() {
		return typable(null);
	}
	
	/**
	 * Finds input elements that can be used to check/uncheck an option and are labeled with a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * checkable("newsletter")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link CheckablePredicate}
	 */	
	public static Predicate checkable(String text) {
		return new CheckablePredicate(text);
	}
	
	/**
	 * Finds all input elements that can be used to check/uncheck an option.
	 * 
	 * @see QueryFactory#checkable(String)
	 * @return {@link CheckablePredicate}
	 */
	public static Predicate checkable() {
		return checkable(null);
	}
	
	/**
	 * Finds input elements that can be used to choose from multiple options and are labeled with a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * choosable("ingredients")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link ChoosablePredicate}
	 */	
	public static Predicate choosable(String text) {
		return new ChoosablePredicate(text);
	}
	
	/**
	 * Finds all input elements that can be used to choose from multiple options.
	 * 
	 * @see QueryFactory#choosable(String)
	 * @return {@link ChoosablePredicate}
	 */
	public static Predicate choosable() {
		return choosable(null);
	}
	
	/**
	 * Finds input elements that can be used to select a date and are labeled with a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * datepicker("departure")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link DatepickerPredicate}
	 */	
	public static Predicate datepicker(String text) {
		return new DatepickerPredicate(text);
	}

	/**
	 * Finds input elements that can be used to select a date.
	 * 
	 * @see QueryFactory#datepicker(String)
	 * @return {@link DatepickerPredicate}
	 */
	public static Predicate datepicker() {
		return datepicker(null);
	}
	
	/**
	 * Finds input elements that can be used to submit a form and are labeled with a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * submittable("purchase")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link SubmittablePredicate}
	 */		
	public static Predicate submittable(String text) {
		return new SubmittablePredicate(text);
	}
	
	/**
	 * Finds input elements that can be used to submit a form.
	 * 
	 * @see QueryFactory#submittable(String)
	 * @return {@link SubmittablePredicate}
	 */	
	public static Predicate submittable() {
		return submittable(null);
	}
	
	/**
	 * Finds image elements that are labeled/captioned with a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * image("statistics last year")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link ImagePredicate}
	 */
	public static Predicate image(String text) {
		return new ImagePredicate(text);
	}

	/**
	 * Finds all image elements.
	 * 
	 * @see QueryFactory#image(String)
	 * @return {@link ImagePredicate}
	 */
	public static Predicate image() {
		return image(null);
	}
	
	/**
	 * Finds frames and iframe elements that are labeled/captioned with a specific text.
	 * <p> 
	 * <strong>Example:</strong>
	 * <pre>
	 * frame("information")
	 * </pre>
	 * 
	 * @param text case insensitive text
	 * @return {@link FramePredicate}
	 */	
	public static Predicate frame(String text) {
		return new FramePredicate(text);
	}

	// -------------------------------------------
	//  Color predicates
	// -------------------------------------------
	
	/**
	 * Finds elements that match the desired color. Performs slow if no other predicates are used.
	 * <p>
	 * Tolerance: Lower values mean a lower tolerance regarding the color distance between the given color
	 * and the pixels of an element to select it. 
	 * <p>
	 * Dominance: Lower values mean a lower percentage of pixels containing the specified color needed to select an element. 
	 * 
	 * @param color the result elements have a similar color as the specified {@link Color}
	 * @param tolerance the higher the tolerance, the more distant colors are matching.
	 * <code>1</code> is always true and <code>0</code> is only true if the image has enough dominant pixels with that exact color
	 * @param dominance the higher the dominance, the less percent of the element can be covered by other colors
	 * <code>0</code> is always true and <code>1</code> is only true if the image has exclusively colors within the tolerance range
	 * @see Tolerance
	 * @see Dominance
	 * @return {@link ColorPredicate}
	 */
	public static Predicate color(Color color, double tolerance, double dominance) {
		return new ColorPredicate(color, tolerance, dominance);
	}
	
	/**
	 * Finds elements that match the desired color. See {@link #color(Color, double, double)} for details.
	 * 
	 * @param color the result elements have a similar color as the specified {@link Color}
	 * @param tolerance the higher the tolerance, the more distant colors are matching
	 * @param dominance the higher the dominance, the less percent of the element can be covered by other colors
	 * @return {@link ColorPredicate}
	 */
	public static Predicate color(Color color, Tolerance tolerance, Dominance dominance) {
		return color(color, tolerance.getValue(), dominance.getValue());
	}
	
	/**
	 * Finds elements that match the desired color. See {@link #color(Color, double, double)} for details.
	 * 
	 * @param colorName the result elements have a similar color as the specified {@link ColorName}
	 * @param tolerance the higher the tolerance, the more distant colors are matching
	 * @param dominance the higher the dominance, the less percent of the element can be covered by other colors
	 * @return {@link ColorPredicate}
	 */	
	public static Predicate color(ColorName colorName, double tolerance, double dominance) {
		return color(colorName.getColor(), tolerance, dominance);
	}
	
	/**
	 * Finds elements that match the desired color. See {@link #color(Color, double, double)} for details.
	 * 
	 * @param colorName the result elements have a similar color as the specified {@link ColorName}
	 * @param tolerance the higher the tolerance, the more distant colors are matching
	 * @param dominance the higher the dominance, the less percent of the element can be covered by other colors
	 * @return {@link ColorPredicate}
	 */		
	public static Predicate color(ColorName colorName, Tolerance tolerance, Dominance dominance) {
		return color(colorName.getColor(), tolerance.getValue(), dominance.getValue());
	}
	
	/**
	 * Finds elements that match the desired color. Tolerance and dominance are set to intermediate values.
	 * See {@link #color(Color, double, double)} for details.
	 * 
	 * @param color the result elements have a similar color as the specified {@link Color}
	 * @return {@link ColorPredicate}
	 */	
	public static Predicate color(Color color) {
		return color(color, Tolerance.MIDHIGH, Dominance.MIDHIGH);
	}

	/**
	 * Finds elements that match the desired color. Tolerance and dominance are set to intermediate values.
	 * See {@link #color(Color, double, double)} for details.
	 * 
	 * @param colorName the result elements have a similar color as the specified {@link ColorName}
	 * @return {@link ColorPredicate}
	 */	
	public static Predicate color(ColorName colorName) {
		return color(colorName.getColor());
	}
	
	// -------------------------------------------
	//  Direction predicates
	// -------------------------------------------
	
	/**
	 * Finds elements visually close to the elements that match the given predicates. The result is dependent on the current
	 * browser window size.
	 * <p>
	 * The {@link DirectionOptions} if found elements will be filtered out in case they are visually at another location.
	 * The found elements will be automatically ordered by closeness to the referenceElements.
	 * The distance is calculated by the euclidean measure.
	 * <p>
	 * Use the shortcuts of this method if needed. See ({@link #above(Predicate...)}, {@link #below(Predicate...)},
	 * {@link #leftOf(Predicate...)} and {@link #rightOf(Predicate...)}.
	 * <p>
	 * Calling {@link Query#find()} will order the result by closeness, so that the first result is the closest element to
	 * the reference element. 
	 * 
	 * @param options the parameters of this predicate, including maximum distance and allowed directions
	 * @param predicates the result elements are close to the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(DirectionOptions options, Predicate... predicates) {
		return new DirectionPredicate(options, predicates);
	}
	
	/**
	 * Finds elements visually close to the given {@link HtmlElements}. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param options the parameters of this predicate, including maximum distance and allowed directions
	 * @param elements the result elements are close to these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(DirectionOptions options, HtmlElements elements) {
		return closeTo(options, new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually close to the given {@link HtmlElement}. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param options the parameters of this predicate, including maximum distance and allowed directions
	 * @param element the result elements are close to this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(DirectionOptions options, HtmlElement element) {
		return closeTo(options, new ElementPredicate(new HtmlElements(element)));
	}

	/**
	 * Finds elements visually close to the elements that match the given predicates, with a maximum distance. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param maxDistance the result elements` allowed maximum distance to the predicate results in pixels
	 * @param predicates the result elements are close to the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(int maxDistance, Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.CLOSETO).setMaxDistance(maxDistance), predicates);
	}
	
	/**
	 * Finds elements visually close to the given {@link HtmlElements}, with a maximum distance. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param maxDistance the result elements` allowed maximum distance to the predicate results in pixels
	 * @param elements the result elements are close to these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(int maxDistance, HtmlElements elements) {
		return closeTo(maxDistance, new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually close to the given {@link HtmlElement}, with a maximum distance. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param maxDistance the result elements` allowed maximum distance to the predicate results in pixels
	 * @param element the result elements are close to this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(int maxDistance, HtmlElement element) {
		return closeTo(maxDistance, new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually close to the elements that match the given predicates, with a maximum distance of 300 pixels. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * TODO make constant of 300 pixels configurable
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param predicates the result elements are close to the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(Predicate... predicates) {
		return closeTo(300, predicates);
	}
	
	/**
	 * Finds elements visually close to the given {@link HtmlElements}, with a maximum distance of 300 pixels. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * TODO make constant of 300 pixels configurable
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param elements the result elements are close to these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(HtmlElements elements) {
		return closeTo(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually close to the given {@link HtmlElement}, with a maximum distance of 300 pixels. See {@link #closeTo(Predicate...)} for details.
	 * 
	 * TODO make constant of 300 pixels configurable
	 * 
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...)
	 * @param element the result elements are close to these {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate closeTo(HtmlElement element) {
		return closeTo(new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually <strong>above</strong> the elements that match the given predicates.
	 * <p>
	 * Elements are above if their bottom y coordinate is equal or lower than the top y coordinate of the reference element.
	 * <s>In addition, the element has to be in horizontal bounds of the reference element.</s>
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
	 * In this example, both the {@code <div>} and the {@code <span>} are above the {@code <ul>}.
	 * Also, the {@code <span>} is above the {@code <div>}, but the {@code <ul>} is not.
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate above(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.ABOVE), predicates);
	}
	
	/**
	 * Finds elements visually <strong>above</strong> the given {@link HtmlElements}. See {@link #above(Predicate...)} for details.
	 * 
	 * @see #above(Predicate...)
	 * @param elements the result elements are above these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate above(HtmlElements elements) {
		return above(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually <strong>above</strong> the given {@link HtmlElement}. See {@link #above(Predicate...)} for details.
	 * 
	 * @see #above(Predicate...)
	 * @param element the result elements are above this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate above(HtmlElement element) {
		return above(new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually <strong>above</strong> the elements that match the given predicates.
	 * <p>
	 * Elements are below if their top y coordinate is equal or greater than the bottom y coordinate of the reference element.
	 * <s>In addition, the element has to be in horizontal bounds of the reference element.</s>
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------
	 *   - <ul> -
	 *   --------
	 *        ---------
	 *        - <div> -
	 *        ---------
	 *              ----------
	 *              - <span> -
	 *              ----------
	 * }</pre>
	 * In this example, the {@code <div>} and the {@code <span>} are below the {@code <ul>}.
	 * Also, the {@code <span>} is below the {@code <div>}, but the {@code <ul>} is not.
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate below(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.BELOW), predicates);
	}	
	
	/**
	 * Finds elements visually <strong>below</strong> the given {@link HtmlElements}. See {@link #below(Predicate...)} for details.
	 * 
	 * @see #below(Predicate...)
	 * @param elements the result elements are below these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */	
	public static Predicate below(HtmlElements elements) {
		return below(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually <strong>below</strong> the given {@link HtmlElement}. See {@link #below(Predicate...)} for details.
	 * 
	 * @see #below(Predicate...)
	 * @param element the result elements are below this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate below(HtmlElement element) {
		return below(new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually <strong>left of</strong> the elements that match the given predicates.
	 * <p>
	 * Elements are left of if their right x coordinate is equal or lower than the left x coordinate of the reference element.
	 * <s>In addition, the element has to be in vertical bounds of the reference element.</s>
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                    ----------
	 *   --------         - <span> -
	 *   - <ul> -         ----------
	 *   --------   ---------
	 *              - <div> -
	 *              ---------
	 * }</pre>
	 * In this example, the {@code <ul>} is left of the {@code <span>} and the {@code <div>}, but the {@code <div>} is
	 * not left of the {@code <span>}, because its right border is overlapping the left border of the {@code <span>}. 
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate leftOf(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.LEFTOF), predicates);
	}	
	
	/**
	 * Finds elements visually <strong>left of</strong> the given {@link HtmlElements}. See {@link #leftOf(Predicate...)} for details.
	 * 
	 * @see #leftOf(Predicate...)
	 * @param elements the result elements are left of these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */	
	public static Predicate leftOf(HtmlElements elements) {
		return leftOf(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually <strong>left of</strong> the given {@link HtmlElement}. See {@link #leftOf(Predicate...)} for details.
	 * 
	 * @see #leftOf(Predicate...)
	 * @param element the result elements are left of this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */	
	public static Predicate leftOf(HtmlElement element) {
		return leftOf(new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually <strong>right of</strong> the elements that match the given predicates.
	 * <p>
	 * Elements are right of if their left x coordinate is higher than the right x coordinate of the reference element.
	 * <s>In addition, the element has to be in vertical bounds of the reference element.</s>
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                    ----------
	 *   --------         - <span> -
	 *   - <ul> -         ----------
	 *   --------  ---------
	 *             - <div> -
	 *             ---------
	 * }</pre>
	 * In this example, the {@code <span>} and the {@code <div>} are right of the {@code <ul>}, but the {@code <span>}
	 * is not right of the {@code <div>} because its left border is overlapping the right border of the {@code <span>}.  
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate rightOf(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.RIGHTOF), predicates);
	}
	
	/**
	 * Finds elements visually <strong>right of</strong> the given {@link HtmlElements}. See {@link #rightOf(Predicate...)} for details.
	 * 
	 * @see #rightOf(Predicate...)
	 * @param elements the result elements are left of these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */	
	public static Predicate rightOf(HtmlElements elements) {
		return rightOf(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually <strong>right of</strong> the given {@link HtmlElement}. See {@link #rightOf(Predicate...)} for details.
	 * 
	 * @see #rightOf(Predicate...)
	 * @param element the result elements are left of this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */	
	public static Predicate rightOf(HtmlElement element) {
		return rightOf(new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually <strong>above all</strong> the elements that match the given predicates.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *             ----------
	 *             - <span> -
	 *             ----------
	 *        ---------   --------
	 *        - <div> -   - <ul> -
	 *        ---------   --------
	 *   --------            ---------
	 *   - <ul> -            - <div> -
	 *   --------            ---------
	 * }</pre>
	 * In this example, only the {@code <span>} is above all the {@code <ul>} (or {@code <div>} respectively).
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @see QueryFactory#above(Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate aboveAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.ABOVE).setDirectionHasToMatchAllTargets(true), predicates);
	}
	
	/**
	 * Finds elements visually <strong>above all</strong> the elements that match the given {@link HtmlElements}.
	 * See {@link #aboveAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#aboveAll(Predicate...)
	 * @param elements the result elements are above all these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate aboveAll(HtmlElements elements) {
		return aboveAll(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually <strong>above all</strong> the elements that match the given {@link HtmlElement}.
	 * See {@link #aboveAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#aboveAll(Predicate...)
	 * @param element the result elements are above all this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */	
	public static Predicate aboveAll(HtmlElement element) {
		return aboveAll(new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually <strong>below all</strong> the elements that match the given predicates.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   --------            ---------
	 *   - <ul> -            - <div> -
	 *   --------            ---------
	 *        ---------   --------
	 *        - <div> -   - <ul> -
	 *        ---------   --------
	 *             ----------
	 *             - <span> -
	 *             ----------
	 * }</pre>
	 * In this example, only the {@code <span>} is below all the {@code <ul>} (or {@code <div>} respectively).
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @see QueryFactory#below(Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate belowAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.BELOW).setDirectionHasToMatchAllTargets(true), predicates);
	}	
	
	/**
	 * Finds elements visually <strong>below all</strong> the elements that match the given {@link HtmlElements}.
	 * See {@link #belowAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#belowAll(Predicate...)
	 * @param elements the result elements are below all these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate belowAll(HtmlElements elements) {
		return belowAll(new ElementPredicate(elements));
	}

	/**
	 * Finds elements visually <strong>below all</strong> the elements that match the given {@link HtmlElement}.
	 * See {@link #belowAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#belowAll(Predicate...)
	 * @param element the result elements are below all this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate belowAll(HtmlElement element) {
		return belowAll(new HtmlElements(element));
	}

	/**
	 * Finds elements visually <strong>left of all</strong> the elements that match the given predicates.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *                            --------
	 *                            - <ul> -
	 *                ---------   --------
	 *                - <div> -
	 *    ----------  ---------
	 *    - <span> -
	 *    ---------- ---------
	 *                - <ul> -
	 *                --------    ---------
	 *                            - <div> -
	 *                            ---------
	 * }</pre>
	 * In this example, only the {@code <span>} is left of all the {@code <ul>} (or {@code <div>} respectively).
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @see QueryFactory#leftOf(Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate leftOfAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.LEFTOF).setDirectionHasToMatchAllTargets(true), predicates);
	}
	
	/**
	 * Finds elements visually <strong>left of all</strong> the elements that match the given {@link HtmlElements}.
	 * See {@link #leftOfAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#leftOfAll(Predicate...)
	 * @param elements the result elements are left of all these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate leftOfAll(HtmlElements elements) {
		return leftOfAll(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually <strong>left of all</strong> the elements that match the given {@link HtmlElement}.
	 * See {@link #leftOfAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#leftOfAll(Predicate...)
	 * @param element the result elements are left of all this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */	
	public static Predicate leftOfAll(HtmlElement element) {
		return leftOfAll(new HtmlElements(element));
	}
	
	/**
	 * Finds elements visually <strong>right of all</strong> the elements that match the given predicates.
	 * <p>
	 * <strong>Example:</strong> (visual representation of the web page) 
	 * <pre>{@code
	 * .
	 *   ---------
	 *   - <div> -
	 *   ---------  --------
	 *              - <ul> -
	 *              --------   ----------
	 *                         - <span> -
	 *              ---------  ----------
	 *              - <div> -
	 *   --------   ---------
	 *   - <ul> -
	 *   --------
	 * }</pre>
	 * In this example, only the {@code <span>} is right of all the {@code <ul>} (or {@code <div>} respectively).
	 * <p>
	 * See {@link QueryFactory#closeTo(DirectionOptions, Predicate...)} for more details.
	 *
	 * @see QueryFactory#closeTo(DirectionOptions, Predicate...) 
	 * @see QueryFactory#rightOf(Predicate...) 
	 * @param predicates the result elements are above the elements determined by this predicates
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate rightOfAll(Predicate... predicates) {
		return closeTo(new DirectionOptions(DirectionType.RIGHTOF).setDirectionHasToMatchAllTargets(true), predicates);
	}
	
	/**
	 * Finds elements visually <strong>right of all</strong> the elements that match the given {@link HtmlElements}.
	 * See {@link #rightOfAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#rightOfAll(Predicate...)
	 * @param elements the result elements are right of all these {@link HtmlElements}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate rightOfAll(HtmlElements elements) {
		return rightOfAll(new ElementPredicate(elements));
	}
	
	/**
	 * Finds elements visually <strong>right of all</strong> the elements that match the given {@link HtmlElement}.
	 * See {@link #rightOfAll(Predicate...)} for more details.
	 * 
	 * @see QueryFactory#rightOfAll(Predicate...)
	 * @param element the result elements are right of all this {@link HtmlElement}
	 * @return {@link DirectionPredicate}
	 */
	public static Predicate rightOfAll(HtmlElement element) {
		return rightOfAll(new HtmlElements(element));
	}
	
}
