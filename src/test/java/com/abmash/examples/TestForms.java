package com.abmash.examples;

import com.abmash.api.Browser;

public class TestForms {
	
	private static Browser browser;

	public static void main(String[] args) {
		// read webdriver and profile stuff from property file and create remote browser instance
		browser = new Browser(null);

		browser.openUrl("http://web2.0rechner.de/");
		browser.click("4");
		browser.click("+");
		browser.click("8");
		browser.click("=");
		browser.type("input", "+3-2*4.5");
		browser.click("=");
		System.out.println(browser.query().isTypable().has("input").findFirst().getAttribute("value"));

		browser.close();
	}
	
}
