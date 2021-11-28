package drudge.data;

import java.net.*;
import drudge.Debug;
import drudge.page.Page;

public class DuplicateURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public DuplicateURLException(Object p) {
	super(p);
	message = UselessMessages.DUPLICATE.mes;//this has to be assigned
	}

}
