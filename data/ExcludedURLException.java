package drudge.data;

import java.net.*;
import drudge.page.Page;




public class ExcludedURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public ExcludedURLException(Object p) {
	super(p);
	message = UselessMessages.EXCLUDED.mes;
	}
	
}
