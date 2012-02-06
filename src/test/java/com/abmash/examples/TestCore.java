package com.abmash.examples;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;

import java.util.ArrayList;


public class TestCore {
	
	private static Browser browser;

	public static void main(String[] args) {
		// read browser configuration from property file and create remote browser instance
		browser = new Browser(null);

		browser.openUrl("http://google.de");
		browser.click("Bilder");
		browser.type("q", "Darmstadt").submit();
		
		// configuration and formulating search query
		ArrayList<String> images = new ArrayList<String>();
		int pageCount = 5;

		// check for search query on multiple result pages
		for (int page = 1; page <= pageCount; page++) {
			
			// click on "next result page" if needed
			if(page > 1) {
//				browser.breakpoint();
				browser.click("Weiter");
			}
			
			// extract the desired captions and find the closest images
			HtmlElements captionElements = browser.query().has("platz").isText().find();
			for (HtmlElement captionElement: captionElements) {
				HtmlElement imageElement = browser.query().isImage().closestTo(captionElement).findFirst();
//				browser.highlight(imageElement);
				images.add(imageElement.getAttribute("src"));
			}
		}
		
		// do something with the extracted image sources
		for (String image: images) {
			System.out.println(" extracted image url: " + image);
		}
		
		browser.close();
	}

}
