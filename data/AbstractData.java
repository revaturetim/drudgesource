package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;

abstract class AbstractData<T> extends AbstractList<T> implements Data<T>, Serializable {
protected String source;
protected int level = 1;

	abstract public T remove(int cycle);
	abstract public T get(int cycle);
	abstract public boolean add(T obj);
	abstract public void begin() throws Exception;
	abstract public void end() throws Exception;
	abstract public void truncate();
	abstract public boolean checkError();
	
	public void setSource(String s) {
	source = s;
	}
	
	public String source() {
	return source;
	}
	
	public void setLevel(int l) {
	level = l;
	}
	
	public int level() {
	return level;
	}
}
