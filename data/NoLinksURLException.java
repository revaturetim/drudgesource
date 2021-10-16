package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NoLinksURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	NoLinksURLException(Object p) {
	super(p, UselessMessages.NOLINKS.mes);
	}
	
	NoLinksURLException(Object p, String m) {
	super(p, m);
	}

	NoLinksURLException(Object p, Object o) {
	super(p, o);
	}

}
