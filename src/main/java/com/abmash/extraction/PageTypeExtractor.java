package com.abmash.extraction;


import com.abmash.api.Browser;
import com.abmash.extraction.container.ExtractionContainer;
import com.abmash.extraction.container.PageTypeExtractionContainer;
import com.abmash.parser.content.Link;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



public class PageTypeExtractor extends ExtractorWithDB {

	private enum Status {
		OK,					// page type could be extracted 
		ERROR,				// page not loadable
		OFFLINE,			// page loadable but content not available due to domain change or domain parking
	}
	
	private enum PageType {
		PRE_PAGE,					// start pages like intros or language selectors
		
		// links contain "common_links"
		HOTEL,						// regular hotel page
		HOTEL_SUBPAGE,				// subpage of regular hotel page
		HOTEL_GROUP_PORTAL,			// 3 or more links contain long paths with at least 3 "/" in it
		HOTEL_GROUP_PORTAL_SUBPAGE, // subpage of hotel group portals

		// links contain "top_links"
		HOTEL_CHAIN,				// hotel chain
		SMALL_HOTEL_CHAIN,			// hotel chain page with less than 10 links

		// links do not contain "common_links"
		NO_HOTEL,					// visible text does not contain "top_links" or "common_links"
		HOTEL_SUBPAGE_UNSURE,		// visible text does contain "top_links" or "common_links"
		HOTEL_SINGLEPAGE,			// no links at all
	}
	
	private Status status = null;
	private PageType pageType = null;

	public PageTypeExtractor(Browser browser, Connection conn) {
		super(browser, conn);
	}

	@Override
	/**
	 * extraction instances need to be added to the class variable extractions 
	 */
	protected void extract() {
		String url = parser.getUrl();
		// do something with url
		
		String title = parser.getTitle();
		// do something with title

		HashMap<String, String> metaTags = parser.getMetaTags();
		for (String metaTag: metaTags.keySet()) {
			// do something with metatags
		}
		
		String visibleText = parser.getVisibleText();
		// do something with visible text

		ArrayList<Link> links = parser.getLinks();
		for (Link link: links) {
			// do something with links
		}
		
		// if you need more fine-grained control of finding the information
		// you need, you can use the browser instance
//		HtmlElementList elements = browser.find().textElements("hotel");

		// you can even interact with the browser
//		browser.click("english");
//		browser.type("search", "Hotel Foobar");
		
		// set status and found most probable page type
		status = Status.OK;
		pageType = PageType.HOTEL;
		
		// add result to extraction container
		PageTypeExtractionContainer extraction = new PageTypeExtractionContainer();
	   	extraction.setStatus(status.name());
	   	extraction.setPageType(pageType.name());
	    extractions.add(extraction);
	}
	
	
	@Override
	protected String getExtractionOutput(ExtractionContainer extractionContainer) {
		return ((PageTypeExtractionContainer) extractionContainer).getPageType();
	}

}
