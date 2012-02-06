package com.abmash.parser.content;


public class Header {
	
	private String text;
	private String size;
	
	public String toString() {
		return text + ":" + size;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String string) {
		this.size = string;
	}

}
