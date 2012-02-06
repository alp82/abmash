package com.abmash.extraction;


import com.abmash.api.Browser;
import com.abmash.extraction.container.ExtractionContainer;
import com.abmash.parser.DocumentParser;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Abstract class for extractors, which are parametrized by search query input
 */
public abstract class FindExtractor extends Extractor {
	
	SearchContainer searchContainer;
		
	public FindExtractor(Browser browser, SearchContainer search) {
		super(browser);
		searchContainer = search;
	}

}
