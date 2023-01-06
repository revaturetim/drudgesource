package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;

abstract class AbstractData<T> extends AbstractList<T> implements Data<T>, Serializable {
protected String source;
protected int level = 1;

	
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
