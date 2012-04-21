package com.abmash.api.browser;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;
import com.abmash.core.browser.JavaScriptResult;
import com.abmash.core.browser.Popup;
import com.abmash.core.browser.Popups;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;


/**
 * Interaction with browser windows, used by calling {@link Browser#window()}.
 * <p>
 * This class is used to create, close and refresh browser windows and to switch between them.
 * In addition, it supports the detection of popups and alerts.
 * 
 * @author Alper Ortac
 */
public class Window {
	
	private Browser browser;

	private String mainWindow= null;
	private String currentWindow= null;

	private Popups popups = new Popups();
	
	
	/**
	 * Constructs new BrowserWindow instance to interact with browser windows.
	 * 
	 * @param browser <code>Browser</code> instance to work with
	 */
	public Window(Browser browser) {
		this.browser = browser;
//		detectPopups();
//		detectAlerts();
	}
	
	// popup and alert handling
	
	/**
	 * Detects popups, not fully supported yet. 
	 */
	// TODO as event listener
	public void detectPopups() {
		// check current window handles
		Set<String> windowHandles = browser.getWebDriver().getWindowHandles();
		
		// check if there is a new popup
		for(String windowHandle: windowHandles) {
			// TODO handle new windows
			if(!popups.contains(windowHandle)) {
				if(mainWindow == null && popups.size() == 0) {
					browser.log().info("Main window detected: {}", windowHandle);
					mainWindow = windowHandle;
					currentWindow = mainWindow;
				} else if(!windowHandle.equals(mainWindow)) {
					browser.log().info("New popup detected: {}", windowHandle);
					popups.add(new Popup(browser, windowHandle, browser.getCurrentUrl()));
					// do not allow automatic focus of new popup window  
					switchToMainWindow();
				}
			}
		}
		
		// check if popups are removed
		Popups popupsWithoutRemoved = new Popups(popups);
		for(Popup popup: popups) {
			// TODO handle removed windows
			if(!windowHandles.contains(popup.getWindowHandle())) popupsWithoutRemoved.remove(popup);
		}
		popups = popupsWithoutRemoved;
	}
	
	public void checkForAlerts() {
//		String alertText = (String) browser.javaScript("return checkForWindowAlert()").getReturnValue();
//		String alertText = (String) new JavaScriptResult(((JavascriptExecutor) browser.getWebDriver()).executeScript("return checkForWindowAlert();")).getReturnValue();
//		if(!alertText.equals("")) {
//			browser.log().info("Alert detected: " + alertText);
//		}
	}
	
	/**
	 * Detects alerts, not fully supported yet. 
	 * @return {@link Alert} object if an alert was detected, or null if not 
	 */
	// TODO as event listener
	public Alert getAlert() {
		// check alerts
		
		Alert alert = null;
		
		try {
			// TODO PERFORMANCE PROBLEM BECAUSE OF TIMEOUT IF NO ALERT EXISTS
			alert = browser.getWebDriver().switchTo().alert();
		} catch (Exception e) {
		}
		return alert;
	}
	
	// window handling
	
	/**
	 * Refreshs the current page.
	 * 
	 * All found {@link HtmlElement} instances may lose their validity.
	 */
	public void refresh() {
		browser.log().info("Refreshing page");
		browser.getWebDriver().navigate().refresh();	
	}

	/**
	 * Switches to main content in the current window. 
	 */
	public void switchToMainContent() {
//		browser.log().debug("Switching to main content in window");
		browser.getWebDriver().switchTo().defaultContent();
	}
	
	
	/**
	 * Switches to main window.
	 */
	public void switchToMainWindow() {
//		browser.log().debug("Switching to main window");
		switchToWindow(mainWindow);
	}
	
	/**
	 * Switches to the last opened popup.
	 */
	public void switchToLastOpenedPopup() {
		Popup popup = popups.get(popups.size() - 1);
		switchToPopup(popup);
	}
	
	/**
	 * Switches to the specified popup.
	 * 
	 * @param popup
	 */
	public void switchToPopup(Popup popup) {
		switchToWindow(popup.getWindowHandle());
	}
	
	/**
	 * Switches to window with specified name or handle.
	 * 
	 * @param windowName
	 */
	public void switchToWindow(String windowName) {
		browser.log().debug("Switching to window: " + windowName);
		browser.getWebDriver().switchTo().window(windowName);
		currentWindow = windowName;
	}
	
	/**
	 * Creates new window and goes to specified URL.
	 * 
	 * @param url
	 */
//	public String newWindow(String url) {
//		// TODO new window
//		String windowName = "newtab";
//		String script = "window.open('" + url + "', '" + windowName + "');";
//		browser.javaScript(script).getReturnValue();
//		switchTo(windowName);
//		return windowName;
//	}
	
	/**
	 * Closes current window
	 */
//	public void closeCurrentWindow() {
//		// TODO remove popup
//		String script = "window.close()";    
//		browser.javaScript(script).getReturnValue();
//		switchToMainWindow();
//	}
	
	/**
	 * Closes all popup windows (all windows except the initial main window).
	 * 
	 * Exits the browser application if the main window was closed beforehand.
	 */
	public void closeAllPopups() {
		for(Popup popup: popups) {
			closePopup(popup);
		}
		popups.clear();
	}
	
	/**
	 * Closes specified popup.
	 * 
	 * Exits the browser application if it was the last open window.
	 * 
	 * @param popup
	 */
	public void closePopup(Popup popup) {
		if(popups.contains(popup)) {
			closeWindow(popup.getWindowHandle());
		}
	}	
	
	/**
	 * Closes window with specified handle.
	 * 
	 * Exits the browser application if it was the last open window.
	 * 
	 * @param windowHandle
	 */
	public void closeWindow(String windowHandle) {
		browser.getWebDriver().switchTo().window(windowHandle).close();
		if(windowHandle.equals(mainWindow)) {
			browser.log().debug("Main window closed");
			// TODO main window handling, which is now the main window?
			mainWindow = null;
		} else {
			browser.log().debug("window closed: " + windowHandle);
			popups.remove(windowHandle);
		}
	}	
	
	/**
	 * Tabs are not supported yet. Uses {@link Window#newWindow(String)}
	 * 
	 * @param url
	 * @return name of the new tab
	 */
//	public String newTab(String url) {
//		// TODO new tab
//		return newWindow(url);
//	}

	/**
	 * Tabs are not supported yet. Uses {@link Window#closeWindow(String)}
	 * 
	 * @param tabName
	 */
//	public void closeTab(String tabName) {
//		// TODO close tab
//		closeWindow(tabName);
//	}

	// content-type and validation
	
	/**
	 * Not implemented yet 
	 */
//	public void validate() {
//		// TODO window validation
//	}

	/**
	 * Gets current name of active window.
	 * 
	 * @return name of currently focused window
	 */
	public String getCurrentWindowName() {
		// TODO not reliable
		return currentWindow;
	}

	/**
	 * Gets current popups.
	 * 
	 * @return all popups windows
	 */
	public Popups getPopups() {
		detectPopups();
		return popups;
	}
	
	/**
	 * Gets content type of current page.
	 * 
	 * @return content type
	 */
	public String getCurrentContentType() {
		String contentType = null;
		try {
			contentType = getContentTypeOfURL(new URL(browser.getWebDriver().getCurrentUrl()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contentType;
	}

	/**
	 * Gets content type for specified {@link URL}.
	 * 
	 * @param url
	 * @return content type
	 */
	public String getContentTypeOfURL(URL url) {
		// alternative approach
//		try {
//			URLConnection uc = url.openConnection();
//			String contentType = uc.getContentType();
//			browser.log().debug("Content type of target url: {}", contentType);
//			return contentType;
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}

		HttpClient httpclient = new DefaultHttpClient();
		HttpHead httphead = null;
		try {
			httphead = new HttpHead(url.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpResponse response = null;
		Header contentType = null;
		
		try {
			response = httpclient.execute(httphead);
//			Header[] headers = response.getAllHeaders();
//			for (int i = 0; i < headers.length; i++) {
//				Header header = headers[i];
//				browser.log().debug(header.getName() + " :: " + header.getValue());
//			}
			contentType = response.getFirstHeader("Content-Type");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//return entity instanceof HttpEntity ? entity.getContentType().getValue() : "";
		return contentType instanceof Header ? contentType.getValue() : null;
	}

}
