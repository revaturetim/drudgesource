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
public class DataListExcludeWords<T extends String> extends DataListExclude<T>{

	public DataListExcludeWords() {

	}

	public DataListExcludeWords(String s) {
	super(s);
	}
	
	@SuppressWarnings("unchecked")	
	/*The default assumes that it is working with a page object*/
	public void begin() throws Exception {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		add((T)line);
		}
	}
	
	public boolean checkError() {
	System.out.println("Checking " + source() + " file for errors.");
	boolean duplicate = false;
	boolean linkerror = false;
	int linecount1 = 0;
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			linecount1 = reader.getLineNumber();
			
			/*This will check for duplicates in the data*/
			LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
				for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine()) {
				final int linecount2 = reader2.getLineNumber();
					if (linecount2 < linecount1) {
						
						if (line.equals(line2)) {
						duplicate = true;
						System.out.print("..." + line + " is a duplicate entry found at ");
					      	System.out.println(String.valueOf(linecount1) 
					      	+ " and " 
					      	+ String.valueOf(linecount2));
						}
					}
				}
				
			}
		System.out.println("...The number of lines in " + source() +  " is "  + linecount1);
		}
		catch (IOException I) {
		System.out.println(I);
		}
	return (duplicate || linkerror);
	}
}
