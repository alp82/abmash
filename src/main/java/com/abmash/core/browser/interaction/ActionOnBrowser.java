package com.abmash.core.browser.interaction;


import com.abmash.api.Browser;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public abstract class ActionOnBrowser {
	
	protected Browser browser;
	
	public ActionOnBrowser(Browser browser) {
		this.browser = browser;
	}
	
	public void execute() {
		try {
			// first detect potentially new dialogs and windows
//			Alert alert = browser.window().getAlert();
//			if(alert instanceof Alert) {
//				browser.log().info("Alert detected: " + alert.getText());
//				alert.accept();
//			}
//			browser.window().checkForAlerts();
			browser.window().detectPopups();
			
			// TODO validate presence of correct window
			// TODO after switchToMain...
//			browser.window().validate();
			
			// finally perform the action
			performBefore();
			perform();
			performAfter();
		} catch (Exception e) {
			try {
				// TODO only if webdriver supports taking screenshots
				File screenshotFile = ((TakesScreenshot) browser.getWebDriver()).getScreenshotAs(OutputType.FILE);
				// TODO save all screenshots
				FileUtils.copyFile(screenshotFile, new File("output/error/lastErrorScreenshot.png"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//TODO no need anymore?
			//if(!(this instanceof JavaScript)) e.printStackTrace();
		}
	}
	
	protected abstract void perform() throws Exception;

	protected void performBefore() {
		// TODO workaround for: org.openqa.selenium.WebDriverException: this.getWindow() is null
		// TODO collides when using find()
//		browser.window().switchToMain();
	}

	protected void performAfter() {
		// do nothing per default
	}

}
