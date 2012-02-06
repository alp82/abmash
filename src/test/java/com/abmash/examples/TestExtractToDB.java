package com.abmash.examples;


import com.abmash.api.Browser;
import com.abmash.extraction.SearchContainer;
import com.abmash.extraction.WordExtractor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestExtractToDB {
	
	private static Browser browser;

	public static void main(String[] args) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "test", "test");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	    // read webdriver and profile stuff from property file and create remote browser instance
		browser = new Browser(null);

		browser.openUrl("http://www.cafe-extrablatt.com/");
		browser.click("essen");
		browser.click("pizza");
		SearchContainer searchContainer = new SearchContainer();
		searchContainer.addQuery("Pizza");
		WordExtractor extractor = new WordExtractor(browser, searchContainer);
		System.out.println(extractor.getOutput());
		browser.debug().breakpoint();

		browser.close();
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
