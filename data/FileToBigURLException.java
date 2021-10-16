package drudge.data;

import java.net.*;
import drudge.page.Page;


public class FileToBigURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	FileToBigURLException(Object p) {
	super(p, UselessMessages.FILETOBIG.mes);
	}
	
	FileToBigURLException(Object p, String m) {
	super(p, m);
	}

	FileToBigURLException(Object p, Object o) {
	super(p, o);
	}



}
