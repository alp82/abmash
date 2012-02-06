package com.abmash.test;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;


public class BrowserTest {

	Browser browser;
	
	@Before
	public void setUp() throws Exception {
		browser = new Browser(null);
	}
	
	@After
	public void tearDown() throws Exception {
		browser.close();
	}
	
	@Test
	public void testHtmlTagWithoutAttributes() {
		browser.openUrl("http://acamasystems.com/");
		HtmlElement element = browser.query().isText().has("body").findFirst();
		assertArrayEquals(element.getAttributeNames().toArray(), new ArrayList<String>().toArray());
	}

}
