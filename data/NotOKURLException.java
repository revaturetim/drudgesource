package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NotOKURLException extends UselessURLException {
private final static long serialVersionUID = 0;
private String responsemessage = "";

	public NotOKURLException(Object p) {
	super(p);
	message = UselessMessages.NOTOK.mes;
	}

	public NotOKURLException(Object p, String r) {
	this(p);
	responsemessage = r;
	}
	
	public void printRow() {
	printRow("", "", "", responsemessage, this.message);
	}

}
