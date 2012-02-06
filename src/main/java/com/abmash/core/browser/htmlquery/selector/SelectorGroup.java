package com.abmash.core.browser.htmlquery.selector;


import java.util.ArrayList;


public class SelectorGroup extends ArrayList<Selector> {
	
	public enum Type {
		NORMAL,
		FALLBACK
	}
	
	private Type type = Type.NORMAL; // normal or fallback group
	private int limit = 50; // result limit for this group
	
	public SelectorGroup() {
		super();
	}

	public SelectorGroup(Selector selector) {
		super();
		add(selector);
	}

	public SelectorGroup(Type type) {
		this();
		this.type = type;
	}

	public SelectorGroup(Type type, int limit) {
		this(type);
		this.limit = limit;
	}
	
	public SelectorGroup(Selector selector, Type type) {
		this(selector);
		this.type = type;
	}

	public SelectorGroup(Selector selector, int limit) {
		this(selector);
		this.limit = limit;
	}

	public SelectorGroup(SelectorGroup group, Type type, int limit) {
		super();
		addAll(group);
		this.type = type;
		this.limit = limit;
	}
	
	public SelectorGroup(Selector selector, Type type, int limit) {
		this(new SelectorGroup(selector), type, limit);
	}
	
	public boolean add(Selector selector) {
		if(selector instanceof Selector) super.add(selector);
		return true;
	}

	public int getLimit() {
		return limit;
	}

	public SelectorGroup setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public Type getType() {
		return type;
	}

	public SelectorGroup setType(Type type) {
		this.type = type;
		return this;
	}
	
}