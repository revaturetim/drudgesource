package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NoContentURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NoContentURLException(Object p) {
	super(p);
	message = UselessMessages.NOCONTENT.name();
	}

	public NoContentURLException(Object p, Object q) {
	super(p, q);
	message = UselessMessages.NOCONTENT.name();
	}

}
