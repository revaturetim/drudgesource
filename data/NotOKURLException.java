package drudge.data;

import java.net.*;
import drudge.page.Page;
import drudge.Print;

public class NotOKURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NotOKURLException(Object p) {
	super(p);
	message = UselessMessages.NOTOK.mes;
	}

	public NotOKURLException(Object p, Object r) {
	super(p, r);
	message = UselessMessages.NOTOK.mes;
	}
	
	public void printRow() {
		
		if (Print.UselessClass == UselessURLException.class || Print.UselessClass == this.getClass()) {
		String[] n = {"", "", "", this.getSecondObject().toString(), this.message, this.getFirstObject().toString()};
		Print.row(n);
		}
	}
}
