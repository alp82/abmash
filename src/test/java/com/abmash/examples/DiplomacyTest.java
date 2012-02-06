package com.abmash.examples;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import java.io.FileInputStream;
import java.util.Properties;


public class DiplomacyTest {
	
	private static Browser browser;

	public static void main(String[] args) {
		// read webdriver and profile stuff from property file and create remote browser instance
		browser = new Browser(null);
		
		// read properties
		Properties properties = new Properties();
		try {
			FileInputStream stream;
			stream = new FileInputStream("private.properties");
			properties.load(stream);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		browser.openUrl("http://webdiplomacy.net/");
		browser.click("log on");
		browser.type("username", "alp82");		
		browser.type("password", properties.getProperty("diplomacy.password"));
		browser.click("Log on");		
		HtmlElement el = browser.query().has(".gamePanelHome").findFirst();
		browser.debug().highlight(el);
		browser.debug().breakpoint();

		browser.close();
	}

}
