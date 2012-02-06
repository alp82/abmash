package com.abmash.parser.content;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

public class ContentElement {
	
	private String tagName;
	private String text;
	private Point location;
	private Dimension size;
	private HashMap<String, String> attributes = new HashMap<String, String>();
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public Dimension getSize() {
		return size;
	}
	public void setSize(Dimension size) {
		this.size = size;
	}
	public void addAttribute(String attribute, String value) {
		attributes.put(attribute, value);
	}


}
