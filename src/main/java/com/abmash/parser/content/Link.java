package com.abmash.parser.content;


public class Link {
	
	private String name;
	private String url;
	
	public String toString() {
		return name + ":" + url;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String string) {
		this.url = string;
	}

}
