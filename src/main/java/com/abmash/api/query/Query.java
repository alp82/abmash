package com.abmash.api.query;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
		
		// send screenshot if color queries requested
		if(hasColorPredicates(predicates)) {
			try {
				byte[] pageAsPNGByteArray = ((TakesScreenshot) browser.getWebDriver()).getScreenshotAs(OutputType.BYTES);
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(pageAsPNGByteArray));
				
				// encode image as base64 string
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, "png", baos);
				baos.flush();
				pageAsPNGByteArray = baos.toByteArray();
				baos.close();
				
				// TODO find way to take only one screenshot
//				String pageAsBase64PNG = "data:image/png;base64," + Base64.encodeBase64URLSafeString(pageAsPNGByteArray);
				String pageAsBase64PNG = "data:image/png;base64," + ((TakesScreenshot) browser.getWebDriver()).getScreenshotAs(OutputType.BASE64);
				
				// TODO example base64 png
//				pageAsBase64PNG = "data:image/png;base64," + "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";

				if(pageAsBase64PNG.length() >= 500000) {
					for (int i = 0; i < pageAsBase64PNG.length(); i += 500000) {
						browser.javaScript("abmash.buildImageDataForPageScreenshot(arguments[0])", pageAsBase64PNG.substring(i, Math.min(i + 499999, pageAsBase64PNG.length())));
					}
					browser.javaScript("abmash.updatePageScreenshot(arguments[0], arguments[1])", image.getWidth(), image.getHeight());
				} else {
					browser.javaScript("abmash.buildImageDataForPageScreenshot(arguments[0]); abmash.updatePageScreenshot(arguments[1], arguments[2])", pageAsBase64PNG, image.getWidth(), image.getHeight());
				}
				
//				browser.javaScript("abmash.updatePageScreenshot(arguments[0], arguments[1], arguments[2])", pageAsBase64PNG, image.getWidth(), image.getHeight());
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	
	private boolean hasColorPredicates(Predicates predicates) {
		for(Predicate predicate: predicates) {
			if(predicate instanceof ColorPredicate) return true;
			if(predicate instanceof BooleanPredicate) {
				if(hasColorPredicates(((BooleanPredicate) predicate).getPredicates())) {
					return true;
				}
			}
		}
		return false;
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
					jsonPredicate.put("color", ((ColorPredicate) predicate).getColorAsRGB());
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
