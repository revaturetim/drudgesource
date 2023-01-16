package drudge.data;

import java.net.*;
import java.io.*;



public class DataListImages<T extends URL> extends DataListEmail<T> {



	public DataListImages() {
	super();
	}

	public DataListImages(String s) {
	super(s);
	}

	public boolean checkError() {
	System.out.println("Checking " + source() + " file for errors.");
	boolean duplicate = false;
	boolean linkerror = false;
	int linecount = 0;
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		int firstcolumnlength = 0;
			for (String image1 = reader.readLine(); image1 != null; image1 = reader.readLine()) {
			linecount = reader.getLineNumber();
			/*this will check for decoding errors*/
				if (image1.contains("%")) {
				System.out.println("...Possible encoding error at line " + String.valueOf(linecount));
				}
			/*This will check for duplicates in the data*/
				try {
				URL u = new URL(image1);
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					for (String image2 = reader2.readLine(); image2 != null; image2 = reader2.readLine()) {
					int linecount2 = reader2.getLineNumber();
						if (linecount2 < linecount) {
							if (image1.equals(image2)) {
							duplicate = true;
							System.out.print("..." + image1 + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) 
					       	+ " and " 
					       	+ String.valueOf(linecount2));
							}
						}
					}
				}
				catch (MalformedURLException M) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has an image link that isn't quite right: ");
				System.out.println(image1);
				}
			}
		System.out.println("...The number of lines in " + source() +  " is "  + linecount);
		}
		catch (IOException I) {
		System.out.println(I);
		}
	return (duplicate || linkerror);
	}

}
