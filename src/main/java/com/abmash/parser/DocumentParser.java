package com.abmash.parser;


import com.abmash.parser.content.ContentElement;
import com.abmash.parser.content.Header;
import com.abmash.parser.content.Image;
import com.abmash.parser.content.Input;
import com.abmash.parser.content.Link;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class DocumentParser {
	
	String url;
	String sourceText;
	String visibleText;
	String title;
	HashMap<String, String> metaTags;
	ArrayList<Header> headers;
	ArrayList<Link> links;
	ArrayList<Image> images;
	ArrayList<Input> inputs;
	ArrayList<ContentElement> customElements;
	ArrayList<ContentElement> periodicElements;
	
	public DocumentParser(String source) {
		sourceText = source;
	}
	
	// general parser methods
	
	protected abstract void parseUrl();
	
	/**
	 * parse visible text
	 */
	protected abstract void parseVisibleText();

	/**
	 * parse title
	 */
	protected abstract void parseTitle();

	protected abstract void parseMetaTags();

	protected abstract void parseHeaders();

	protected abstract void parseLinks();

	protected abstract void parseImages();

	protected abstract void parseInputs();

	// custom parser methods
	
	// TODO general methods with custom selector query
	
	public abstract void parsePeriodicElements();

	// getter methods
	
	public String getUrl() {
		if(url == null) {
			parseUrl();
		}
		return url;
	}
	public String getSourceText() {
		return sourceText;
	}
	public String getVisibleText() {
		if(visibleText == null) {
			parseVisibleText();
		}
		return visibleText;
	}
	public String getTitle() {
		if(title == null) {
			parseTitle();
		}
		return title;
	}
	public HashMap<String, String> getMetaTags() {
		if(metaTags == null) {
			metaTags = new HashMap<String, String>();
			parseMetaTags();
		}
		return metaTags;
	}
	public ArrayList<Header> getHeaders() {
		if(headers == null) {
			headers = new ArrayList<Header>();
			parseHeaders();
		}
		return headers;
	}
	public ArrayList<Link> getLinks() {
		if(links == null) {
			links = new ArrayList<Link>();
			parseLinks();
		}
		return links;
	}
	public ArrayList<Image> getImages() {
		if(images == null) {
			images = new ArrayList<Image>();
			parseImages();
		}
		return images;
	}
	public ArrayList<Input> getInputs() {
		if(inputs == null) {
			inputs = new ArrayList<Input>();
			parseInputs();
		}
		return inputs;
	}
	
	public ArrayList<ContentElement> getPeriodicElements() {
		periodicElements = new ArrayList<ContentElement>();
		parsePeriodicElements();
		return periodicElements;
	}
}
