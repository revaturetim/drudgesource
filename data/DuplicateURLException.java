package drudge.data;

import java.net.*;
import drudge.Debug;
import drudge.page.Page;

public class DuplicateURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public DuplicateURLException(Object p) {
	super(p, UselessMessages.DUPLICATE.mes);
	}
	
	DuplicateURLException(Object p, String m) {
	super(p, m);
	}

	DuplicateURLException(Object p, Object o) {
	super(p, o);
	}
}
