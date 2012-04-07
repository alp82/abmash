package com.abmash.api.query;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.remote.RemoteWebElement;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.api.query.predicate.Predicate;
import com.abmash.core.jquery.JQuery;
import com.abmash.core.jquery.command.BooleanCommand;
import com.abmash.core.jquery.command.Command;

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
			System.out.println(jsonJQueryList.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String script = "return abmash.query(arguments[0], arguments[1]/*, arguments[2], arguments[3], arguments[4], arguments[5], arguments[6], arguments[7]*/);";
		@SuppressWarnings("unchecked")
		ArrayList<RemoteWebElement> seleniumElements = (ArrayList<RemoteWebElement>) browser.javaScript(
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
		for(RemoteWebElement seleniumElement: seleniumElements) {
			HtmlElement element = new HtmlElement(browser, seleniumElement);
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
			jsonCommand.put("boolean", command.isBooleanCommand());
			jsonCommand.put("closeness", command.isClosenessCommand());
			jsonCommand.put("color", command.isColorCommand());
			if(command instanceof BooleanCommand) {
				BooleanCommand booleanCommand = ((BooleanCommand) command);
				jsonCommand.put("type", booleanCommand.getType());
				jsonCommand.put("jQueryList", convertJQueryListToJSON(booleanCommand.getJQueryList()));
			} else {
				jsonCommand.put("method", command.getMethod());
				jsonCommand.put("selector", command.getSelector());
				jsonCommand.put("weight", command.getWeight());
			}
			jsonCommands.put(jsonCommand);
		}
		
		JSONObject jsonJQuery = new JSONObject();
		jsonJQuery.put("weight", jQuery.getWeight());
		jsonJQuery.put("commands", jsonCommands);
		return jsonJQuery;
	}
	
	private ArrayList<JQuery> build() {
		jQueryList = new ArrayList<JQuery>();
		for(Predicate predicate: predicates) {
			predicate.buildCommands();
			
			JQuery jQuery = new JQuery(null);
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
