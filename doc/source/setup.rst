Installation
============

There are four ways to setup Abmash:

#. Use Maven to create a reference to a Snapshot
#. Checkout a copy of the `Github repository <http://github.com/alp82/abmash>`_
#. Download the jar file including the dependencies
#. Download the jar file without the dependencies and download all needed dependencies seperately

**The recommended way of installing Abmash is using Maven.**

.. _section_installation_maven:

Maven
-----

Abmash snapshots are available in a Maven repository. See the section :ref:`section_eclipse` in order to learn how to integrate Maven into Eclipse.

First put the following repository in your `pom.xml`::

  <repositories>
    <repository>
      <id>alp-snapshots</id>
      <url>https://github.com/alp82/alp-mvn-repo/raw/master/snapshots</url>
    </repository>
  </repositories>


Then add this dependency::

  <dependencies>
    <dependency>
      <groupId>com.abmash</groupId>
      <artifactId>abmash</artifactId>
      <version>0.2.6-SNAPSHOT</version>
      <type>jar</type>
      <scope>compile</scope>
    </dependency>
  </dependencies>

Github
------

Cloning the repository is as easy as::

  $ git clone git://github.com/alp82/abmash

Then put the new folder in your project path. Be sure to have all dependencies in your path, too.


JAR
---

You can also download the jar file directly. The current version is 0.2.6-SNAPSHOT:

* `Abmash 0.2.6-SNAPSHOT jar <https://github.com/alp82/alp-mvn-repo/blob/master/snapshots/com/abmash/abmash/0.2.6-SNAPSHOT/abmash-0.2.6-20120713.170610-1.jar>`_
* `Abmash 0.2.6-SNAPSHOT jar with dependencies <https://github.com/alp82/alp-mvn-repo/blob/master/snapshots/com/abmash/abmash/0.2.6-SNAPSHOT/abmash-0.2.6-20120713.170610-1-jar-with-dependencies.jar>`_
* `Abmash 0.2.6-SNAPSHOT sources jar <https://github.com/alp82/alp-mvn-repo/blob/master/snapshots/com/abmash/abmash/0.2.6-SNAPSHOT/abmash-0.2.6-20120713.170610-1-sources.jar>`_
* `Abmash 0.2.6-SNAPSHOT javadoc jar <https://github.com/alp82/alp-mvn-repo/blob/master/snapshots/com/abmash/abmash/0.2.6-SNAPSHOT/abmash-0.2.6-20120713.170610-1-javadoc.jar>`_

Dependencies
------------

If you chose to install Abmash via Maven, you can ignore this section because Maven automatically downloads all needed dependencies.

Here is a list of all referenced libraries:

* joda-time
* slf4j
* slf4j-simple
* junit
* hamcrest-library

.. _section_eclipse:

Howto use Maven with Eclipse
----------------------------

#. Install the Maven plugin for Eclipse: `Installing m2eclipse <http://www.eclipse.org/m2e/download/>`_
#. Create a new project and choose `Maven Project`
#. Select the option `Create a simple project (skip archetype selection)`
#. Choose any group id, for example `com.myproject`
#. Choose any artifact id, for example `myproject`
#. The other fields are not mandatory, just click on `Finish`
#. Open the newly created `pom.xml` and edit the XML source by adding the code as described in the section :ref:`section_installation_maven`
#. Save the file and you are done! Eclipse will now automatically download Abmash and all its dependencies 