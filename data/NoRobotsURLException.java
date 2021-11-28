package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NoRobotsURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NoRobotsURLException(Object p) {
	super(p);
	message = UselessMessages.NOROBOTS.mes;
	}


}
