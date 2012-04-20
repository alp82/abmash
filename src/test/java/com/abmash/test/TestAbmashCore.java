package com.abmash.test;

import static org.junit.Assert.*;

//import static org.hamcrest.core.Is.is;
//import static org.hamcrest.core.IsNot.not;
//import static org.hamcrest.core.IsEqual.equalTo;
//import static org.hamcrest.core.IsInstanceOf.instanceOf;
//import static org.hamcrest.core.IsSame.sameInstance;
//import static org.hamcrest.core.IsNull.*;
//import static org.hamcrest.core.AllOf.*;
//import static org.hamcrest.core.AnyOf.*;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.abmash.api.query.QueryFactory.*;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.core.browser.Popups;

public class TestAbmashCore {

	static Browser browser;
	
	@BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code      
		browser = new Browser();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    	browser.close();
    }
    
    // HTMLQuery tests
    
	// TODO http://acama-systems.de/contact errors

    @Test
    public void bodyElementIsFoundExactlyOnceWithJQuery() {
		goToUrlAndDontReloadIfAlreadyOpen("http://acama-systems.de/");
    	HtmlElements elements = browser.query(select("html > body")).find();
    	assertEquals(1, elements.size());
    	assertEquals("body", elements.first().getTagName());
    }
    
    @Test
    public void bodyElementIsFoundExactlyOnceWithXPath() {
		goToUrlAndDontReloadIfAlreadyOpen("http://acama-systems.de/");
		HtmlElements elements = browser.query(xPath("//html/body")).find();
    	assertEquals(1, elements.size());
    	assertEquals("body", elements.first().getTagName());
    }
    
    @Test
    public void elementsAreFoundBySelectPredicate() {
		goToUrlAndDontReloadIfAlreadyOpen("http://acama-systems.de/");
		HtmlElements elements = browser.query(select("a")).find();
		assertThat(elements.size(), not(is(0)));
		for (HtmlElement element: elements) {
			assertThat("a", equalTo(element.getTagName()));
		}
    }
    
    @Test
    public void typableElementsAreFound() {
		goToUrlAndDontReloadIfAlreadyOpen("http://acama-systems.de/");
		HtmlElements elements = browser.query(typable()).find();
		// TODO
//		assertThat(0, not(is(elements.size())));
    }
    
    @Test
	public void elementHasNoAttributes() {
		goToUrlAndDontReloadIfAlreadyOpen("http://acama-systems.de/");
		HtmlElement element = browser.query(select("html > body")).findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>();
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}
	
	@Test
	public void elementHasOneAttribute() {
		goToUrlAndDontReloadIfAlreadyOpen("http://acama-systems.de/");
		HtmlElement element = browser.query(select("html > body > div#page_margins")).findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>(Arrays.asList("id"));
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}

	@Test
	public void elementHasMultipleAttributes() {
		goToUrlAndDontReloadIfAlreadyOpen("http://acama-systems.de/");
		HtmlElement element = browser.query(select("#topnav > a")).findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>(Arrays.asList("class", "title", "href"));
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}
	
	@Test
	public void popupsAreNotFocused() {
		goToUrlAndDontReloadIfAlreadyOpen("http://jadranka.hr/");
		Popups popups = browser.window().getPopups();
		HtmlElements htmllinks = browser.query(select("a")).find();
		assertThat(popups.size(), is(1));
		assertThat(htmllinks.size(), greaterThan(1));
	}
	
	private void goToUrlAndDontReloadIfAlreadyOpen(String url) {
		if(!browser.getCurrentUrl().equals(url)) {
			browser.openUrl(url);
		}
	}
}
