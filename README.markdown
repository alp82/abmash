Abmash
======
Abmash is about: **Imitation of human interactions with a web browser, based on the visible rendered output.**

Getting Started
---------------
Abmash is all about interacting with a browser as humans would do. Including AJAX and forms.

The goal is that programmers do not need any knowledge about the HTML/CSS source code of web pages.
Instead, elements can be found by querying for visual attributes and visible text.

TODO Screencast

*Example (Java):*

	// open new browser window
	Browser browser = new Browser("http://google.com");
  
	// type text in search query input field and submit the search
	HtmlElement searchBox = browser.type("Browser Automation").submit();
  
	// find the first result (conditions can be arbitrarily chained)
	HtmlElement firstResult = browser.query().isTitle().isLink().below(searchBox).findFirst();
  
	// click it
	firstResult.click();

Query conditions like `below()` are based on the visual representation of the web
page, independently from the page source and DOM structure. Correspondingly,
`isTitle()` not only selects `<h1>`, `<h2>`, ... elements, but also elements with a
bigger font-size than the average on the current page.

Color Queries
-------------
Colors are an important part of the visual representation of a web page. Therefore, Abmash allows you to
find elements by their color. Example:

	HtmlElements blueImages = browser.query().isImage().hasColor("blue", Tolerance.LOW, Dominance.HIGH).find();

A list of all colors can be found at [Recognized color keyword names](http://www.w3.org/TR/SVG/types.html#ColorKeywords).
The *tolerance* defines how close the specified color has to match.
The *dominance* controls how many percent of the element's pixels need to match these constraints.
By the way, images are also elements with a background image.

Documentation
-------------
TODO Javadoc link

Implementation Details
----------------------
Abmash is a Java framework to automate web applications, based on **[Selenium](http://seleniumhq.org/)**,
**[Firefox](http://www.mozilla.org/firefox/)** and **[jQuery](http://jquery.com/)**.

**Abmash is still under heavy development. Please let me know if you are interested in this project.**

Maven
-----
Abmash snapshots are available as Maven repositories.

First put the following repository in your `pom.xml`:

	<repositories>
		<repository>
			<id>alp-snapshots</id>
			<url>https://github.com/alp82/alp-mvn-repo/raw/master/snapshots</url>
		</repository>
	</repositories>

Then add this dependency:

	<dependencies>
		<dependency>
			<groupId>com.abmash</groupId>
			<artifactId>abmash</artifactId>
			<version>0.1.8-SNAPSHOT</version>
			<type>jar</type>
			<scope>compile</scope>
		</dependency>
	</dependencies>

*TODO more information*