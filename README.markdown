Abmash
======
**"Imitation of human interactions with a web browser, based on the visible rendered output."**

Getting Started
---------------
Abmash is all about interacting with a browser as humans would do. Including AJAX and forms.

The goal is that programmers do not need any knowledge about the HTML/CSS source code of web pages.
Instead, elements can be found by querying for visual attributes and visible text.

Here is an introductory screencast: [Abmash: Browser automation in Java](http://www.youtube.com/watch?v=Il0191C8fg8)

*Example (Java):*

	// open new browser window
	Browser browser = new Browser("http://google.com");
  
	// type text in search query input field and submit the search
	HtmlElement searchBox = browser.type("search", "Browser Automation").submit();
  
	// find the first result (predicates can be arbitrarily chained and nested)
	HtmlElement firstResult = browser.query(
		headline(),
		link(),
		below(searchBox)
	).findFirst();
  
	// click it
	firstResult.click();

Query predicates like `below()` are based on the visual representation of the web
page, independently from the page source and DOM structure. Correspondingly,
`isTitle()` not only selects `<h1>`, `<h2>`, ... elements, but also elements with a
bigger font-size than the average on the current page.

Color Queries
-------------
Colors are an important part of the visual representation of a web page. Therefore, Abmash allows you to
find elements by their color. Example:

	HtmlElements blueImages = browser.query(
		image(),
		color("blue")
	).find();

A list of all named colors can be found at [Recognized color keyword names](http://www.w3.org/TR/SVG/types.html#ColorKeywords).
Custom RGB values are possible too.

Optionally, the color predicate takes the two additional parameters `tolerance` and `dominance`.
The *Tolerance* defines how close the specified color has to match.
The *Dominance* controls how many percent of the element's pixels need to match these constraints.
By the way, the `image()` predicate also retrieves elements with a background image.

Documentation
-------------
Please refer to the [Abmash Javadoc](http://alp82.github.com/abmash/doc/).

License
-------
You may use any Abmash project under the terms of either the MIT License or the GNU General Public License (GPL) Version 2.

The MIT License is recommended for most projects. It is simple and easy to understand and it places almost no restrictions
on what you can do with a Abmash project.

If the GPL suits your project better you are also free to use an Abmash project under that license.

You can use Abmash in commercial projects as long as the copyright is left intact.

Implementation Details
----------------------
Abmash is a Java framework to automate web applications, based on **[Selenium](http://seleniumhq.org/)**,
**[Firefox](http://www.mozilla.org/firefox/)**/**[Chrome](http://www.google.com/chrome/)** and **[jQuery](http://jquery.com/)**.

**Abmash is still under heavy development. Please let me know if you are interested in this project.**

Maven
-----
Abmash snapshots are available in a Maven repository.

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
			<version>0.2.1-SNAPSHOT</version>
			<type>jar</type>
			<scope>compile</scope>
		</dependency>
	</dependencies>
