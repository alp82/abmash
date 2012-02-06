package com.abmash.extraction;


import com.abmash.api.Browser;
import com.abmash.extraction.container.ExtractionContainer;
import com.abmash.parser.DocumentParser;
import com.abmash.parser.HtmlParser;

import java.util.ArrayList;


/**
 * Abstract class for extractors, which serve the purpose of finding interesting data.
 */
public abstract class Extractor {
	
	DocumentParser parser;
	ArrayList<ExtractionContainer> extractions = new ArrayList<ExtractionContainer>();
		
	public Extractor(Browser browser) {
		parser = new HtmlParser(browser);
	}
	
	// extraction handling
	
	public void perform() {
		try {
			extract();
		} catch (Exception e) {
			// TODO handle extraction exceptions
			e.printStackTrace();
		}
	}
	
	protected abstract void extract();
	
	// result handling
	
	public ArrayList<ExtractionContainer> getExtractions() {
		return extractions;
	}

	public ArrayList<String> getOutput() {
		ArrayList<String> output = new ArrayList<String>();
		for (ExtractionContainer extractionContainer: extractions) {
			output.add(getExtractionOutput(extractionContainer));
		}
		return output;
	}

	protected abstract String getExtractionOutput(ExtractionContainer extractionContainer);

}
