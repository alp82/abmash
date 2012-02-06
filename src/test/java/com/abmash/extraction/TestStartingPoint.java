package com.abmash.extraction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElements;
import com.abmash.core.browser.BrowserConfig;
import com.abmash.extraction.PageTypeExtractor;
import com.abmash.extraction.WordExtractor;

import java.sql.SQLException;
import java.util.ArrayList;


public class TestStartingPoint extends TestWithDatabase {
	
	public static void main(String[] args) {
		createDBConnection("localhost", "test", "test", properties.getProperty("mysql.test.password"));
		
		// get url list from database or test it with a simple array
//		conn.createStatement();
		@SuppressWarnings("serial")
		final ArrayList<String> urls = new ArrayList<String>() {{
			add("http://www.hotelmedano.com/");
			add("http://www.saigon-bar.de/");
			add("http://www.parkcafe.com/");
		}};
		
	    // create new browser instance
		browser = new Browser(null);

		// extract page type for each url
		for (String url : urls) {
			browser.openUrl(url);
			PageTypeExtractor pageTypeExtractor = new PageTypeExtractor(browser, conn);
			pageTypeExtractor.perform();
			String pageType = pageTypeExtractor.getOutput().get(0);
			// save pageType somewhere
		}

		browser.close();
		closeDBConnection();
	}
	
}
