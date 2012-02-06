package com.abmash.extraction;


import com.abmash.api.Browser;
import com.abmash.extraction.container.ExtractionContainer;
import com.abmash.extraction.container.TextExtractionContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class WordExtractor extends FindExtractor {


	public WordExtractor(Browser browser, SearchContainer searchContainer) {
		super(browser, searchContainer);
	}

	@Override
	/**
	 * extraction instances need to be added to the class variable extractions 
	 */
	protected void extract() {
		String text = parser.getVisibleText();
		ArrayList<String> searchWords = searchContainer.getQueries();
		
		for (String searchWord: searchWords) {
			// find all matches
			Pattern pattern = Pattern.compile("\\w*" + searchWord + "\\w*");
		    Matcher matcher = pattern.matcher(text);

		    // process matches and add extractions
		    while (matcher.find()) {
			   	String wordMatch = matcher.group();
			   	String exactMatch = wordMatch.substring(wordMatch.indexOf(searchWord), wordMatch.indexOf(searchWord) + searchWord.length());
			   	// TODO windowed match
			   	String expandedMatch = 
			    	text.substring(matcher.start() - 10, matcher.start()) +
			    	"[" + exactMatch + "]" +
			    	text.substring(matcher.end(), matcher.end() + 10)
		    	;
			   	TextExtractionContainer extraction = new TextExtractionContainer();
			   	extraction.setWordMatch(wordMatch);
			    extraction.setExactMatch(exactMatch);
			    extraction.setExactMatchWithWindow(expandedMatch.trim().replace("\n", " "));
			    extractions.add(extraction);
		    }
		}
	}

	@Override
	protected String getExtractionOutput(ExtractionContainer extractionContainer) {
		return ((TextExtractionContainer) extractionContainer).getWordMatch();
	}

}
