package com.abmash.core.pdf;

import org.apache.pdfbox.exceptions.InvalidPasswordException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import java.io.IOException;

import java.util.List;

/**
 * This is an example on how to get some x/y coordinates of text.
 * 
 * Usage: java org.apache.pdfbox.examples.util.PrintTextLocations
 * &lt;input-pdf&gt;
 * 
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.7 $
 */
public class PrintTextLocations extends PDFTextStripper {
	/**
	 * Default constructor.
	 * 
	 * @throws IOException
	 *             If there is an error loading text stripper properties.
	 */
	public PrintTextLocations() throws IOException {
		super.setSortByPosition(true);
	}

	/**
	 * This will print the documents data.
	 * 
	 * @param args
	 *            The command line arguments.
	 * 
	 * @throws Exception
	 *             If there is an error parsing the document.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			usage();
		} else {
			PDDocument document = null;
			try {
				document = PDDocument.load(args[0]);
				if (document.isEncrypted()) {
					try {
						document.decrypt("");
					} catch (InvalidPasswordException e) {
						System.err.println("Error: Document is encrypted with a password.");
						System.exit(1);
					}
				}
				PrintTextLocations printer = new PrintTextLocations();
				List allPages = document.getDocumentCatalog().getAllPages();
				for (int i = 0; i < allPages.size(); i++) {
					PDPage page = (PDPage) allPages.get(i);
					System.out.println("Processing page: " + i);
					PDStream contents = page.getContents();
					if (contents != null) {
						printer.processStream(page, page.findResources(), page.getContents().getStream());
					}
				}
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}
	}

	/**
	 * A method provided as an event interface to allow a subclass to perform
	 * some specific functionality when text needs to be processed.
	 * 
	 * @param text
	 *            The text to be processed
	 */
	protected void processTextPosition(TextPosition text) {
		System.out.println("String[" + text.getXDirAdj() + "," + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale=" + text.getXScale() + " height=" + text.getHeightDir() + " space="
				+ text.getWidthOfSpace() + " width=" + text.getWidthDirAdj() + "]" + text.getCharacter());
	}

	/**
	 * This will print the usage for this document.
	 */
	private static void usage() {
		System.err.println("Usage: java org.apache.pdfbox.examples.pdmodel.PrintTextLocations <input-pdf>");
	}

}
