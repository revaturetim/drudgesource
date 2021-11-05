package drudge.page;

import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;
import java.lang.ref.*;

import drudge.*;
import drudge.spider.*;
import drudge.data.*;

/*this is the skeletal class of all page objects*/
final public class Page {
static public Proxy proxyserver = Proxy.NO_PROXY;
static public int data = 1;
final static private int pagesize = 35_000;
//private variables for info about page itself which are set to the default values
//The default value for MOST of these should be null
final private Source content = new Source(pagesize);
final private Header header = new Header();
private URL url = null;  
private URL roboturl = null;
private URLConnection connection = null;	
final private Data<Page> dlist = new DataList<Page>();
private Data<URL> elist = new DataList<URL>();
private Data<String> klist = new DataList<String>();
private boolean connected = false;

	public Page(URL u) throws URISyntaxException, NotHTMLURLException, IOException {
	Debug.check(u, null, P.nopag);
	url = u;
	P.checkHtmlFile(url);//this will throw nothtmlurlexception when it is not an html file and URISyntaxException
	connection = P.getConnection(url, proxyserver);//this throws IOException 
	}
	
	public Page(URL p, String l) throws URISyntaxException, NotHTMLURLException, MalformedURLException, IOException {
	this(new URL(p, l));//this throws malformedurlexception
	}

	public Page(String u) throws URISyntaxException, NotHTMLURLException, MalformedURLException, IOException {
	this(new URL(u));//this will throw malformedurlexception while page(URL) constructor throws the rest
	}
	
	public Page(String u, String l) throws URISyntaxException, NotHTMLURLException, MalformedURLException, IOException {
	this(new URL(u), l);//this will throw exceptions
	}
	
	public boolean equals(Object obj) {
	boolean isequal = false;
		if (obj instanceof Page) {
		String urlstring = this.toString();
		String pstring = obj.toString();
		int e = urlstring.compareTo(pstring);
			if (e == 0) {
			isequal = true;
			}
		}
	return isequal;
	}

	/*Most override to avoid warning*/
	public int hashCode() {
	return toString().hashCode();
	}
	
	public Data<Page> getLinks() {
		//I just liked anonymous classes/methods so I decided to keep this
		//It also refers to the this object url variable for its absolute path
		if (content.wasFilled() && content.size() > 0) {
			P.LinkAction<String> action = new P.LinkAction<String>() {
				
				public void act(String link) throws URISyntaxException, UselessURLException, MalformedURLException, IOException {
				Page p = new Page(url, link);
				//WeakReference<Page> w = new WeakReference<Page>(p);
				//SoftReference<Page> s = new SoftReference<Page>(p);
				dlist.put(p);
				}
			};
		P.GetLinkAction.get(content, action);
		}
	Debug.endCycleTime("Getting Links");
	return dlist;
	}
	
	public Data<URL> getEmails() {
	elist = P.getEmails(content, elist);
	Debug.endCycleTime("Getting Emails");
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
	Debug.endCycleTime("Getting Title");
	return title;
	}
	
	public Data<String> getKeywords() {
	klist = P.getKeywords(content, klist);
	Debug.endCycleTime("Getting Keywords");
	return klist;
	}

	public int getKeywordCount() {
	return klist.size();
	}

	//this will essentiallly handle all the prechecking needed before it gets content
	public boolean isUseless() throws RedirectedURLException, NotHTMLURLException, 
	       NotOKURLException, NoContentURLException, BadEncodingURLException {
	Debug.endCycleTime("Checking Uselessness");
	P.checkHeaders(header, toString());
	return false; //if it made it this far then this is the default answer
	}

	public boolean isUselessByURLConnection() throws RedirectedURLException, NotHTMLURLException, 
	       NotOKURLException, NoContentURLException, BadEncodingURLException {
	Debug.endCycleTime("Checking Uselessness");
	Debug.check(connection, null, P.nocon);
	P.checkHeaders(connection);
	return false; //if it made it this far then this is the default answer
	}

	public Source getSource() throws IOException {
	//throws ioexcpetion above 
	final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), content.length());
	connected = true;
		for (int i = 0; i < content.length(); i++) {
			try {
			reader.mark(0);
			int n =  reader.read();//this throws ioexception
				if (n == -1) {
				break;
				}
			content.append((char)n, i);
			}
			catch (IOException I) {
			//Debug.here(I);
			reader.reset();
			}
		}
	reader.close();//closes the reader and throws ioexception
	return content;
	}
	
	public Data<String> getRow() {
	DataCollection<String> row = new DataCollection<String>();
		for (int r = 0; r < Page.data; r++) {
			if (r == 0) {
			row.add(this.toString());
			}
			else if (r == 1) {
			row.add(this.getTitle());
			}
			else if (r == 2) {
			row.add(this.getKeywords().rawString());
			}
		}
	return row;
	}

	public URL getURL() {
	return url;
	}

	public URLConnection connection() {
	return connection;
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
	return connected;
	}

	public boolean isExcluded(boolean exc) throws ExcludedURLException {
	String host1 = getURL().getHost();
		for (Page elink : Drudge.excludedlinks) {
		String host2 = elink.getURL().getHost();
			if (host1.equals(host2) == exc/*This determines the include/exlude behavior*/) {
			throw new ExcludedURLException(this);
			}
		}
	return false;
	}

	public boolean isRobotAllowed() throws NoRobotsURLException {
		try {
			/*this should stop it from getting new roboturl for each new link it finds*/
			if (roboturl == null) {
			roboturl = new URL(url, "/robots.txt");
			}
		P.checkRobot(roboturl, url);//this throws NoRobotsURLException 	
		}
		catch (IOException I) {
		D.error(I);
		}
	return true;
	}

	//inner classes - Header - Source 
	final public class Header {
			
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
	
	final public class Source implements CharSequence { 
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
		boolean complete = false;
			if (size < allc.length) {
			complete = false;
			}
			else {
			complete = true;
			}
		return complete;
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
