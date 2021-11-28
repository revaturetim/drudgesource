package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NotOKURLException extends UselessURLException {
private final static long serialVersionUID = 0;
private String responsemessage = "";

	public NotOKURLException(Object p) {
	super(p, UselessMessages.NOTOK.mes);
	}
	
	public NotOKURLException(Object p, String m) {
	super(p, m);
	}

	public NotOKURLException(Object p, Object o) {
	super(p, o);
	}

	public NotOKURLException(Object p, Object o, String r) {
	this(p);
	responsemessage = r;
	}
	
	public void printRow() {
	printRow("", "", "", responsemessage, this.message);
	}

}
