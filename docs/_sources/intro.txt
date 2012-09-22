Introduction
============

The goal of Abmash is that programmers do not need any knowledge about the HTML/CSS source code of web pages.
Instead, elements can be found by querying visual attributes and visible text.

Example
-------

This is a short example of a minimal Abmash application::

  // open new browser window and visit Google
  Browser browser = new Browser("http://google.com");

  // type "Abmash" in search field and submit it
  // "google" is the visible label of the input field (you could use "search" or "lucky" too, if you use Google in English)
  HtmlElement searchField = browser.type("google", "Abmash").submit();

  // find the first result containing "github"
  HtmlElement firstResult = browser.query(
    headline(),
    link("github"),
    below(searchField) // alternative: below(typable("google"))
  ).findFirstWithWait();
  
  // finally click it and close the browser
  firstResult.click();
  browser.close();

Using the query methods requires a static import in your Java file::

  // static import for abmash query methods (e.g. headline() or link())
  import static com.abmash.api.query.QueryFactory.*;

| Query predicates like ``below()`` are based on the visual representation of the web page, independently from the page source and DOM structure.
| Correspondingly, ``headline()`` not only selects ``<h1>``, ``<h2>``, ... elements, but also elements with a bigger font size than the average on the current page.

Features
--------

Abmash comes with handy tools to interact with web pages.

Mouse & Keyboard
^^^^^^^^^^^^^^^^
* Clicks, double-clicks, right-clicks and drag'n'drop
* Typing text, pressing and holding keys
* Combination of mouse and keyboard interaction (e.g. "CTRL + Click")

Visual Queries
^^^^^^^^^^^^^^^
* Finding elements to interact with by visual cues
* Type: text, link, form, image, headline, ...
* Color: "blue button"
* Location: "all links below an image"
* Closeness: "closest text to a specific headline" 
* Size: "biggest image"

Complex Queries
^^^^^^^^^^^^^^^
* Building queries by the combination of arbitrary predicates
* Boolean queries: AND / OR / NOT
* Example: "(red images below a form) or (blue links which do not contain 'external')" 

Interaction with forms
^^^^^^^^^^^^^^^^^^^^^^
* Read the values of form elements
* Enter text, choose options and submit the form
* Datepicker support (experimental)

AJAX support
^^^^^^^^^^^^
* AJAX requests can be triggered by interacting with the page as a human
* Waits are handled automatically or can implicitly executed 
