package drudge.data;

import drudge.Debug;
import drudge.page.*;
import drudge.data.*;
import drudge.global.FileNames;
import java.io.*;
import java.net.*;
import java.util.*;

public class DataStringBuilder<T> implements Data<T> {
final private StringBuilder builder = new StringBuilder();
String file = FileNames.links;

	public T get(final int L) {
	T p = null;
		try {
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new StringReader(builder.toString())));
		reader.setLineNumber(-1);//this is so index and line number will be the same
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (reader.getLineNumber() == L) {
				p = D.getPageFromEntry(line);
				break;
				}	
			}
		reader.close();
		}
		catch (IOException I) {
		D.error(I);
		}
	return p;
	}

	public void put(T page) throws DuplicateURLException {
		if (contains(page)) {
		throw new DuplicateURLException(page);
		}
		else {
		builder.append(page.toString());
		builder.append("\n");
		}
	}
	
	public T remove(int l) {
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

	public String source() {
	return file;
	}

}
