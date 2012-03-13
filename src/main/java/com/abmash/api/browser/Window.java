package com.abmash.api.browser;


import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;

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

	private ArrayList<String> windowHandles = new ArrayList<String>();
	
	private String currentWindowName;
	
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
		// check window handles
		String handle = browser.getWebDriver().getWindowHandle();
		if(!windowHandles.contains(handle)) {
			browser.log().debug("New window handle detected: {}", handle);
			Set<String> winHandles = browser.getWebDriver().getWindowHandles();
			for(String winHandle: winHandles) {
				// TODO handle new windows
				if(!windowHandles.contains(winHandle)) windowHandles.add(winHandle);
			}
			ArrayList<String> windowHandlesNew = new ArrayList<String>(windowHandles);
			for(String windowHandle: windowHandles) {
				// TODO handle removed windows
				if(!winHandles.contains(windowHandle)) windowHandlesNew.remove(windowHandle);
			}
			windowHandles = windowHandlesNew;
		}		
	}
	
	/**
	 * Detects alerts, not fully supported yet. 
	 * @return {@link Alert} object if an alert was detected, or null if not 
	 */
	// TODO as event listener
	public Alert getAlert() {
		// check alerts
		
		/*for (String windowHandle: browser.getWebDriver().getWindowHandles()) {
		}
		System.out.println(" WINDOW HANDLES: " + browser.getWebDriver().getWindowHandles());*/
		
		try {
			// TODO PERFORMANCE PROBLEM BECAUSE OF TIMEOUT IF NO ALERT EXISTS
			Alert alert = browser.getWebDriver().switchTo().alert();
			
			// check if alert exists
			if(alert != null) {
				String alertText = alert.getText();
				
				// TODO alert handling
				browser.log().info("Alert detected: {}", alertText);
				return alert;
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	// window handling
	
	/**
	 * Refreshs the current page. All found {@link HtmlElement} instances may lose their validity.
	 */
	public void refresh() {
		browser.log().info("Refreshing page");
		browser.getWebDriver().navigate().refresh();	
	}
	
	/**
	 * Switches to main window 
	 */
	public void switchToMain() {
		try {
//			browser.log().debug("Switching to main window");
			browser.getWebDriver().switchTo().defaultContent();
			currentWindowName = null;
		} catch (Exception e) {
			browser.log().debug("Main window to switch to not found, attempting to use existing window handles");
			String windowHandle = windowHandles.get(0);
			switchTo(windowHandle);
		}
	}
	
	/**
	 * Switches to window with specified name.
	 * 
	 * @param windowName
	 */
	public void switchTo(String windowName) {
		browser.log().debug("Switching to window: " + windowName);
		browser.getWebDriver().switchTo().window(windowName);
		currentWindowName = windowName;
	}

	/**
	 * Creates new window and goes to specified URL.
	 * 
	 * @param url
	 */
	public String newWindow(String url) {
		// TODO new window
		String windowName = "newtab";
		String script = "window.open('" + url + "', '" + windowName + "');";
		browser.javaScript(script).getReturnValue();
		switchTo(windowName);
		return windowName;
	}
	
	/**
	 * Closes window with specified name.
	 * 
	 * @param windowName
	 */
	public void closeWindow(String windowName) {
		String script = "window.close()";    
		browser.javaScript(script).getReturnValue();
		switchToMain();
	}
	
	/**
	 * Tabs are not supported yet. Uses {@link Window#newWindow(String)}
	 * 
	 * @param url
	 * @return name of the new tab
	 */
	public String newTab(String url) {
		// TODO new tab
		return newWindow(url);
	}

	/**
	 * Tabs are not supported yet. Uses {@link Window#closeWindow(String)}
	 * 
	 * @param tabName
	 */
	public void closeTab(String tabName) {
		// TODO close tab
		closeWindow(tabName);
	}

	// content-type and validation
	
	/**
	 * Not implemented yet 
	 */
	public void validate() {
		// TODO window validation
	}

	/**
	 * Gets current name of active window.
	 * 
	 * @return name of currently focused window
	 */
	public String getCurrentWindowName() {
		return currentWindowName;
	}

	/**
	 * Gets current window handles.
	 * 
	 * @return all window handles
	 */
	public ArrayList<String> getWindowHandles() {
		return windowHandles;
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
