package drudge.data;

import java.net.*;
import drudge.page.Page;
import drudge.Print;

public class NotOKURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NotOKURLException(Object p) {
	super(p);
	message = UselessMessages.NOTOK.name();
	}

	public NotOKURLException(Object p, Object r) {
	super(p, r);
	message = UselessMessages.NOTOK.name();
	}
	
	public void printRow() {
		
		if (Print.uselessmessage == UselessMessages.ALL || Print.uselessmessage.cls == this.getClass()) {
		String[] n = {"", "", "", this.getSecondObject().toString(), this.message, this.getFirstObject().toString()};
		Print.row(n);
		}
	}
}
