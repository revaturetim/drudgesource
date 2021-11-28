package drudge.data;

import java.net.*;
import drudge.page.Page;


public class InvalidURLException extends UselessURLException {
private final static long serialVersionUID = 0;
private String type;

	public InvalidURLException(Object p) {
	super(p);
	message = UselessMessages.NOTHTML.mes;
	}
	
	public InvalidURLException(Object p, String t) {
	this(p);
	type = t;
	}
	
	public String getType() {
	return type;
	}

}
