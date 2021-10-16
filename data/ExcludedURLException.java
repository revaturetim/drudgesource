package drudge.data;

import java.net.*;
import drudge.page.Page;




public class ExcludedURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public ExcludedURLException(Object p) {
	super(p, UselessMessages.EXCLUDED.mes);
	}
	
	ExcludedURLException(Object p, String m) {
	super(p, m);
	}

	ExcludedURLException(Object p, Object o) {
	super(p, o);
	}



}
