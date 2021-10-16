package drudge.data;

import java.net.*;
import drudge.page.Page;


public class NoRobotsURLException extends UselessURLException {
private final static long serialVersionUID = 0;

	public NoRobotsURLException(Object p) {
	super(p, UselessMessages.NOROBOTS.mes);
	}
	
	public NoRobotsURLException(Object p, String m) {
	super(p, m);
	}

	public NoRobotsURLException(Object p, Object o) {
	super(p, o);
	}



}
