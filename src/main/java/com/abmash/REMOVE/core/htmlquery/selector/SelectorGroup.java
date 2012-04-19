package com.abmash.REMOVE.core.htmlquery.selector;


import java.util.ArrayList;

import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;


public class SelectorGroup extends ArrayList<Selector> {
	
	public enum Type {
		NORMAL,
		LABEL,
		FALLBACK, 
	}
	
	private int limit = 1000; // result limit for this group
	private int weight = 0; // the higher the value the higher the weight 
	private Type type = Type.NORMAL; // normal or fallback group
	private HtmlElements referenceElements = new HtmlElements();
	
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

	public SelectorGroup(Type type, int limit, int weight) {
		this(type, limit);
		this.weight = weight;
	}
	
	public SelectorGroup(Selector selector, Type type) {
		this(selector);
		this.type = type;
	}

	public SelectorGroup(Selector selector, int limit) {
		this(selector);
		this.limit = limit;
	}

	public SelectorGroup(Selector selector, int limit, int weight) {
		this(selector, limit);
		this.weight = weight;
	}
	
	public SelectorGroup(SelectorGroup group, Type type, int limit) {
		super();
		addAll(group);
		this.type = type;
		this.limit = limit;
	}

	public SelectorGroup(SelectorGroup group, Type type, int limit, int weight) {
		this(group, type, limit);
		this.weight = weight;
	}
	
	public SelectorGroup(Selector selector, Type type, int limit) {
		this(new SelectorGroup(selector), type, limit);
	}
	
	public SelectorGroup(Selector selector, Type type, int limit, int weight) {
		this(selector, type, limit);
		this.weight = weight;
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

	public int getWeight() {
		return weight;
	}

	public SelectorGroup setWeight(int weight) {
		this.weight = weight;
		return this;
	}
	
	public Type getType() {
		return type;
	}

	public SelectorGroup setType(Type type) {
		this.type = type;
		return this;
	}
	
	public void addReferenceElement(HtmlElement element) {
		referenceElements.add(element);
	}
	
	public void addReferenceElements(HtmlElements elements) {
		referenceElements.addAll(elements);
	}
	
	public HtmlElements getReferenceElements() {
		return referenceElements;
	}

	public boolean hasReferenceElements() {
		return referenceElements.size() > 0;
	}

}