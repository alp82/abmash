package com.abmash.REMOVE.core.htmlquery.selector;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;



public class DirectMatchSelector extends Selector {

	private HtmlElements elements;
	
	public DirectMatchSelector(HtmlElements elements) {
		super(null, 0);
		this.elements = elements;
	}

	@Override
	public String getExpressionAsJQueryCommand() {
		// TODO direct match as jquery
		String script = "jQuery();";
		return script;
	}

	public HtmlElements getElements() {
		return elements;
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
