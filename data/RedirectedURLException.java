package drudge.data;

import java.net.*;
import drudge.page.Page;



public class RedirectedURLException extends UselessURLException {
private final static long serialVersionUID = 0;
private String redir = null;

	public RedirectedURLException(Object p) {
	super(p, UselessMessages.REDIRECTED.mes);
	}

	public RedirectedURLException(Object p, String m) {
	super(p, m);
	}

	public RedirectedURLException(Object p, String m, String r) {
	this(p, m);
	redir = r;
	}

	public String getRedirect() {
	return redir;
	}
}
