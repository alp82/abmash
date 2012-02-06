package com.abmash.core.tools;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.xerces.impl.dv.util.Base64;

public class IOTools {

	public static String getContentsFrom(String urlString) throws IOException {
		URL url = new URL(urlString);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String inputLine;
		String content = "";
		while ((inputLine = in.readLine()) != null) {
			content += inputLine;
		}
		in.close();
		return content;
	}

	public static String convertStreamToString(InputStream inputStream) {
		return (new Scanner(inputStream)).useDelimiter("\\A").next();
	}
	
	public static File convertStreamToFile(InputStream inputStream, String extension) {
		try {
			File tmpFile = File.createTempFile("file", "." + extension);
			tmpFile.deleteOnExit();

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(tmpFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			inputStream.close();
			out.flush();
			out.close();

			return tmpFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertImageToBinaryData(InputStream imageInputStream, String fileExtension) {
		BufferedImage image;
		try {
			image = ImageIO.read(imageInputStream);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, fileExtension, baos);
			return "data:image/" + fileExtension + ";base64," + Base64.encode(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
