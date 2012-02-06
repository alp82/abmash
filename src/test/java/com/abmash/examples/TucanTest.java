package com.abmash.examples;


import com.abmash.api.Browser;

import java.io.FileInputStream;
import java.util.Properties;


public class TucanTest {
	
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

		browser.openUrl("https://www.tucan.tu-darmstadt.de");
		browser.type("TU-ID", "a_ortac");		
		browser.type("Passwort", properties.getProperty("tucan.password"));
		browser.click("Anmelden");
		browser.click("Prüfungen");
		browser.click("Leistungsspiegel");
//		DocumentElement el = browser.findElement(".gamePanelHome", Strategy.DEFAULT);
//		System.out.println(el.getUniqueSelector());
//		browser.highlight(el.getUniqueSelector());
		browser.debug().breakpoint();

		browser.close();
	}

}
