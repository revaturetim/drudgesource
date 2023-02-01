package drudge.data;

import java.io.*;
import java.net.*;
import java.sql.*;

public enum UselessMessages {
NONE(null, "", "This is for no messages"), 
ALL(UselessURLException.class, "USELESSURL", "This is for all messages"),	
USELESS(UselessURLException.class, "USELESSURL", "This is for all uselessurls as well as all messages"),
DUPLICATE(DuplicateURLException.class, "DUPLICATE", "This is for all duplicate urls that are found"),
EXCLUDED(ExcludedURLException.class, "EXCLUDED", "This if for all excluded urls that are found"), 
NOTHTML(InvalidURLException.class, "INVALIDFILE", "This is for cases when a url is not a valid html file for this program to use"),
NOTOK(NotOKURLException.class, "NOTOKRESPONSE", "This is when the response is not and OK response"),
NOLINKS(NoLinksURLException.class, "NOLINKSFOUND", "This is when there are no links found on a page"),
FILETOBIG(FileToBigURLException.class, "FILETOBIG", "This is when a file is too big"),
NOCONTENT(NoContentURLException.class, "NOCONTENT", "This is when there is no content generated by the link"),
REDIRECTED(RedirectedURLException.class, "REDIRECTEDURL", "This is when a url request gets redirected to another url"),
BADENCODE(BadEncodingURLException.class, "BADENCODE", "This is when there is a bad encoding"),
NOROBOTS(RobotsExcludedURLException.class, "NOROBOTSALLOWED", "This is when the website doesn't allow robots"),
EMAIL(EmailURLException.class, "EMAIL", "This is when it finds an email link"),
IMAGE(ImageURLException.class, "IMAGE", "This is when it finds an image link"),

IO(IOException.class, "INPUTOUTPUT", "This indicates an IO exception has occurred"),
MALFORMED(MalformedURLException.class, "MALFORMEDURL", "A malformed url was used"),
UNKNOWNHOST(UnknownHostException.class, "UNKNOWNHOST", "This is when an unknown host is used"), 

EXCEPTION(Exception.class, "ANYEXCEPTION", "This is used for any and all exceptions!"),
NULLPOINT(NullPointerException.class, "NULLPOINTER", "This is used to indicate a null pointer was found."),
ILLEGALARG(IllegalArgumentException.class, "ILLEGALARGUMENT", "Somewhere an illegal vale was passed into a method"),

SQL(SQLException.class, "SQLEXCEPTION", "This is used for all SQL exceptions!")	
	
;
/*This is for the actual class for the enum values above*/
public String mes = "";
public String hlp = "No Help at all!";
public Class<?> cls = null;

	private UselessMessages(Class<?> c) {
	cls = c;
	}

	private UselessMessages(Class<?> c, String m) {
	this(c);
	mes = m;
	}
	
	private UselessMessages(Class<?> c, String m, String h) {
	this(c, m);
	hlp = h;
	}

	public static UselessMessages get(int c) {
	UselessMessages useless = UselessMessages.NONE;
		for (UselessMessages u : values()) {
			if (u.ordinal() == c) {
			useless = u;
			break;
			}
		}
	return useless;
	}

}
