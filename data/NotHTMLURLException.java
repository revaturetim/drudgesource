package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NotHTMLURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NotHTMLURLException(Object p) {
	super(p, UselessMessages.NOTHTML.mes);
	}
	
	public NotHTMLURLException(Object p, String m) {
	super(p, m);
	}

	public NotHTMLURLException(Object p, Object o) {
	super(p, o);
	}

}
