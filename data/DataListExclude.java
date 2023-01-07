package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;
import drudge.Debug;
import drudge.page.*;
import drudge.global.*;

/*this class is a superclass of objects that use an array as the storage device*/
/*T has to be assumed to be an object so it retains is general behavior for all types*/
/*This is the default Data object for this program when not using database object*/
public class DataListExclude<T> extends DataList<T>{

	public DataListExclude() {

	}

	public DataListExclude(String s) {
	super(s);
	}
	
	final public T remove(final int i) {
	//empty since it is going to be a read only object
	return null;
	}
	
	@SuppressWarnings("unchecked")	
	/*The default assumes that it is working with a page object*/
	public void begin() throws Exception {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			try {
			T u = (T)new URL(line);
			put(u);
			}
			catch (MalformedURLException M) {
			D.error(M);
			}
		}
	}

	/*The default assumes you are working with a page object.  Subclasses should override*/
	final public void end() throws Exception {
	//empty since it is going to be a read only object
	}
	
	final public void truncate() {
	//empty since it is going to be a read only object
	}
	
	public boolean checkError() {
	System.out.println("Checking " + source() + " file for errors.");
	boolean duplicate = false;
	boolean linkerror = false;
	int linecount = 0;
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		int firstcolumnlength = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			linecount = reader.getLineNumber();
			String[] columns = line.split("\n");
			/*This will see if the total number of columns for each line is the same as the previous one*/
				if (linecount == 1) {
				firstcolumnlength = columns.length;
				}
				if (columns.length != firstcolumnlength) {
				System.out.println("...There is a column length error at line " + String.valueOf(linecount));
				}
			/*this will check for decoding errors*/
				for (String col : columns) {
					if (col.contains("%")) {
					System.out.println("...Possible encoding error at line " + String.valueOf(linecount));
					}
				}
			/*This will check for duplicates in the data*/
			String link = columns[0];
				try {
				Page p = new Page(link);//this throws malformedurlexception
				p.isValid();//this throws InvalidURLException, URISyntaxException
				URL u = p.getURL();
					if (u.getAuthority() == null) {
					throw new MalformedURLException("No Host");
					}
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine()) {
					int linecount2 = reader2.getLineNumber();
						if (linecount2 < linecount) {
						String[] columns2 = line2.split(CountFile.sep);
						String link2 = columns2[0];
							if (link.equals(link2)) {
							duplicate = true;
							System.out.print("..." + link + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) 
					       	+ " and " 
					       	+ String.valueOf(linecount2));
							}
						}
					}
				}
				catch (InvalidURLException N) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't an html file: ");
				System.out.println(link);
				}
				catch (URISyntaxException U) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
				catch (MalformedURLException M) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
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
