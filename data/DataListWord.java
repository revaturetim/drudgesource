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
public class DataListWord extends DataListEmail {

	public DataListWord() {

	}

	public DataListWord(String s) {
	super(s);
	}
	
	public void checkError() {
	D.writeBeginningResponse(source());
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			/*This will check for duplicates in the data*/
			final LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
				for (String line2 = reader2.readLine(); reader2.getLineNumber() < reader.getLineNumber(); line2 = reader2.readLine()) {
					if (line.equals(line2)) {
					D.writeDuplicateResponse(line, String.valueOf(reader.getLineNumber()), String.valueOf(reader2.getLineNumber()));
					}
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
