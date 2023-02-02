package drudge.data;

import java.net.*;
import drudge.page.Page;


public class BadEncodingURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	BadEncodingURLException(Page p) {
	super(p);
	message = UselessMessages.BADENCODE.name();
	}

	BadEncodingURLException(Page p, Page q) {
	super(p, q);
	message = UselessMessages.BADENCODE.name();
	}
}
