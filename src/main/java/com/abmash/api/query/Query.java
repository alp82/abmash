package com.abmash.api.query;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.remote.RemoteWebElement;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.core.element.Location;
import com.abmash.core.element.Size;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.command.Command;
import com.abmash.core.query.BooleanType;
import com.abmash.core.query.predicate.BooleanPredicate;
import com.abmash.core.query.predicate.ColorPredicate;
import com.abmash.core.query.predicate.DirectionPredicate;
import com.abmash.core.query.predicate.JQueryPredicate;
import com.abmash.core.query.predicate.Predicate;
import com.abmash.core.query.predicate.Predicates;
import com.abmash.core.query.predicate.RecursivePredicate;
import com.abmash.core.tools.DataTypeConversion;

public class Query {

	private Predicates predicates = new Predicates();
	
	public Query(Predicates predicates) {
		addPredicates(predicates);
	}
	
	public Query(Predicate... predicates) {
		addPredicates(predicates);
	}

	public void addPredicates(Predicates predicates) {
		addPredicates((Predicate[]) predicates.toArray());
	}
	
	public void addPredicates(Predicate... predicates) {
		for(Predicate predicate: predicates) {
			this.predicates.add(predicate);
		}
	}

	public void union(Query... queries) {
		Predicate andPredicate = new BooleanPredicate(BooleanType.AND, (Predicate[]) predicates.toArray());
		BooleanPredicate orPredicate = new BooleanPredicate(BooleanType.OR, andPredicate);
		for(Query query: queries) {
			orPredicate.addPredicates((Predicate[]) query.getPredicates().toArray());
		}
		predicates = new Predicates(andPredicate);
	}
	
	public Predicates getPredicates() {
		return predicates;
	}
	
//	private JQueryLists build() {
//		jQueryLists = new JQueryLists();
//		for(Predicate predicate: predicates) {
//			predicate.buildCommands();
//			
////			JQuery jQuery = new JQuery(null, null);
////			for(JQuery predicateJQuery: predicate.getJQueryList()) {
////				for(Command command: predicateJQuery.getCommands()) {
////					jQuery.addCommand(command);
////				}
////			}
////			jQueryList.add(jQuery);
//			jQueryLists.add(predicate.getJQueryList());
//		}
//		
//		return jQueryLists;
//	}
	
	public HtmlElements execute(Browser browser) {
		JSONArray jsonJQueryList = new JSONArray();
		try {
			jsonJQueryList = convertPredicatesToJSON(predicates);
//			System.out.println(jsonJQueryList.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String script = "return abmash.query(arguments[0], arguments[1]/*, arguments[2], arguments[3], arguments[4], arguments[5], arguments[6], arguments[7]*/);";
		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> resultElements = (ArrayList<Map<String, Object>>) browser.javaScript(
				script,
				jsonJQueryList.toString()
//				jsonClosenessConditions.toString(),
//				jsonColorConditions.toString(),
//				rootElements,
//				elementsToFilter,
//				referenceElements,
//				labelElements,
//				limit
		).getReturnValue();
		
		// converting selenium elements to abmash elements
		HtmlElements tempElements = new HtmlElements();
		for(Map<String, Object> resultElement: resultElements) {
			HtmlElement element = new HtmlElement(browser, (RemoteWebElement) resultElement.get("element"));
			element.setTagName((String) resultElement.get("tag"));
			element.setText((String) resultElement.get("text"));
			element.setSourceText((String) resultElement.get("sourceText"));
			element.setAttributeNames((ArrayList<String>) resultElement.get("attributeNames"));
			element.setAttributes((Map<String,String>) resultElement.get("attributes"));
			element.setUniqueSelector((String) resultElement.get("uniqueSelector"));
			
			Map<String, Object> location = (Map<String, Object>) resultElement.get("location");
			Double left = DataTypeConversion.longOrDoubleToDouble(location.get("left"));
			Double top = DataTypeConversion.longOrDoubleToDouble(location.get("top"));
			element.setLocation(new Location(left, top));
			
			Map<String, Object> size = (Map<String, Object>) resultElement.get("size");
			Double width = DataTypeConversion.longOrDoubleToDouble(size.get("width"));
			Double height = DataTypeConversion.longOrDoubleToDouble(size.get("height"));
			element.setSize(new Size(width, height));
			
//			// if filter elements are used, check if the elements match
//			if(elementsToFilter.isEmpty() || elementsToFilter.contains(element)) {
			tempElements.add(element);
//			}
		}
		
		return tempElements;
	}
	
	private JSONArray convertPredicatesToJSON(Predicates predicates) throws JSONException {
		JSONArray jsonPredicates = new JSONArray();
		for(Predicate predicate: predicates) {
			JSONObject jsonPredicate = new JSONObject();
			if(predicate instanceof RecursivePredicate) {
				if(predicate instanceof BooleanPredicate) {
					jsonPredicate.put("isBoolean", true);
					jsonPredicate.put("type", ((BooleanPredicate) predicate).getType());
				} else if(predicate instanceof DirectionPredicate) {
					jsonPredicate.put("isDirection", true);
					jsonPredicate.put("type", ((DirectionPredicate) predicate).getType());
				} else if(predicate instanceof ColorPredicate) {
					jsonPredicate.put("isColor", true);
					jsonPredicate.put("color", ((ColorPredicate) predicate).getColor());
					jsonPredicate.put("tolerance", ((ColorPredicate) predicate).getTolerance());
					jsonPredicate.put("dominance", ((ColorPredicate) predicate).getDominance());
				}
				jsonPredicate.put("predicates", convertPredicatesToJSON(((RecursivePredicate) predicate).getPredicates()));
			} else if(predicate instanceof JQueryPredicate) {
				JSONArray jsonJQueryList = new JSONArray();
				for(JQuery jQuery: ((JQueryPredicate) predicate).getJQueryList()) {
					jsonJQueryList.put(convertJQueryToJSON(jQuery));
				}
				jsonPredicate.put("jQueryList", jsonJQueryList);
			}
			jsonPredicates.put(jsonPredicate);
		}
		
		return jsonPredicates;
	}	
	
	private JSONObject convertJQueryToJSON(JQuery jQuery) throws JSONException {
		JSONArray jsonCommands = new JSONArray();
		for(Command command: jQuery.getCommands()) {
			JSONObject jsonCommand = new JSONObject();
			jsonCommand.put("method", command.getMethod());
			jsonCommand.put("selector", command.getSelector());
			jsonCommands.put(jsonCommand);
		}
		
		JSONObject jsonJQuery = new JSONObject();
		jsonJQuery.put("selector", jQuery.getSelector());
		jsonJQuery.put("weight", jQuery.getWeight());
		jsonJQuery.put("commands", jsonCommands);
		return jsonJQuery;
	}
	
	@Override
	public String toString() {
		return toString(0);
	}

	public String toString(int intendationSpaces) {
		String str = "Query:";
		for(Predicate predicate: predicates) {
			str += "\n" + predicate.toString(intendationSpaces + 2);
		}
		return str;
	}

}
