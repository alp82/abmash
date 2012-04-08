package com.abmash.api.query;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.abmash.core.htmlquery.selector.JQuerySelector;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.command.BooleanCommand;
import com.abmash.core.jquery.command.Command;
import com.abmash.core.query.Predicate;
import com.abmash.core.tools.DataTypeConversion;

public class Query {

	private Predicate[] predicates;
	private ArrayList<JQuery> jQueryList;
	
	public Query(Predicate... predicates) {
		this.predicates = predicates;
		build();
	}

	public void union(Query... queries) {
		for(Query query: queries) {
			jQueryList.addAll(query.getjQueryList());
		}
	}
	
	public ArrayList<JQuery> getjQueryList() {
		return jQueryList;
	}
	
	public HtmlElements execute(Browser browser) {
		JSONArray jsonJQueryList = new JSONArray();
		try {
			jsonJQueryList = convertJQueryListToJSON(jQueryList);
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
		System.out.println(resultElements);
		
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
	
	private JSONArray convertJQueryListToJSON(ArrayList<JQuery> jQueryList) throws JSONException {
		JSONArray jsonJQueryList = new JSONArray();
		
		for(JQuery jQuery: jQueryList) {
			jsonJQueryList.put(convertJQueryToJSON(jQuery));
		}
		
		return jsonJQueryList;
	}
	
	private JSONObject convertJQueryToJSON(JQuery jQuery) throws JSONException {
		JSONArray jsonCommands = new JSONArray();
		for(Command command: jQuery.getCommands()) {
			JSONObject jsonCommand = new JSONObject();
			jsonCommand.put("isEval", command.isEvalCommand());
			jsonCommand.put("isBoolean", command.isBooleanCommand());
			jsonCommand.put("isCloseness", command.isClosenessCommand());
			jsonCommand.put("isColor", command.isColorCommand());
			if(command instanceof BooleanCommand) {
				BooleanCommand booleanCommand = ((BooleanCommand) command);
				jsonCommand.put("type", booleanCommand.getType());
				JSONArray jsonJQueryLists = new JSONArray();
				for(ArrayList<JQuery> jQueryList: booleanCommand.getJQueryLists()) {
					jsonJQueryLists.put(convertJQueryListToJSON(jQueryList));
				}
				jsonCommand.put("jQueryLists", jsonJQueryLists);
			} else {
				jsonCommand.put("method", command.getMethod());
				jsonCommand.put("selector", command.getSelector());
			}
			jsonCommands.put(jsonCommand);
		}
		
		JSONObject jsonJQuery = new JSONObject();
		jsonJQuery.put("selector", jQuery.getSelector());
		jsonJQuery.put("weight", jQuery.getWeight());
		jsonJQuery.put("commands", jsonCommands);
		return jsonJQuery;
	}
	
	private ArrayList<JQuery> build() {
		jQueryList = new ArrayList<JQuery>();
		for(Predicate predicate: predicates) {
			predicate.buildCommands();
			
//			JQuery jQuery = new JQuery(null, null);
//			for(JQuery predicateJQuery: predicate.getJQueryList()) {
//				for(Command command: predicateJQuery.getCommands()) {
//					jQuery.addCommand(command);
//				}
//			}
//			jQueryList.add(jQuery);
			jQueryList.addAll(predicate.getJQueryList());
		}
		
		return jQueryList;
	}
	
}
