package drudge.page;

import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;
import java.lang.ref.*;

import drudge.*;
import drudge.spider.*;
import drudge.data.*;
import drudge.global.*;

/*this is the skeletal class of all page objects*/
final public class Page implements Serializable {
final static private int pagesize = 35_000;
static public Proxy proxyserver = Proxy.NO_PROXY;
//private variables for info about page itself which are set to the default values
//The default value for MOST of these should be null
final private Source content = new Source(pagesize);
final private Header header = new Header();
private URL url = null;  
private URLConnection connection = null;
private boolean didconnect = false;
final private Data<Page> dlist = new DataList<Page>();
final private Data<URL> elist = new DataListEmail<URL>();
final private Data<String> klist = new DataList<String>();

	public Page(URL u) throws IOException, URISyntaxException, InvalidURLException {
	this.url = u;
	P.checkHtmlFile(this.url);//this throws InvalidURLException and URISyntaxException
	this.connection = P.createConnection(this.url, Page.proxyserver);//this will throw the IOException
	}
	
	public Page(URL p, String l) throws MalformedURLException, IOException, URISyntaxException, InvalidURLException {
	this.url = new URL(p, P.decode(l));//this throws malformedurlexception
	P.checkHtmlFile(this.url);//this throws InvalidURLException and URISyntaxException
	this.connection = P.createConnection(this.url, Page.proxyserver);//this will throw the IOException
	}

	public Page(String u) throws MalformedURLException, IOException, URISyntaxException, InvalidURLException  {
	this.url = new URL(P.decode(u));//this will throw malformedurlexception
	P.checkHtmlFile(this.url);//this throws InvalidURLException and URISyntaxException
	this.connection = P.createConnection(this.url, Page.proxyserver);//this will throw the IOException
	}
	
	public Page(String u, String l) throws MalformedURLException, IOException, URISyntaxException, InvalidURLException {
	URL purl = new URL(P.decode(u));//this will throw malformedurlexception
	this.url = new URL(purl, P.decode(l));//this will throw malformedurlexception
	P.checkHtmlFile(this.url);//this throws InvalidURLException and URISyntaxException
	this.connection = P.createConnection(this.url, Page.proxyserver);//this will throw the IOException
	}
	
	public Page(Page op, String l) throws MalformedURLException, IOException, URISyntaxException, InvalidURLException  {
	URL oldurl = op.getURL();
	this.url = new URL(oldurl, P.decode(l));//this will throw malformedurlexception
	P.checkHtmlFile(this.url);//this throws InvalidURLException and URISyntaxException
	this.connection = P.createConnection(this.url, Page.proxyserver);//this will throw the IOException
	}
	
	public boolean equals(Object obj) {
	boolean isequal = false;
		if (obj instanceof Page) {
		String urlstring = this.toString();
		String pstring = obj.toString();
		isequal = urlstring.equals(pstring);
		}
	return isequal;
	}
	
	/*Most override to avoid warning*/
	public int hashCode() {
	return toString().hashCode();
	}

	public Data<Page> getLinks(LinkFilter filter) {
		if (content.wasFilled() && content.size() > 0) {
		P.Links.find(content, filter);
		}
	Debug.time("Getting Links Filter");
	return dlist;
	}
	
	public Data<Page> getLinks() {
		LinkFilter<String> action = new LinkFilter<String>() {
			
			public void act(String link) {
			P.add(url, link, dlist);
			}
		};
	this.getLinks(action);
	Debug.time("Getting Links");
	return dlist;
	}
	
	public Data<URL> getEmails() {
	P.getEmails(content, elist);
	Debug.time("Getting Emails");
	return elist;
	}

	public int getEmailCount() {
	return elist.size();
	}

	public int getLinkCount() {
	return dlist.size();
	}

	public String getTitle() {
	String title = P.getTitle(content);
	Debug.time("Getting Title");
	return title;
	}
	
	public Data<String> getKeywords() {
	P.getKeywords(content, klist);
	Debug.time("Getting Keywords");
	return klist;
	}

	public int getKeywordCount() {
	return klist.size();
	}

	//this will essentiallly handle all the prechecking needed before it gets content
	public boolean isUseless() throws RedirectedURLException, InvalidURLException, NotOKURLException, NoContentURLException, BadEncodingURLException {
	Debug.time("Checking Uselessness");
	P.checkHeaders(header, toString());
	return false; //if it made it this far then this is the default answer
	}

	public boolean isValid() throws URISyntaxException, InvalidURLException {
	P.checkHtmlFile(url);//this will throw InvalidURLException when it is not an html file and URISyntaxException
	return true;
	}

	/* Use String.append() instead of String + String for all getSouce() methods
	 * I discovered it was much faster that way
	 */
	public Source getSource() {
		try {
		P.getSource(content, connection);
		didconnect = true;
		}
		catch (SocketTimeoutException S) {
		Print.row(S, this);
		}
		catch (IOException I) {
		Print.row(I, this);
		}
	return content;
	}
	
	public URL getURL() {
	return url;
	}
	
	public URLConnection getConnection() {
	return connection;
	}

	public URL getURL(final String sample, final String real) {
	URL url = null;//default value
		if (this.url.getProtocol().equals("file")) {	
			try {
			File r = new File(sample);
			url = new URL(r.toURI().toString());
			}
			catch (IOException I) {
			D.error(I);
			}
		
		}
		else {
			try {
			url = new URL(this.url, real);
			}
			catch (IOException I) {
			D.error(I);
			}
		}
	return url;
	}
	
	public Header header() {
	return header;
	}
     	
	public Source source() {
	return content;
	}

	public String toString() {
	return url.toString();
	}

	public boolean didConnect() {
	return didconnect;
	}

	public boolean isIncluded() throws ExcludedURLException {
	boolean included = false;
		for (URL u : DataObjects.include) {
		included = this.toString().contains(u.toString());
			if (included) {
			break;
			}
		}
		if (included == false) {
		throw new ExcludedURLException(this.url);
		}	
	return true;
	}
	
	public boolean isExcluded() throws ExcludedURLException {
		if (DataObjects.exclude.contains(this.url)) {
		throw new ExcludedURLException(this.url);
		}
	return false;
	}

	public boolean isRobotExcluded() throws RobotsExcludedURLException {
	final URL roboturl = getURL(FileNames.samprobot, "/robots.txt");
		if (!DataObjects.norobots.containsKey(roboturl)) {
			try {
			final URLConnection c = roboturl.openConnection(Page.proxyserver);
			final Data<URL> dr = P.readRobotFile(c);
			DataObjects.norobots.put(roboturl, dr);
				//this is for add disallowed urls to database
				for (URL use : dr) {
				P.add(use, DataObjects.dada);
				}
			}
			catch (IOException I) {
			D.error(I);
			}
		}
		else {
		Data<URL> urls = DataObjects.norobots.get(roboturl);
			if (urls.contains(this.url)) { 
			throw new RobotsExcludedURLException(this.url);
			}
		}
	return true;
	}

	//inner classes - Header - Source 
	final public class Header implements Serializable {
	
		public String getRedirectLocation() {
		final String loc = "Location";
		return get(loc);
		}
		
		public String get(String key) {
		int k = 1;//this has to start at one so it will work.  This won't get null key or OK message status since that is 0
		String field = null;
			for (field = connection.getHeaderField(k); field != null; field = connection.getHeaderField(k)) {
			/*I had to make this because jave api is case sensitive*/
			String ke = connection.getHeaderFieldKey(k);
				if (ke.equalsIgnoreCase(key)) {
				break;
				}
			k++;
			} 
		return field;
		}

		public String getResponse() {
		String r = connection.getHeaderField(null);
		return r;
		}
			
		public int getResponseCode() {
		int code = -1;
			try {
			HttpURLConnection h = (HttpURLConnection)connection;
			code = h.getResponseCode();
			}
			catch (ClassCastException C) {
			D.error(C);
			}
			catch (IOException I) {
			D.error(I);
			}
		return code;
		}

		public int getContentLength() {
		int l = -1;
		final String length = "Content-Length";
		final String r = get(length);
			if (r != null) {
				try {
				Integer I = Integer.valueOf(r);
				l = I.intValue();
				}
				catch (NumberFormatException N) {
				D.error(N);
				}
			}
		return l;
		}

		public String getContentEncoding() {
		final String encoding = "Content-Encoding";
		String r = get(encoding);
		return r;
		}

		public String getContentType() {
		final String type = "Content-Type";
		String r = get(type);
		return r;
		}

	}//end of headers class 
	
	final public class Source implements CharSequence, Serializable { 
	/*This is really an attempt to make my own string object to increase efficiency of string, stringbuffer classes */
	private int size = 0;
	private char[] allc = null;	
	private boolean filled = false;

		private Source(int s) {
		allc = new char[s];	
		}
		
		private Source(char[] c) {
		allc = c;
		}

		void append(char c, int i) {
		allc[i] = c;
		size++;
		filled = true;
		}
		
		public boolean isComplete() {
		return (size >= allc.length);//if size is greater than array length then it is complete else it isn't
		/* for some reason using this branchless programming sped up my program a lot.  
		 * I don't know why.  It just did.
		 */
		}
		
		public boolean wasFilled() {
		return filled;
		}
		
		public char charAt(int i) {
		return allc[i];
		}
		
		public int size() {
		return size;
		}

		public int length() {
		return allc.length;
		}

		public String toString() {
		return new String(allc);
		}

		public CharSequence subSequence(final int b, final int e) {
		int l = e - b;
		Source s = new Source(l);
			for (int i = 0; i < l; i++) {
			s.append(allc[b + i], i);
			}
		return s;
		}
	}//end of Source class
}//end of page class
