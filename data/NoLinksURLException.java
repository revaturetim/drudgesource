package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NoLinksURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	NoLinksURLException(Object p) {
	super(p);
	message = UselessMessages.NOLINKS.name();
	}

	NoLinksURLException(Object p, Object q) {
	super(p, q);
	message = UselessMessages.NOLINKS.name();
	}

}
