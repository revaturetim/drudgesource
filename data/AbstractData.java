package drudge.data;

import java.io.*;
import java.net.*;

abstract class AbstractData<T> implements Data<T> {
protected String source;
protected int level = 1;
protected boolean exclude = false;
protected boolean include = false;
protected boolean robotallowed = true;

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
	
	public boolean excluded() {
	return exclude;
	}
	
	public void setExcluded(boolean b) {
	exclude = b;
	}
	
	public boolean included() {
	return include;
	}
	
	public void setIncluded(boolean b) {
	include = b;
	}
	
	public boolean robotAllowed() {
	return robotallowed;
	}
	
	public void setRobotAllowed(boolean b) {
	robotallowed = b;
	}

	
	

}
