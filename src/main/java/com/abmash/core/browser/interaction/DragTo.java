package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.ButtonReleaseAction;
import org.openqa.selenium.interactions.ClickAndHoldAction;
import org.openqa.selenium.interactions.MoveMouseAction;
import org.openqa.selenium.internal.Locatable;



public class DragTo extends ActionOnHtmlElement {
	
	HtmlElement targetElement;

	protected Mouse mouse;
	protected Keyboard keyboard;

	public DragTo(Browser browser, HtmlElement sourceElement, HtmlElement targetElement) {
		super(browser, sourceElement);
		this.targetElement = targetElement;
		
	    mouse = ((HasInputDevices) browser.getWebDriver()).getMouse();
	    keyboard = ((HasInputDevices) browser.getWebDriver()).getKeyboard();
	}
	
	protected void perform() throws Exception {
		if(element != null) {
			if(targetElement != null) {
				browser.log().info("Dragging {} to {}", element, targetElement);
				// TODO Only working if native events are enabled on this platform
			    new ClickAndHoldAction(mouse, (Locatable) element.getSeleniumElement()).perform();
			    new MoveMouseAction(mouse, (Locatable) targetElement.getSeleniumElement()).perform();
			    new ButtonReleaseAction(mouse, null).perform();
//				Action dragAction = new Actions(browser.getWebDriver()).dragAndDrop(element.getSeleniumElement(), targetElement.getSeleniumElement()).build();
//				dragAction.perform();
//				element.getSeleniumElement().dragAndDropOn(targetElement.getSeleniumElement());
			} else {
				browser.log().warn("Target element to drag on does not exist");
			}
		} else {
			browser.log().warn("Element to drag does not exist");
		}
	}
}
