package com.abmash.examples;


import com.abmash.api.Browser;

import java.io.FileInputStream;
import java.util.Properties;


public class AmazonTest {
	
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

		browser.openUrl("http://amazon.com/");
		browser.click("sign in");
		browser.type("email", "alportac@gmail.com");		
		browser.type("password", properties.getProperty("amazon.password")).submit();
		browser.click("Shop All");		
		browser.click("Blu-ray");		
		browser.type("search", "Matrix").submit();		
		browser.click("The Ultimate Matrix Collection");
		browser.click("Add to Shopping Cart");
		browser.click("Proceed to Checkout");
		browser.type("email", "alportac@gmail.com");		
		browser.type("password", properties.getProperty("amazon.password")).submit();
		browser.type("Full Name", "Test Name");
		browser.type("Address Line1", "Test Address");
		browser.type("City", "Test City");
		browser.type("ZIP", "12345");
		browser.click("Country");
		browser.click("Germany");
		browser.type("Phone", "0000099999");
		browser.click("Ship to this address");
		browser.debug().breakpoint();

		browser.close();
	}

}
