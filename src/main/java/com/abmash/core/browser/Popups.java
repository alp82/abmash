package com.abmash.core.browser;

import java.util.ArrayList;

public class Popups extends ArrayList<Popup> {

	public Popups() {
		super();
	}
	
	public Popups(Popups popups) {
		this();
		for(Popup popup: popups) {
			this.add(popup);
		}
	}
	

	public boolean contains(String handle) {
		for(Popup popup: this) {
			if(popup.getWindowHandle().equals(handle)) return true;
		}
		return false;
	}
	
	public boolean remove(String handle) {
		for(Popup popup: this) {
			if(popup.getWindowHandle().equals(handle)) return remove(this);
		}
		return false;
	}
	
}
