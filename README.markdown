Abmash
======
Abmash is a Java framework to automate web applications, based on Selenium/
Webdriver, Firefox and JQuery. The goal is that programmers do not need any
knowledge about the HTML/CSS source code of web pages. Instead, elements
can be found by querying for visual attributes and visible text.

**Abmash is human-oriented imitation of browser interactions.**

*Example:*

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

Abmash is still under heavy development. Please let me know if you are interested in this project.

Maven
-----
Abmash snapshots are available as Maven repositories.

First, add the following repository:

	<repositories>
		<repository>
			<id>alp-snapshots</id>
			<url>https://github.com/alp82/alp-mvn-repo/raw/master/snapshots</url>
		</repository>
	</repositories>

Then, put this dependency in your `pom.xml`:

	<dependencies>
		<dependency>
			<groupId>com.abmash</groupId>
			<artifactId>abmash</artifactId>
			<version>0.1.7-SNAPSHOT</version>
			<type>jar</type>
			<scope>compile</scope>
		</dependency>
	</dependencies>

*TODO more information*