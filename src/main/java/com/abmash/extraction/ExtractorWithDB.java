package com.abmash.extraction;


import com.abmash.api.Browser;

import java.sql.Connection;


/**
 * Abstract class for extractors, which are parametrized by search query input
 */
public abstract class ExtractorWithDB extends Extractor {
	
	Connection conn;
		
	public ExtractorWithDB(Browser browser, Connection conn) {
		super(browser);
		this.conn = conn;
	}

}
