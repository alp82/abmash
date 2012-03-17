package com.abmash.test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsEqual.equalTo;
//import static org.hamcrest.core.IsInstanceOf.instanceOf;
//import static org.hamcrest.core.IsSame.sameInstance;
//import static org.hamcrest.core.IsNull.*;
//import static org.hamcrest.core.AllOf.*;
//import static org.hamcrest.core.AnyOf.*;
import java.util.Arrays;
import java.util.HashSet;

import org.hamcrest.core.IsEqual;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;

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
    
    /*@Before
	public void setUp() throws Exception {
		browser = new Browser(null);
	}
	
	@After
	public void tearDown() throws Exception {
		browser.close();
	}*/
    
    // HTMLQuery tests
    
    @Test
    public void bodyElementIsFoundExactlyOnceWithJQuery() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
    	HtmlElements elements = browser.query().jQuerySelector("find('html > body')").find();
    	assertEquals(1, elements.size());
    	assertEquals("body", elements.first().getTagName());
    }
    
    @Test
    public void bodyElementIsFoundExactlyOnceWithCSS() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
		HtmlElements elements = browser.query().cssSelector("html > body").find();
    	assertEquals(1, elements.size());
    	assertEquals("body", elements.first().getTagName());
    }
    
    @Test
    public void bodyElementIsFoundExactlyOnceWithXPath() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
		HtmlElements elements = browser.query().xPathSelector("html/body").find();
    	assertEquals(1, elements.size());
    	assertEquals("body", elements.first().getTagName());
    }
    
    @Test
    public void elementsAreFoundByTagSelector() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
		HtmlElements elements = browser.query().tag("a").find();
		assertThat(elements.size(), not(is(0)));
		for (HtmlElement element: elements) {
			assertThat("a", equalTo(element.getTagName()));
		}
    }
    
    @Test
    public void typableElementsAreFound() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
		HtmlElements elements = browser.query().isTypable().find();
		// TODO
//		assertThat(0, not(is(elements.size())));
    }
    
    @Test
	public void elementHasNoAttributes() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
		HtmlElement element = browser.query().jQuerySelector("find('html > body')").findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>();
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}
	
	@Test
	public void elementHasOneAttribute() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
		HtmlElement element = browser.query().jQuerySelector("find('html > body > div#page_margins')").findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>(Arrays.asList("id"));
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}

	@Test
	public void elementHasMultipleAttributes() {
		goToAcamaSystemsStartPageIfNotAlreadyOpen();
		HtmlElement element = browser.query().jQuerySelector("find('#topnav > a')").findFirst();
		HashSet<String> expctdAttributeNames = new HashSet<String>(Arrays.asList("class", "title", "href"));
		HashSet<String> actualAttributeNames = new HashSet<String>(element.getAttributeNames());
		assertEquals(expctdAttributeNames, actualAttributeNames);
	}
	
	private void goToAcamaSystemsStartPageIfNotAlreadyOpen() {
		// TODO http://acama-systems.de/contact errors
		if(!browser.getCurrentUrl().equals("http://acama-systems.de/")) {
			browser.openUrl("http://acama-systems.de/");
		}
	}
}
