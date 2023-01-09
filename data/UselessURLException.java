package drudge.data;

import java.io.Serializable;
import drudge.Print;
import drudge.page.Page;

public class UselessURLException extends Exception {
protected Object page;//this way it will cover different types of page units like page, string, or url
protected String message = UselessMessages.USELESS.mes;

//this has to be here in order to prevent dumb serialversionid warnings from showing up 
private final static long serialVersionUID = 0;

	public UselessURLException(Object p) {
	page = p;
	}

	//this is to clean up screan output when it throws and exception
	public String toString() {
	return message;
	}

	public String getUrl() {
	return page.toString();
	}

	public void printRow(String...m) {
	String[] n = {m[0], m[1], m[2], m[3], m[4], getUrl()};
		
		if (Print.UselessClass == UselessURLException.class || Print.UselessClass == this.getClass()) {
		Print.row(n);
		}
	}
	
	public void printRow() {
	printRow("", "", "", "", this.message);
	}

}










