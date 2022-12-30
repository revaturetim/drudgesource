package drudge.data;

import java.net.*;
import drudge.page.Page;


public class InvalidURLException extends UselessURLException {
private final static long serialVersionUID = 0;
private String reason;

	public InvalidURLException(Object p) {
	super(p);
	message = UselessMessages.NOTHTML.mes;
	}
	
	public InvalidURLException(Object p, String r) {
	this(p);
	reason = r;
	}
	
	public String getReason() {
	return reason;
	}

}
