/**
 * Provides the main classes to control browsers and to find and interact with elements on the current web page.
 * <p>
 * <strong>Example Application:</strong>
 * <pre>{@code
 * Browser browser = new Browser("http://example.com");       // open browser with specified URL
 * browser.click("Contact");                                  // click on a link labeled "Contact" (case-sensitive)
 * browser.type("Name", "John");                              // enter text into the textfield labeled "Name"
 * browser.type("Message", "This is an example").submit();    // enter text into the textarea labeled "Message"
 * HtmlElement successMessage = browser.query(headline(), has("Success")).findFirst();
 * assertEquals(successMessage instanceof HtmlElement, true); // make sure that submit was successful 	
 * }</pre>
 * <p>
 * <strong>Abmash</strong> ("Automated Browser for Mashups") is a framework which allows the developer to
 * directly interact with web applications as humans would do. The {@link com.abmash.api.Browser#click(String)} method
 * simulates a click on any element. Further, it is possible to simulate keyboard interaction
 * to enter text in an input field, or extracting content from specific page elements. The main classes of
 * com.abmash.are <code>Browser</code>, <code>HtmlElement</code>, <code>HtmlElements</code> and <code>HtmlQuery</code>.
 * <p>
 * {@link com.abmash.api.Browser} is used to start a new browser session. Its main purpose is to find {@link com.abmash.api.HtmlElements}
 * by using the {@link com.abmash.api.query.Query} methods, and interacting with them by using methods like
 * {@link com.abmash.api.Browser#click(String)} or {@link com.abmash.api.Browser#type(String, String)}.
 * <p>
 * <ul>
 * <li>{@link com.abmash.api.HtmlElement} represents an HTML element on the current page.</li>
 * <li>{@link com.abmash.api.HtmlElements} is a list of <code>HtmlElement</code> objects.</li>
 * <li>{@link com.abmash.api.query.Query} is used to find HTML elements on the current page.</li>
 * </ul>
 * <p>
 * An {@link com.abmash.api.HtmlElement} object is a representation of an HTML element on the current web page. It
 * can be used to interact with them, to parameterize other browser interaction tasks or to get the contents of that element.
 * {@link com.abmash.api.query.Query#findFirst()} returns an <code>HtmlElement</code>.
 * <p>
 * {@link com.abmash.api.HtmlElements} is a list of <code>HtmlElement</code> objects. It
 * can be used to interact with them, to parameterize other browser interaction tasks or to get the contents of that elements
 * {@link com.abmash.api.query.Query#find()} returns an <code>HtmlElements</code> object.
 * <p>
 * A {@link com.abmash.api.query.Query} contains of an arbitrary number of search conditions to find {@link com.abmash.api.HtmlElements}.
 * 
 * @see com.abmash.api.Browser
 * @see com.abmash.api.HtmlElement
 * @see com.abmash.api.HtmlElements
 * @see com.abmash.api.query.Query
 * @see com.abmash.api.query.QueryFactory
 */
package com.abmash.api;