package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import java.util.List;

import org.openqa.selenium.WebElement;


public class Select extends ActionOnHtmlElement {
	
	public enum SelectMethod {
		SELECT, DESELECT
	}
	
	private enum CompareMethod {
		EQUAL, CONTAINS
	}
	
	private HtmlElement element;
	private org.openqa.selenium.support.ui.Select select;
	private SelectMethod method;
	private String optionValue;

	public Select(Browser browser, HtmlElement element, String optionQuery, SelectMethod method) {
		super(browser, element);
		this.element = element;
		this.select = new org.openqa.selenium.support.ui.Select(element.getSeleniumElement());
		this.optionValue = getOptionValue(optionQuery);
		this.method = method;
	}
	
	private String getOptionValue(String query) {
		List<WebElement> options = select.getOptions();
		WebElement option = null;
		String optionValue = null;
		if((option = hasFittingOption(options, query, CompareMethod.EQUAL)) != null) optionValue  = option.getAttribute("value");
		else if((option = hasFittingOption(options, query, CompareMethod.CONTAINS)) != null) optionValue  = option.getAttribute("value");
		return optionValue;
	}
	
	private WebElement hasFittingOption(List<WebElement> options, String query, CompareMethod compare) {
		for (WebElement option: options) {
			switch (compare) {
			case EQUAL:
				if(option.getAttribute("value").equals(query) || option.getText().equals(query)) {
					return option;
				}
				break;
			case CONTAINS:
				if(option.getAttribute("value").contains(query) || option.getText().contains(query)) {
					return option;
				}
				break;
			}
		}
		return null;
	}
	
	@Override
	protected void perform() throws Exception {
		if(element != null) {
			// TODO check if option exists
			browser.log().info("Selected option '" + optionValue + "' for: {}", element);
			switch(method) {
				case SELECT:
					select.selectByValue(optionValue);
					break;
				case DESELECT:
					select.deselectByValue(optionValue);
					break;
			}
		} else {
			browser.log().warn("Select element to choose option does not exist");
		}
	}
}
