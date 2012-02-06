package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;



public class Click extends ActionOnHtmlElement {
	
	public enum ClickType {
		CLICK,
		DOUBLECLICK,
		RIGHTCLICK,
	}

	private ClickType type;
	
	public Click(Browser browser, HtmlElement element, ClickType clickType) {
		super(browser, element);
		type = clickType;
	}

	protected void perform() throws Exception {
		if(element != null) {
			Action clickAction;
			switch (type) {
			case DOUBLECLICK:
				browser.log().info("Double-clicking: {}", element);
				clickAction = new Actions(browser.getWebDriver()).doubleClick(element.getSeleniumElement()).build();
				clickAction.perform();
				break;
			case RIGHTCLICK:
				browser.log().info("Right-clicking: {}", element);
				clickAction = new Actions(browser.getWebDriver()).contextClick(element.getSeleniumElement()).build();
				clickAction.perform();
				break;
			case CLICK:
			default:
				browser.log().info("Clicking: {}", element);
//				clickAction = new Actions(browser.getWebDriver()).click(element.getSeleniumElement()).build();
//				clickAction.perform();
				element.getSeleniumElement().click();
				break;
			}
		} else {
			browser.log().warn("Element to click does not exist");
		}
		
		// TODO click type
		/*if (type.equals("click")) {
			selenium.click(target);
		} else if (type.equals("mousedown")) {
			selenium.mouseDown(target);
		} else if (type.equals("mouseup")) {
			selenium.mouseUp(target);
		} else if (type.equals("dblclick")) {
			selenium.doubleClick(target);
		} else if(type.equals("focus")) {
			selenium.focus(target);
		}*/
	}
}
