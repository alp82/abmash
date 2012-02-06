package com.abmash.parser.content;


public class Image {
	
	private String title;
	private String alt;
	private String url;
	private byte[] raw;
	
	public String toString() {
		return (title.length() > 0 ? title : alt) + ":" + url;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String string) {
		this.url = string;
	}
	public byte[] getRaw() {
		return raw;
	}
	public void setRaw(byte[] raw) {
		this.raw = raw;
	}

}
