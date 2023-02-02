package drudge.data;

import java.net.*;
import drudge.page.Page;


public class RobotsExcludedURLException extends ExcludedURLException {
private final static long serialVersionUID = 0;

	public RobotsExcludedURLException(Object p) {
	super(p);
	message = UselessMessages.NOROBOTS.name();
	}


}
