package drudge.data;

import java.io.Serializable;
import drudge.Print;
import drudge.page.Page;

public class UselessURLException extends Exception {
protected Object object1;//this way it will cover different types of page units like page, string, or url
protected Object object2;
protected String message = UselessMessages.USELESS.name();

//this has to be here in order to prevent dumb serialversionid warnings from showing up 
private final static long serialVersionUID = 0;

	public UselessURLException(Object o) {
	object1 = o;
	}

	public UselessURLException(Object o, Object p) {
	this(o);
	object2 = p;
	}
	//this is to clean up screan output when it throws and exception
	public String toString() {
	return message;
	}

	public Object getFirstObject() {
	return object1;
	}

	public Object getSecondObject() {
	return object2;
	}

	public void printRow() {
	Print.row(this, this.getFirstObject());	
	}

}










