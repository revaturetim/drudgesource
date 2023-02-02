package drudge.data;

import java.net.*;
import drudge.page.Page;



public class RedirectedURLException extends NotOKURLException {
private final static long serialVersionUID = 0;

	public RedirectedURLException(Object p) {
	super(p);
	message = UselessMessages.REDIRECTEDURL.name();
	}

	public RedirectedURLException(Object p, Object r) {
	super(p, r);
	message = UselessMessages.REDIRECTEDURL.name();
	}

}
