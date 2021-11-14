package drudge.data;

import java.net.*;
import drudge.page.Page;


public class InvalidURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public InvalidURLException(Object p) {
	super(p, UselessMessages.NOTHTML.mes);
	}
	
	public InvalidURLException(Object p, String m) {
	super(p, m);
	}

	public InvalidURLException(Object p, Object o) {
	super(p, o);
	}

}
