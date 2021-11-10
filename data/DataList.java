/*Do not erase this file.  It is important because I sometimes need a cheap Data object for this program*/
package drudge.data;

import java.net.*;
import java.io.*;
import java.util.*;
import drudge.page.*;
import drudge.Debug;
import drudge.data.*;

public class DataList<T> extends ArrayList<T> implements Data<T> {
private String source = null;
private int datalevel = 0;
private final static long serialVersionUID = 0;

	public DataList() {
	super();
	}
	
	public DataList(String s) {
	source = s;
	}

	public DataList(String s, int cap) {
	super(cap);
	}
	
	public DataList(String s, Collection<T> col) {
	super(col);
	}
	
	public DataList(String s, T[] words) {
	this(s);
		for (T w : words) {
		add(w);
		}
	}
	
	public void put(T link) throws DuplicateURLException {
		if (contains(link)) {
		throw new DuplicateURLException(link);
		}
		else {
		add(link);
		}
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


}
