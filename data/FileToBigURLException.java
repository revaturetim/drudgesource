package drudge.data;

import java.net.*;
import drudge.page.Page;


public class FileToBigURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	FileToBigURLException(Object p) {
	super(p);
	message = UselessMessages.FILETOBIG.name();
	}

	FileToBigURLException(Object p, Object q) {
	super(p, q);
	message = UselessMessages.FILETOBIG.name();
	}
}
