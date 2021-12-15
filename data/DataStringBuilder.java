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

	public boolean checkError() {
	return true;
	}
	
	public boolean add(T obj) {
	return false;
	}


}
