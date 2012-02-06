package com.abmash.examples;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

public class TestNewApi {
	
	private static Browser browser;

	public static void main(String[] args) {
		// read browser configuration from property file and create remote browser instance
		browser = new Browser("http://google.de");

		browser.click("Bilder");
		HtmlElement searchButton = browser.query().isClickable().has("suchen").findFirst();
		HtmlElement searchField = browser.query().isTypable().closestTo(searchButton).findFirst();
		searchField.type("Jaguar").submit();
		browser.click("Nach Thema sortiert");
		browser.click("animal");
		browser.close();
	}

}
