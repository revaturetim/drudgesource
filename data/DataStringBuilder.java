package drudge.data;

import drudge.Debug;
import drudge.page.*;
import drudge.data.*;
import drudge.global.FileNames;
import java.io.*;
import java.net.*;
import java.util.*;

public class DataStringBuilder<T> extends DataWriter<T> {
final private StringBuilder builder = new StringBuilder();
private int datalevel = 0;
private boolean exclude = false;
private boolean include = false;

	public PrintWriter createWriter() {
	return new PrintWriter(new BufferedWriter(new StringWriter()));
	}
	
	protected LineNumberReader createReader() {
	return new LineNumberReader(new BufferedReader(new StringReader(builder.toString())));	
	}
	
	public T delete(int l) {
	int L = 1;
		for (int i = 0; i != -1; i = builder.indexOf("\n", i + 1)) {
			if (L == l) {
			builder.delete(i, builder.indexOf("\n", i + 1));
			break;
			}		
		L++;
		}
	return null;
	}

	public int size() {
	String bstring = builder.toString();
	String[] barray = bstring.split("\n");
	return barray.length;
	}
	
	public boolean check() {
	return true;
	}
	
	public boolean checkError() {
	return true;
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
	exclude = b;
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
	public boolean add(T obj) {
	return false;
	}


}
