package drudge.data;

import java.net.*;
import drudge.page.Page;



public class RedirectedURLException extends UselessURLException {
private final static long serialVersionUID = 0;
private String redir = null;

	public RedirectedURLException(Object p) {
	super(p);
	message = UselessMessages.REDIRECTED.mes;
	}

	public RedirectedURLException(Object p, String r) {
	this(p);
	redir = r;
	}

	public String getRedirect() {
	return redir;
	}
}
