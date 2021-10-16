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
	//Page p = null;
		if (page instanceof Page) {
		Page.Header h = ((Page)page).header();
		printRow("hello", UselessMessages.NOTOK.mes);
		}
		else {
		printRow(message);
		}

	}

}
