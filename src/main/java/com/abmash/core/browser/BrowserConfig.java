package com.abmash.core.browser;


import com.abmash.api.Browser;
import com.abmash.core.tools.IOTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


/**
 * Custom configuration of browser instance 
 */
public class BrowserConfig {

	private static final String DEFAULT_PROPERTY_FILE = "/default.properties";
	
	private RemoteWebDriver webDriver;

	public BrowserConfig() {
		this(DEFAULT_PROPERTY_FILE);
	}
	
	/**
	 * @param propertyFile
	 * @throws IOException 
	 */
	public BrowserConfig(String propertyFile) {
		Properties properties = new Properties();
		try {
			InputStream stream = getClass().getResourceAsStream(propertyFile);
			properties.load(stream);
			stream.close();
			if(properties.getProperty("browserType").isEmpty() && (!properties.getProperty("browserType").equalsIgnoreCase("htmlunit") || properties.getProperty("browserBin").isEmpty())) throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// headless mode 
		if(properties.containsKey("headless")) System.setProperty("java.awt.headless", properties.getProperty("headless"));
		
		if(properties.getProperty("browserType").equalsIgnoreCase("firefox")) {
			// TODO use Firefox3Locator
			File binaryFile = new File(properties.getProperty("browserBin"));
			FirefoxBinary binary = new FirefoxBinary(binaryFile);
			
			// can be used for headless mode. (Xvfb needs to be running for the specified display)
			if(properties.containsKey("display")) {
				binary.setEnvironmentProperty("DISPLAY", properties.getProperty("display"));
			}
			FirefoxProfile profile = new FirefoxProfile();
			// enable native events
			profile.setEnableNativeEvents(true); 
			 // single window mode
//			firefoxProfile.setPreference("browser.link.open_newwindow", 3);
			profile.setPreference("browser.link.open_newwindow.restriction", 2);
//			firefoxProfile.setPreference("browser.link.open_external", 3);
			// install extensions
			try {
				profile.addExtension(IOTools.convertStreamToFile(BrowserConfig.class.getResourceAsStream("/firefox/JSErrorCollector.xpi"), "xpi"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			webDriver = (RemoteWebDriver) new FirefoxDriver(binary, profile);
		} else if(properties.getProperty("browserType").equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", properties.getProperty("browserBin"));
			webDriver = (RemoteWebDriver) new ChromeDriver();
		}
		
		// TODO htmlunit does not work yet (RemoteWebElement is Firefox specific, CSS selectors do not work properly)
//		if(properties.getProperty("browserType").equalsIgnoreCase("htmlunit")) {
//			webDriver = (RemoteWebDriver) new HtmlUnitDriver();
//		}
	}
	
	public RemoteWebDriver getWebDriver() {
		return webDriver;
	}
}
