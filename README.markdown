Abmash
=
Abmash is a Java library that allows **interacting with a browser as humans would do**.
* *Based on the visible rendered output.*
* *Including AJAX and forms.*

Here is an introductory screencast: [**Abmash: Browser automation in Java**](http://www.youtube.com/watch?v=Il0191C8fg8)

Notice: Abmash is still experimental. Advanced query features like directions, distance and colors are likely buggy and quite slow compared to usual selectors.

Table of Contents
-
1. [Features](#features)
2. [Installation](#installation)
3. [Dependencies](#dependencies)
4. [Getting Started](#getting-started)
5. [Color Queries](#color-queries)
6. [Documentation](#documentation)
7. [License](#license)
8. [Implementation](#implementation)
9. [Issues](#issues)
10. [Maven](#maven)
11. [Eclipse Tutorial](#eclipse-tutorial)

Features
-
* **Clicking**, using the **keyboard** or a combination of both ("CTRL + Click")
* Interaction with **forms**
* **AJAX support without wait commands**, Abmash usually knows when it has to wait for an AJAX request to be completed
* **Mouseover** + **Drag'n'Drop**
* Searching for **visual closeness** in specific directions ("text below image")
* Searching for **specific colors** ("red button")
* Complex **boolean queries** ("blue image or headline which is not clickable")
* **Datepicker** support, just run `browser.chooseDate("arrival", new DateTime(2012, 10, 22, 14, 30));` (experimental) 

Installation
-
The recommended way of installing Abmash is using Maven. See below in section [Maven](#maven) and [Eclipse Tutorial](#eclipse-tutorial).

You can also download the jar file directly. [`The current version is 0.2.9-SNAPSHOT`](https://github.com/alp82/alp-mvn-repo/blob/master/snapshots/com/abmash/abmash/0.2.9-SNAPSHOT) and is available in four different packages:
* jar without dependencies
* jar with dependencies
* jar with sources only
* jar with javadoc only

Dependencies
-
If you chose to install Abmash via Maven, you can ignore this section because Maven automatically downloads all needed dependencies.

Here is a list of all referenced libraries:
* joda-time
* slf4j
* slf4j-simple
* junit
* hamcrest-library

Getting Started
-
The goal of Abmash is that programmers do not need any knowledge about the HTML/CSS source code of web pages.
Instead, elements can be found by querying visual attributes and visible text.

**Example:**

*Imports:*

	// you need a static import for the query methods (e.g. headline() or link())
	import static com.abmash.api.query.QueryFactory.*;
	
*Main application:*

	// open new browser window and visit Google
	Browser browser = new Browser("http://google.com");
  
	// type text in search query input field and submit the search
	// "google" is the visible label of the input field
	// you could use "search" or "lucky" too, if you use the English localized Google
	HtmlElement searchField = browser.type("google", "Abmash").submit();

	// find the first result containing "github"
	// (predicates can be arbitrarily chained and nested)
	HtmlElement firstResult = browser.query(
	    headline(),
	    link("github"),
	    below(searchField) // alternative: below(typable("google"))
	).findFirstWithWait();
	
	// finally click it
	firstResult.click();
	
	// close the browser
	browser.close();

*Explanation:*

Query predicates like `below()` are based on the visual representation of the web
page, independently from the page source and DOM structure.

Correspondingly, `headline()` not only selects `<h1>`, `<h2>`, ... elements, but also
elements with a bigger font size than the average on the current page.

Color Queries
-
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
-
Please refer to the [Abmash Javadoc](http://alp82.github.com/abmash/doc/).

License
-
You may use any Abmash project under the terms of either the MIT License or the GNU General Public License (GPL) Version 3.

The **MIT License** is recommended for most projects. It is simple and easy to understand and it places almost no restrictions
on what you can do with a Abmash project.

If the **GPL** suits your project better you are also free to use an Abmash project under that license.

You can use Abmash in commercial projects as long as the copyright is left intact.

Implementation
-
Abmash is a Java framework to automate web applications, based on **[Selenium](http://seleniumhq.org/)**,
**[Firefox](http://www.mozilla.org/firefox/)**/**[Chrome](http://www.google.com/chrome/)** and **[jQuery](http://jquery.com/)**.

Issues
-
Abmash is still in development. Therefore, there are still some problems:

* Tutorial is missing, we are working on this currently
* Documentation needs improvement
* Windows should be supported, but is not tested yet
* Configuration of the Browser is possible but not well integrated 

Maven
-
Abmash snapshots are available in a Maven repository. See section [Eclipse Tutorial](#eclipse-tutorial) in order to learn how to integrate Maven into Eclipse.

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
			<version>0.2.9-SNAPSHOT</version>
			<type>jar</type>
			<scope>compile</scope>
		</dependency>
	</dependencies>

Eclipse Tutorial
-
1. Install the Maven plugin for Eclipse: [Installing m2eclipse](http://www.eclipse.org/m2e/download/)
2. Create a new project and choose `Maven Project`
3. Select the option `Create a simple project (skip archetype selection)`
4. Choose any group id, for example `com.myproject`
5. Choose any artifact id, for example `myproject`
6. The other fields are not mandatory, just click on `Finish`
7. Open the newly created `pom.xml` and edit the XML source by adding the code as described in the last section [Maven](#maven)
8. Save the file and you are done! Eclipse will now automatically download Abmash and all its dependencies 