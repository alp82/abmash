package com.abmash.extraction.container;

public class TextExtractionContainer extends ExtractionContainer {
	
	private String wordMatch;
	private String exactMatch;
	private String exactMatchWithWindow;
	
	public String getWordMatch() {
		return wordMatch;
	}
	public void setWordMatch(String wordMatch) {
		this.wordMatch = wordMatch;
	}
	public String getExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(String exactMatch) {
		this.exactMatch = exactMatch;
	}
	public String getExactMatchWithWindow() {
		return exactMatchWithWindow;
	}
	public void setExactMatchWithWindow(String exactMatchWithWindow) {
		this.exactMatchWithWindow = exactMatchWithWindow;
	}	
}
