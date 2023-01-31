package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;

abstract class AbstractData extends AbstractList implements Data, Serializable {
protected String source;
protected int level = 1;
protected int firstindex = 0;

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
	
	public int firstIndexNumber() {
	return firstindex;
	}

}
