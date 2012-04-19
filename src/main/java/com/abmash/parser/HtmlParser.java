package com.abmash.parser;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.api.query.QueryFactory;
import com.abmash.parser.content.Header;
import com.abmash.parser.content.Image;
import com.abmash.parser.content.Input;
import com.abmash.parser.content.Link;

public class HtmlParser extends DocumentParser {

	private Browser browser;
	
	// TODO parse methods as class structure

	public HtmlParser(Browser browser) {
		super(browser.getWebDriver().getPageSource());
		this.browser = browser;
	}

	@Override
	protected void parseUrl() {
		url = browser.getCurrentUrl();
	}

	// TODO structured: texts, tables, lists, sentences

	@Override
	protected void parseVisibleText() {
		// TODO visible text without html tags
		visibleText = browser.query(QueryFactory.select("body")).findFirst().getText();
//		browser.log().debug(content.getVisibleText());
	}
	
	@Override
	protected void parseTitle() {
		title = browser.getWebDriver().getTitle();
//		browser.log().debug(" TITLE: " + content.getTitle());
	}

	@Override
	protected void parseMetaTags() {
		// TODO parse meta-tags
//		HtmlElements metaElements = browser.query().cssSelector("meta").find();
//		for (HtmlElement element: metaElements) {
//			String key = element.getAttribute("name");
//			if(key == null) key = element.getAttribute("http-equiv");
//			if(key != null) {
////				browser.log().debug(" META: " + webElement.getAttribute(key) + " - " + webElement.getAttribute("content"));
//				metaTags.put(element.getAttribute(key), element.getAttribute("content"));
//			}
//		}
	}

	@Override
	protected void parseHeaders() {
		// parse headers
		HtmlElements headerElements = browser.query(QueryFactory.headline()).find();
		for (HtmlElement element: headerElements) {
			Header header = new Header();
			header.setText(element.getText());
			header.setSize(element.getCssValue("font-size"));
//			browser.log().debug(" HEADER: " + header);
			headers.add(header);
		}
	}

	@Override
	protected void parseLinks() {
		// parse links
		HtmlElements linkElements = browser.query(QueryFactory.select("a")).find();
		for (HtmlElement element: linkElements) {
			Link link = new Link();
			link.setUrl(element.getAttribute("href"));
			link.setName(element.getText());
//			browser.log().debug(" LINK: " + link);
			links.add(link);
		}
	}

	@Override
	protected void parseImages() {
		// parse images
		HtmlElements imageElements = browser.query(QueryFactory.image()).find();
		for (HtmlElement element: imageElements) {
			Image image = new Image();
			image.setUrl(element.getAttribute("src"));
			image.setTitle(element.getAttribute("title"));
			image.setAlt(element.getAttribute("alt"));
//			browser.log().debug(" IMAGE: " + image);
			images.add(image);
		}
	}

	@Override
	protected void parseInputs() {
		// parse forms
		HtmlElements inputElements = browser.query(QueryFactory.typable()).find();
		for (HtmlElement element: inputElements) {
			Input input = new Input();
			input.setLabel(""); //TODO find label
			input.setType(element.getAttribute("type"));
			input.setName(element.getAttribute("name"));
//			browser.log().debug(" INPUT: " + input);
			inputs.add(input);
		}
	}

	@Override
	public void parsePeriodicElements() {
	}

}

