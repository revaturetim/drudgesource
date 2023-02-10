package drudge.data;

import java.net.*;
import java.io.*;



public class DataListImage extends DataListEmail {



	public DataListImage() {
	super();
	}

	public DataListImage(String s) {
	super(s);
	}
	
	public void checkError() {
	D.writeBeginningResponse(source());
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
			for (String image1 = reader.readLine(); image1 != null; image1 = reader.readLine()) {
				try {
				URL u = new URL(image1);
				URI r = u.toURI();
				final LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					/*This will check for duplicates in the data*/
					for (String image2 = reader2.readLine(); reader2.getLineNumber() < reader.getLineNumber(); image2 = reader2.readLine()) {
						if (image1.equals(image2)) {
						D.writeDuplicateResponse(String.valueOf(reader.getLineNumber()), image1, image2);	
						}
					}
				}
				catch (MalformedURLException M) {
				D.writeResponse(String.valueOf(reader.getLineNumber()), image1, M.toString());
				}
				catch (URISyntaxException U) {
				D.writeResponse(String.valueOf(reader.getLineNumber()), image1, U.toString());
				}
			}
		D.writeFinalResponse(reader.getLineNumber());
		}
		catch (FileNotFoundException F) {
		
		}
		catch (IOException I) {
		System.out.println(I);
		}
	}

}
