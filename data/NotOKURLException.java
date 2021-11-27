package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NotOKURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NotOKURLException(Object p) {
	super(p, UselessMessages.NOTOK.mes);
	}
	
	public NotOKURLException(Object p, String m) {
	super(p, m);
	}

	public NotOKURLException(Object p, Object o) {
	super(p, o);
	}

	public void printRow() {
	//Page.Header head = ((Page)page).header();
	printRow("", "", "", "NOT 2XX", this.message);

	}

}
