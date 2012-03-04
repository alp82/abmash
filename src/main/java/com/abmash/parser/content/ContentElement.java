package com.abmash.parser.content;

import java.util.HashMap;

import com.abmash.core.element.Location;
import com.abmash.core.element.Size;

public class ContentElement {
	
	private String tagName;
	private String text;
	private Location location;
	private Size size;
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
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public void addAttribute(String attribute, String value) {
		attributes.put(attribute, value);
	}


}
