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
private URL roboturl = null;
private URLConnection connection = null;
private boolean didconnect = false;
private int linkcount = 0;

	/*This will be the sample page constructor*/
	public Page() throws MalformedURLException, IOException {
	this.url = new File(FileNames.samphtml).toURI().toURL();//this will throw malformedurlexception
	this.connection = P.createConnection(this.url, Page.proxyserver);//this will throw the IOException
	this.roboturl = new File(FileNames.samprobot).toURI().toURL();//this will throw a malformedurlexception
	}

	public Page(URL u) throws IOException {
	this.url = u;
	this.connection = P.createConnection(this.url, Page.proxyserver);//this will throw the IOException
	roboturl = P.createURL(this.url, "/robots.txt");// /robots.txt denotes top level directory
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

	public Data getLinks() {
	final Data links = new DataList();
		LinkFilter<String> action = new LinkFilter<String>() {
			
			public void act(String link) {
			Page p = PageFactory.create(url, link);
			links.add(p);
			}
		};
		if (content.wasFilled() && content.size() > 0) {
		P.Links.find(content, action);
		}
	this.linkcount = links.size();
	Debug.time("Getting Links");
	return links;
	}

	public int getLinkCount() {
	return linkcount;
	}

	public URLConnection getConnection() {
	return connection;
	}

	public Data getEmails() {
	final Data emails = P.getEmails(content, new DataListEmail());
	Debug.time("Getting Emails");
	return emails;
	}

	public String getTitle() {
	String title = P.getTitle(content);
	Debug.time("Getting Title");
	return title;
	}
	
	public Data getKeywords() {
	Data keywords = P.getKeywordsByBuffer(content, new DataList());
	Debug.time("Getting Keywords");
	return keywords;
	}

	
	public boolean isValid() throws URISyntaxException, InvalidURLException {
	P.checkHtmlFile(url);//this will throw InvalidURLException when it is not an html file and URISyntaxException
	return true;
	}

	public URL getURL() {
	return url;
	}

	public URL getRobotURL() {
	return roboturl;
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
	P.checkIncluded(this.url, DataEnum.include.data);	
	return true;
	}
	
	public boolean isExcluded() throws ExcludedURLException {
	P.checkExcluded(this.url, DataEnum.exclude.data, true);
	return false;
	}

	public boolean isRobotExcluded() throws RobotsExcludedURLException {
		try {
		final URLConnection c = roboturl.openConnection(Page.proxyserver);
			for (Data disallowed = DataEnum.norobots.get(this.roboturl); disallowed == null; disallowed = DataEnum.norobots.get(this.roboturl)) {
			disallowed = P.readRobotFile(c);
			DataEnum.norobots.put(roboturl, disallowed);
			}
			Data urls = DataEnum.norobots.get(this.roboturl);
				if (urls.contains(this.url)) { 
				throw new RobotsExcludedURLException(this.url);
				}
			}
		catch (IOException I) {
		D.error(I.getClass().getName(), I, "Location", "Page.isRobotExcluded()");
		}
	return true;
	}

	//inner classes - Header - Source 
	final public class Header implements Serializable {
	public String response = "";
	public String contentlength = "";
	public String contentencoding = "";
	public String contenttype = "";
	public String redirectlocation = "";
		/* While Java URLConnection class may do the same thing this was
		 * the only way I could make sure it wasn't establishing a new
		 * connection each time it was looking for headers.  This way 
		 * it only makes one connection to the server instead of multiple 
		 * connections!  This makes this a friendlier crawler since it 
		 * doesn't eat up server resources.  Hooray!
		 */
		public void checkUseless() throws RedirectedURLException, NotOKURLException, InvalidURLException, NoContentURLException, BadEncodingURLException {
		final Map<String, List<String>> headermap = connection.getHeaderFields();
			
			for (final String field : headermap.keySet()) {
				if (field == null) {
				response = getFirstValue(headermap.get(field), 1);
					try {
					String[] splits = response.split(" ");
					final int code = Integer.valueOf(splits[1]);
						switch (code) {

						case 204: throw new NoContentURLException(Page.this, response);
						/* no reason to cascade through these since they are positive responses 
						 * and this only catches negative reponses
						 */
						/*case 100:
						case 101:
						case 103:
						case 200:
						case 201:
						case 202:
						case 203:
						case 205:
						case 206: break;*/
						case 300:
						case 301:
						case 302:
						case 303:
						case 304:
						case 307:
						case 308: 
							redirectlocation = getFirstValue(headermap.get("Location"), 1);
							throw new RedirectedURLException(Page.this, redirectlocation);
						case 400:
						case 401:
						case 402:
						case 403:
						case 404:
						case 405:
						case 406:
						case 407:
						case 408:
						case 409:
						case 410:
						case 411:
						case 412:
						case 413:
						case 414:
						case 415:
						case 416:
						case 417:
						case 500:
						case 501:
						case 502:
						case 503:
						case 504:
						case 505:
						case 511:
						case 600: throw new NotOKURLException(Page.this, response);
						default: break;
						}
					}
					catch (NumberFormatException N) {
					D.error(N.getClass(), N, "Location", "Page.header.checkUseless()");	
					}
				}
				else if (field.equalsIgnoreCase("Content-Encoding")) {
				contentencoding = getFirstValue(headermap.get(field), 1);
				/*I don't know what to do with this one yet so I am going to comment it out*/
				}
				else if (field.equalsIgnoreCase("Content-Length")) {
				contentlength = getFirstValue(headermap.get(field), 1);
					try {
					int conlen = Integer.valueOf(contentlength.toString());
						if (conlen == 0) {
						throw new NoContentURLException(Page.this);
						}
					}
					catch (NumberFormatException N) {
					D.error(N, contentlength);
					}
				}	
				else if (field.equalsIgnoreCase("Content-Type")) {
				contenttype = getFirstValue(headermap.get(field), 1);
					if ((contenttype.contains(P.html) || contenttype.contains(P.plain)) == false) {
					throw new InvalidURLException(Page.this, field);
					}
				}
			}

		}

		final private String getFirstValue(List<String> list, int n) {
		String value = new String();
			if (list != null && n <= list.size()) {
			n--;//decrement since this is 1-based not 0-based
			value = list.get(n);
			}
		return value;
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
		
		/* Use String.append() instead of String + String for all getSouce() methods
		 * I discovered it was much faster that way
		 */
		public Source fill() {
			try {
			P.getSource(this, Page.this.connection);
			Page.this.didconnect = true;
			}
			catch (SocketTimeoutException S) {
			Print.row(S, Page.this);
			}
			catch (IOException I) {
			Print.row(I, Page.this);
			}
		return this;
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
