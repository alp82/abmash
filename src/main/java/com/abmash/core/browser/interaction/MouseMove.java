package com.abmash.core.browser.interaction;



import com.abmash.api.Browser;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;


public class MouseMove extends ActionOnBrowser {
	
	private int x;
	private int y;

	public MouseMove(Browser browser, int x, int y) {
		super(browser);
		this.x = x;
		this.y = y;
	}
	
	protected void perform() throws Exception {
		browser.log().info("Moving mouse relatively by ({},{}).", x, y);
		// TODO Only working if native events are enabled on this platform
		Action action = new Actions(browser.getWebDriver()).moveByOffset(x, y).build();
		action.perform();
	}
}
