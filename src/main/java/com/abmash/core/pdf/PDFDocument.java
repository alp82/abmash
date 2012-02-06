package com.abmash.core.pdf;


import com.abmash.core.Document;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Field;
import org.apache.pdfbox.examples.util.PrintImageLocations;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.lucene.LucenePDFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PDFDocument implements Document {

	public static final Logger LOG = LoggerFactory.getLogger(PDFDocument.class);

	private URL url;
	private PDDocument document = null;
	private HashMap<String, String> headers = new HashMap<String, String>();

	public PDFDocument(URL url) throws IOException {
		this.url = url;

		// load document
		try {
			document = PDDocument.load(url);
			if (document.isEncrypted()) {
				try {
					document.decrypt("");
				} catch (InvalidPasswordException e) {
					LOG.warn("Warning: PDF Document is encrypted with a password and cannot be opened.");
				} catch (CryptographyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} finally {
			if (document != null) {
				document.close();
			}
		}

		// get headers
		@SuppressWarnings("unchecked")
		List<Field> fields = LucenePDFDocument.getDocument(url).getFields();
		for (Field field: fields) {
			headers.put(field.name(), field.stringValue());
		}
	}

	public Set<String> getHeaderNames() {
		return headers.keySet();
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public void getText() {
		String text = null;
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(document);
			document.close();
			PrintImageLocations test;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEXT:");
		System.out.println(text);
	}
}
