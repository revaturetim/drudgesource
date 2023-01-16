package drudge.data;

import java.net.*;
import drudge.page.Page;


public class InvalidURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public InvalidURLException(Object o) {
	super(o);
	message = UselessMessages.NOTHTML.mes;
	}
	
	public InvalidURLException(Object o, Object p) {
	super(o, p);
	message = UselessMessages.NOTHTML.mes;
	}
	
}
