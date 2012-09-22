API
===

:java:class:`Query <com.abmash.api.query.Query>`

df

.. java:class:: HtmlElement

  HtmlElement represents an element in the current web page

  .. java:method:: test(String foo)

    test method, yep

dfs

Color Queries
-------------

Colors are an important part of the visual representation of a web page. Therefore, Abmash allows you to
find elements by their color. Example::

  HtmlElements blueImages = browser.query(
    image(),
    color("blue")
  ).find();

A list of all named colors can be found at `Recognized color keyword names <http://www.w3.org/TR/SVG/types.html#ColorKeywords>`_.
Custom RGB values are possible too.

Optionally, the color predicate takes the two additional parameters ``tolerance`` and ``dominance``.

The *Tolerance* defines how close the specified color has to match.

The *Dominance* controls how many percent of the element's pixels need to match these constraints.
By the way, the ``image()`` predicate also retrieves elements with a background image.
