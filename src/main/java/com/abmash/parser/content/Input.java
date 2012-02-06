package com.abmash.parser.content;

public class Input {
	
	private String label;
	private String type;
	private String name;
	
	public String toString() {
		return (label.length() > 0 ? label : type) + ":" + name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
