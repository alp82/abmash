package com.abmash.examples;


import com.abmash.api.Browser;

import java.io.FileInputStream;
import java.util.Properties;


public class LocalTestCore {

	public static void main(String[] args) {
		Browser browser = new Browser(null);

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
		
		browser.openUrl("http://localhost/phpmyadmin");
		browser.type("username", "root");
		browser.type("password", properties.getProperty("localhost.phpmyadmin.password")).submit();
		// TODO frame stuff in browser window manager
		browser.frame().switchTo("navigation");
		// TODO org.openqa.selenium.StaleElementReferenceException: Element not found in the cache
		browser.debug().highlight("phpMyAdmin");
		browser.window().switchToMain();
		browser.frame().switchTo("content");
		browser.debug().highlight("phpMyAdmin");
		browser.debug().breakpoint();
		browser.close();
	}

}
