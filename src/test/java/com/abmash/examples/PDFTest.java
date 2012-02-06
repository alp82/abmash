package com.abmash.examples;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.pdfbox.lucene.LucenePDFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//http://pdfbox.apache.org/userguide/text_extraction.html
		String path = "/home/alp/Desktop/dorf.pdf";
//		String pdfString = UrlTools.getContentsFrom("http://www.gesetze-im-internet.de/bundesrecht/bgb/gesamt.pdf");
//		PdfTextExtractor parser = new PdfTextExtractor(new PdfReader(new FileInputStream(new File(path))));
//		String text = parser.getTextFromPage(1);
//		System.out.println(text);
//		PdfContentReaderTool.listContentStream(new File("/home/alp/Desktop/gesamt.pdf"), new PrintWriter("/home/alp/Desktop/gesamt.txt"));
		
		Document luceneDocument = LucenePDFDocument.getDocument(new File(path));
		List<Field> fields = luceneDocument.getFields();
		for (Field field: fields) {
			System.out.println("Field: " + field.name());
			System.out.println(field.stringValue());
		}
		
//		String[] contents = luceneDocument.getValues("contents");
//		for (int i = 0; i < contents.length; i++) {
//			System.out.println(contents[i]);
//		}
		
// TODO siehe PrintImageLocations.class
		PDDocument doc = PDDocument.load(path);  
        PDFTextStripper stripper = new PDFTextStripper();  
        String text = stripper.getText(doc);
//        PDResources res = stripper.getResources();
//        Map<String, PDXObjectImage> images = res.getImages();
//        for (Iterator<String> iterator = images.keySet().iterator(); iterator.hasNext();) {
//        	PDXObjectImage image = images.get(iterator.next());
//			System.out.println(image);
//		}
        
        System.out.println(text);
        doc.close();
	}

}
