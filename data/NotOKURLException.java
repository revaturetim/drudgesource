package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NotOKURLException extends UselessURLException {
private final static long serialVersionUID = 0;
private Object responsemessage = "";

	public NotOKURLException(Object p) {
	super(p);
	message = UselessMessages.NOTOK.mes;
	}

	public NotOKURLException(Object p, Object r) {
	super(p, r);
	message = UselessMessages.NOTOK.mes;
	}
	
	public void printRow() {
	printRow("", "", "", getSecondObject().toString(), this.message);
	}

}
