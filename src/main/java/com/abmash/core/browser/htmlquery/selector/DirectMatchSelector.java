package com.abmash.core.browser.htmlquery.selector;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;



public class DirectMatchSelector extends Selector {

	private HtmlElements elements;
	
	public DirectMatchSelector(HtmlElements elements) {
		super(null);
		this.elements = elements;
	}

	@Override
	public HtmlElements find(Browser browser) {
		return elements;
	}

	@Override
	public HtmlElements find(Browser browser, HtmlElement rootElement) {
		return elements;
	}
	
}
