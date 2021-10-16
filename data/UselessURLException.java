package drudge.data;

import java.io.Serializable;
import drudge.Print;
import drudge.page.Page;

public class UselessURLException extends Exception {
protected Object page;//this way it will cover different types of page units like page, string, or url
protected String message = UselessMessages.USELESS.mes;

//this has to be here in order to prevent dumb serialversionid warnings from showing up 
private final static long serialVersionUID = 0;

	//these here exist for the exclusive purpose of subclasses of this class
	public UselessURLException(Object p) {
	page = p;
	}
	
	public UselessURLException(Object p, String m) {
	this(p);
	message = m;
	}

	public UselessURLException(Object p, Object o) {
	this(p, o.toString());
	}

	//this is to clean up screan output when it throws and exception
	public String toString() {
	return message;
	}

	public String getUrl() {
	return page.toString();
	}

	public void printRow(String...m) {
	String[] n = new String[m.length + 1];
		for (int i = 0; i < m.length; i++) {
		n[i] = m[i];
		}
	n[n.length - 1] = getUrl();
		if (Print.UselessClass == UselessURLException.class || Print.UselessClass == this.getClass()) {
		Print.printRow(n);
		}
	}
	
	public void printRow() {
	printRow(this.message);
	}

}










