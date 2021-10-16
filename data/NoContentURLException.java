package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NoContentURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NoContentURLException(Object p) {
	super(p, UselessMessages.NOCONTENT.mes);
	}
	
	public NoContentURLException(Object p, String m) {
	super(p, m);
	}

	public NoContentURLException(Object p, Object o) {
	super(p, o);
	}



}
