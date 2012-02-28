package com.abmash.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;


public class HtmlElementTests {

	static Browser browser;
	
	@BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code      
		browser = new Browser(null);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    	browser.close();
    }
    
    /*@Before
	public void setUp() throws Exception {
		browser = new Browser(null);
	}
	
	@After
	public void tearDown() throws Exception {
		browser.close();
	}*/
	
	@Test
	public void testHtmlTagWithoutAttributes() {
		browser.openUrl("http://acama-systems.de/");
		HtmlElement element = browser.query().cssSelector("html > body").findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>();
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}
	
	@Test
	public void testHtmlTagWithOneAttribute() {
		browser.openUrl("http://acama-systems.de/");
		HtmlElement element = browser.query().cssSelector("html > body > div#page_margins").findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>(Arrays.asList("id"));
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}

	@Test
	public void testHtmlTagWithMultipleAttributes() {
		browser.openUrl("http://acama-systems.de/");
		HtmlElement element = browser.query().cssSelector("#topnav > a").findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>(Arrays.asList("class", "title", "href"));
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}
}
