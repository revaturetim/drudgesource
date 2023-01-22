package drudge.data;

import java.io.*;
import java.util.*;
import java.net.*;
import drudge.page.*;
import drudge.*;
import drudge.global.*;

/*This class is for the data storage of the links it finds using streams
 *I thought it was clever to write up
 */
abstract class DataWriter<T> extends AbstractData<T> {

	//this has to be filled with something in order for subclass works.
	abstract <R extends Reader> R createReader();
	abstract <W extends Writer> W createWriter();
	
	
	public void put(T page) throws DuplicateURLException, IllegalArgumentException {
		if (page == null) {
		throw new IllegalArgumentException("Attempting to add a null object into the database");
		}
		if (contains(page) == false) {
		Page tp = (Page)page;
		PrintWriter PRINTER = createWriter();
			try {
			D.writeEntry(tp, PRINTER, level());
			PRINTER.close();
			}
			catch (IOException I) {
			D.error(I);
			}
		}
		else {
		throw new DuplicateURLException(page);
		}
	}

	public T get(final int n) {
	T entry = null;
		try {
		LineNumberReader READER = createReader();
		READER.setLineNumber(-1);
			for (String line = READER.readLine(); line != null; line = READER.readLine()) {
			final int c = READER.getLineNumber();
				if (c == n) {
				entry = (T)PageFactory.createFromString(line);
				break;
				}
			}
		READER.close();
		}
		catch (IOException I) {
		D.error(I);
		}
	return entry;
	}


	public T remove(final int l) {
	T p = null;
	StringBuffer buff = new StringBuffer();
		try {
		LineNumberReader reader = createReader();
		reader.setLineNumber(-1);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (reader.getLineNumber() != l) {
				buff.append(line);
				buff.append("\n");
				}
				else {
				p = (T)PageFactory.createFromString(line);			
				}
			} 
		reader.close();
		}
		catch (IOException I) {
		D.error(I);
		}
		try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(source()));
		writer.append(buff);
		writer.close();
		}
		catch (IOException I) {
		D.error(I);
		}
	return p;
	}
	
	public int size() {
	int size = 0;
		try {
		LineNumberReader READER = createReader();
		READER.setLineNumber(0);
			for (String line = READER.readLine(); line != null; line = READER.readLine()) {
			size = READER.getLineNumber();
			}
		READER.close();
		}
		catch (IOException I) {
		D.error(I);
		}
	return size;
	}
	
	public void truncate() {
		try {
		FileWriter writer = new FileWriter(source());
		writer.flush();//this should erase everything in the file
		}
		catch (IOException I) {
		D.error(I);
		}
	}
	
	public boolean checkError() {
	return false;
	}
	
	public void begin() {

	}

	public void end() {

	}

	
}

