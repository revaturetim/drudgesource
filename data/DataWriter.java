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
abstract class DataWriter<T> implements Data<T> {
protected String source;
private int datalevel = 1;
private boolean include = false;
private boolean exclude = false;
private boolean robotallowed = true;

	//this has to be filled with something in order for subclass works.
	abstract <R extends Reader> R createReader();
	abstract <W extends Writer> W createWriter();
	abstract public boolean add(T obj);
	
	

	public T get(final int n) {
	T entry = null;
		try {
		LineNumberReader READER = createReader();
		READER.setLineNumber(-1);
			for (String line = READER.readLine(); line != null; line = READER.readLine()) {
			final int c = READER.getLineNumber();
				if (c == n) {
				entry = (T)D.getPageFromEntry(line);
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


	public T delete(final int l) {
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
				p = (T)D.getPageFromEntry(line);			
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
	
	public String source() {
	return source;
	}

	public void setLevel(int l) {
	datalevel = l;
	}

	public int level() {
	return datalevel;
	}

	public void begin() {

	}

	public void finish() {

	}
	
	public void setExcluded(boolean b) {
	
	}
	
	public boolean excluded() {
	return exclude;
	}
	
	public void setIncluded(boolean b) {
	include = b;
	}
	
	public boolean included() {
	return include;
	}
	
	public boolean robotAllowed() {
	return robotallowed;
	}
	
	public void setRobotAllowed(boolean b) {
	robotallowed = b;
	}

	
}

