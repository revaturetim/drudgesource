package drudge.data;

import java.net.*;
import drudge.page.Page;


public class BadEncodingURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	BadEncodingURLException(Page p) {
	super(p, UselessMessages.BADENCODE.mes);
	}
	
	BadEncodingURLException(Page p, String m) {
	super(p, m);
	}

	BadEncodingURLException(Page p, Object o) {
	super(p, o);
	}

}
