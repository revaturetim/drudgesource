package drudge.page;

import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;
import drudge.*;
import drudge.global.*;
import drudge.data.*;

//This is a utility class for pagesource so that it doesn't get to be a huge class object and/or file
class P {

//this are the standard strings for checkEntry descriptions
final static String nopag = "You are attempting to add, create, or use a null page object";
final static String nocon = "You did not create connection object first!  Program terminated!";
final static String nolnk = "You did not call page.connect() for";
final static String nosrc = "You did not call the getSource() method"; 
final static String nohed = "You did not call the getHeader() method";
final static String notitle = "NO TITLE FOUND";
final static private String nowords = "NO KEYWORDS FOUND";
final static private String Null = "null";

	static void checkHtmlFile(final URL url) throws InvalidURLException, URISyntaxException {
	//Debug.startWatch();
	//final String usr = url.getUserInfo();
	final String aut = url.getAuthority();
	//final String pat = url.getPath();
	//final String que = url.getQuery();
	final String schm = url.getProtocol();
	//final String frag = url.getFragment();
	//final String pth = url.getPath();
	//final String file = url.getFile();
	//final FileNameMap map = URLConnection.getFileNameMap();
	//final String ftype = map.getContentTypeFor(url.toString());	
	final boolean ishttp = schm.equals("http") || schm.equals("https");

		if (ishttp || schm.equals("file")) {
		final URI uri = url.toURI();//this throws URISyntaxExeption
		final String ftype = URLConnection.guessContentTypeFromName(url.toString());
		/* Collapsing this into one line of code instead of nesting if statements is suppose to make it faster
		 * This is an example of branchless programming
		 */
			if (ishttp && (uri.isOpaque() || (aut == null || aut.isEmpty()) || (ftype != null && !ftype.startsWith("text")))) {
			throw new InvalidURLException(url);
			}
		}
		else {	
		throw new InvalidURLException(url);
		}
	}

	//I am assuming that this works.  I have no way to really test it thoroughly. 
	static Data<URL> readRobotFile(final URLConnection rcon) throws IOException {
	final LineNumberReader reader = new LineNumberReader(new BufferedReader(new InputStreamReader(rcon.getInputStream())));
	final String useragent = "User-agent:";
	final String disallow = "Disallow:";
	final String comment = "#";
	final Data<URL> norob = new DataList<URL>();
	String user = null;
	
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			if (((HttpURLConnection)rcon).getResponseCode() != HttpURLConnection.HTTP_OK) {
			break;
			}
			if (line.startsWith(comment)) {
			continue;
			}
		String[] spaces = line.split(" ", 2);
			if (spaces.length == 2) {
			String action = spaces[0];
				if (action.equalsIgnoreCase(useragent)) {
				user = spaces[1];//this could throw an exception 
				}
				else if (action.equalsIgnoreCase(disallow)) {
					if (user.equals("*") | user.equalsIgnoreCase(ThisProgram.useragent)) {
					String dir = spaces[1];
						try {
						URL c = new URL(rcon.getURL(), dir);//this thows exception
						norob.add(c);
						}
						catch (MalformedURLException M) {
						Hashtable<String, Object> d = new Hashtable<String, Object>();	
						d.put("Robot URL", rcon.getURL());
						d.put("Directory", dir);
						d.put("Exception", M);		
						D.error(d);
						}
					}
				}
			}	
		}
	return norob;
	}

	static Data<URL> getEmails(CharSequence src, Data<URL> data) {
	final String s = src.toString().toLowerCase();
		
		for (int b = s.indexOf("\"mailto:"); b != -1; b = s.indexOf("\"mailto:", b + 1)) {
		int e = s.indexOf("\"", b + 1);
		String address = s.substring(b + 1, e);
			try {
			URL email = new URL(address);
			String query = "?" + email.getQuery();
			String frag = "#" + email.getRef();
				if (query != null) {
				address = address.replace(query, "");
				}
				if (frag != null) {
				address = address.replace(frag, "");
				}
			email = new URL(address);
			email = email.toURI().toURL();//this handles encoding of special escape charaters 
			data.put(email);
			}
			catch (MalformedURLException M) {
			Print.printRow(M, address);
			}
			catch (URISyntaxException U) {
			Print.printRow(U, address);
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
			catch (ExcludedURLException E) {
			E.printRow();
			}
		}
	return data;
	}

	static class Links {
		
		private static void findInPath(final String link, LinkAction<String> action) {
		/*Ths is to catch all subdirectories of a link*/
		final int doubleslash = link.indexOf("//");
			for (int slash = link.indexOf("/");slash != -1; slash = link.indexOf("/", slash + 1)) {
				if (slash > doubleslash + 1 && slash > doubleslash) {
				String sublink = link.substring(0, slash + 1);
				action.act(sublink.toString());
				}
			}
		action.act(link);
		}

		static void find(CharSequence source, LinkAction<String> action) {
		final String html = source.toString().toLowerCase();

			for (int b = html.indexOf("<"); b != -1; b = html.indexOf("<", b + 1)) {
			final int e = html.indexOf(">", b);
				if (e == -1) {
				continue;//this skips in case there is an error in the html
				}
			final String tag = html.substring(b, e);
			int r = tag.indexOf("href=\"");
				if (r == -1) {
				r = tag.indexOf("action=\"");
					if (r == -1) {
					r = tag.indexOf("src=\"");	
					}
				}

				if (r != -1) { 
				final int rbeg = tag.indexOf("\"", r);
				final int rend = tag.indexOf("\"", rbeg + 1);
				String link = tag.substring(rbeg + 1, rend);
				findInPath(link, action);
				}
			}
			for (int b = 0; b != -1; b = html.indexOf(">", b + 1)) {
			int e = html.indexOf("<", b);
				if (e == -1) {
				e = html.length();//this will see if there is a end quote
				}
				if (b > 0) {
				b++;//this will get rid of the '/' character
				}
			String text = html.substring(b, e);
			String[] words = text.split(" ");
				
				for (String word : words) {
					/*branchless programming*/
					if (word.startsWith("http://") 
						|| word.endsWith(".html") 
						|| word.endsWith(".shtml") 
						|| word.endsWith(".htm") 
						|| word.startsWith("https://") 
						|| word.startsWith("../") 
						|| word.startsWith("/")) {
					findInPath(word, action);//found absolute link
					}
				}
			}
		}
	}
	
	static class LinksRegex {
	
		void get(CharSequence source, LinkAction<String> action) {
		Matcher m1 = Patterns.Links.LINK.match(source);
		
			while (m1.find()) {
			String ref = m1.group();
			Matcher m2 = Patterns.Links.QUOTE.match(ref);
			
				if (m2.find()) {
				String tag = m2.group();
				Matcher m3 = Patterns.Links.Q2Q.match(tag);
					if (m3.find()) {
					String href = m3.group();
					Matcher m4 = Patterns.Links.Q2SL.match(href);
						if (m4.find()) {
						final int b = m4.start();	
						m4.reset();
							while (m4.find()) {//this treats all subdirectories found as a seperate link
							String link = href.substring(b, m4.end());
							action.act(link);
							}
						}
					}
				}	
			}
		}		
	}
	
	public static String getTitleByRegex(final CharSequence source) {
	Matcher m1 = Patterns.Title.TITLE.match(source);
	String text = notitle;	
		if (m1.find()) {
		String tag = m1.group();
		Matcher m2 = Patterns.Title.INTRA.match(tag);
			if (m2.find()) {
			text = m2.group();
			//this will cut off the end ><
			text = text.substring(1, text.length() - 1);
			//this will cut off the white spaces around it
			text = text.trim();
			}
		}
	return text;
	}

	public static String getTitle(final CharSequence source) { 
	String title = P.notitle;
	final String btit = "<title>";
	final String etit = "</title>";
	String text = source.toString().toLowerCase();
	int b = text.indexOf(btit);
	int e = text.indexOf(etit, b);
		if (b != -1 && e != -1) {
		b = b + btit.length();
		title = text.substring(b, e);
		}
	return title;
	}

	public static Data<String> getKeywords(final CharSequence source, Data<String> words) {
	final String text = source.toString().toLowerCase();
	String[] somewords = text.split("<.*>|\\s|<script.*script>|<style.*style>");//this splits and coincidently removes all whitespace characters
		for (String word : somewords) {
			if (word.length() > 2) {
				if (!DataObjects.donotusewords.contains(word)) {	
				words.add(word);
				}
			}
		}
	return words;
	}
	
	public static Data<String> getKeywords_old(final CharSequence source, Data<String> words) {
	final String text = source.toString().toLowerCase();
	int e = 0;
		for (int b = 0; b != -1; b = text.indexOf(">", b + 1)) {
		e = text.indexOf("<", b);
			if (e == -1) {
			e = text.length();
			}
		String sometext = text.substring(b, e);
		sometext = sometext.replace('>', ' ');//I don't know why I need this
		String[] somewords = sometext.split("\\s");//this splits and coincidently removes all whitespace characters
			for (String word : somewords) {
				if (word.length() > 2) {
					if (!DataObjects.donotusewords.contains(word)) {	
					words.add(word);
					}
					else Debug.print(word);
				}
			}
		}
	return words;
	}
	

	public static Data<String> getKeywordsByRegex(final CharSequence source, Data<String> words) {
	String text = source.toString().toLowerCase();
		for (Patterns.Keywords p : Patterns.Keywords.values()) {
		text = p.replace(text, " ");
		}
	String[] keywords = text.split(" ");
		for (String word : keywords) {
		words.add(word);
		}
	return words;
	}
    	
	static URLConnection createConnection(final URL url, final Proxy proxyserver) throws IOException {
	/*For some reason this won't work if the scheme is a mailto link
	 * so either isValid() has to be called or checkHTMLFile does
	 * Page constructer has to call these methods or it won't work.  
	 * I believe it is a bug in the API with openConnection(proxy)
	 */
	final URLConnection connection = url.openConnection(proxyserver);//this throws ioexception
	connection.setConnectTimeout(20*1000);
	connection.setReadTimeout(500);
	connection.addRequestProperty("GET", url.toString());
	connection.addRequestProperty("User-Agent", ThisProgram.useragent);
      	connection.addRequestProperty("Accept", "text/plain, text/html");
	connection.addRequestProperty("Accept-Encoding", "identity");
	connection.addRequestProperty("Accept-Charset", "ISO-8859-1, UTF-8");
	connection.addRequestProperty("Accept-Language", "en-US, en");
	return connection;
	}



	static void checkResponse(final int response, String page) throws NoContentURLException, RedirectedURLException, NotOKURLException {
		if (response >= 200 && response < 300) {//does the most common first 
			if (response == 204) {
			throw new NoContentURLException(page);
			}
		}
		else if (response >= 300 && response < 400) {
		throw new RedirectedURLException(page);
		}
		else {
		throw new NotOKURLException(page, String.valueOf(response));
		}
		
	Debug.time("...Response-Code");		
	}

	static void checkResponse(final String response, final String page) throws NoContentURLException, RedirectedURLException, NotOKURLException {
		if (response != null) {
		String[] values = response.split(" ");
			if (values[0].equals("HTTP/1.1")) {
				try { 
				Integer num = Integer.parseInt(values[1]);
				int code = num.intValue();
				checkResponse(code, page);
				}
				catch (NumberFormatException N) {
				D.error(N);	
				}
			}	
		}
	Debug.time("...Response-Code");		
	}

	static void checkContentType(final String contype, final String con) throws InvalidURLException {
		if (contype != null) {
		boolean ishtml = false;
		String[] values = contype.split(" ");
			for (String value : values) {
				String[] colon = value.split(";");
				value = colon[0];
				if ((value.equalsIgnoreCase("text/html") | value.equalsIgnoreCase("text/plain"))) {
				ishtml = true;
				break;
				}
			}
			if (!ishtml) {
			throw new InvalidURLException(con, contype);
			}
		}
	Debug.time("...Content-Type");		
	}

	static void checkContentLength(final int conlen, final String con) throws NoContentURLException {
	/*DON'T USE CONLEN VARIABLE FROM HEADER OR IT WILL NOT INDEX PAGE!!!!!*/
		if (conlen == 0) {
		throw new NoContentURLException(con);
		}	
	Debug.time("...Content-Length");		
	}

	static void checkContentEncoding(final String contenc, final String con) throws BadEncodingURLException {
		if (contenc != null) {
			/*if (contenc.equals("")) {
			throw new BadEncodingURLException(con.toString());
			}
			I don't know what to do with this one yet so I am going to comment it out
			*/
		}
	Debug.time("...Content-Encoding");			
	}

	/*checkHeaders methods do not throw all uselessurl exceptions so -U option may not catch them all*/
	static void checkHeaders(final URLConnection con) throws
	       	BadEncodingURLException, NoContentURLException,
	       	RedirectedURLException, NotOKURLException, InvalidURLException {
		
	final String u = con.toString();
		try {
		String response = ((HttpURLConnection)con).getResponseMessage();
		checkResponse(response, u);
		}
		catch (IOException I) {
		D.error(I);
		}
		catch (ClassCastException C) {
		D.error(C);
		}

	final String contype = con.getContentType();
	final String contenc = con.getContentEncoding();
	final int conlen = con.getContentLength();

	checkContentType(contype, u);	
	checkContentLength(conlen, u);
	checkContentEncoding(contenc, u);
	}


	static void checkHeaders(final Page.Header h, final String u) throws
	       	BadEncodingURLException, NoContentURLException,
	       	RedirectedURLException, NotOKURLException, InvalidURLException {
	
	final String response = h.getResponse();
	final String contype = h.getContentType();
	final String contenc = h.getContentEncoding();
	final int conlen = h.getContentLength(); 
	
	checkResponse(response, u);
	checkContentType(contype, u);
	checkContentLength(conlen, u);
	checkContentEncoding(contenc, u);
	}

	static void checkHeaders(final Page p) throws
	       	BadEncodingURLException, NoContentURLException,
	       	RedirectedURLException, NotOKURLException, InvalidURLException {
	final Page.Header h = p.header();
	final String u = p.toString();
	checkHeaders(h, u);
	}
	
	static Page.Source getSource(Page.Source source, URLConnection con) throws IOException {
	final BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()), source.length());//throws ioexcpetion above 
	boolean errorwrite = false;
		for (int i = 0; i < source.length(); i++) {
			try {
			int n =  reader.read();//this throws ioexception
				if (n != -1) {
				source.append((char)n, i);
				}
				else {
				break;
				}
			}
			catch (IOException I) {
				//this will make sure i only writes to the error log once!
				if (errorwrite == false) {
				Hashtable<String, Object> t = new Hashtable<String, Object>();
				t.put("Exception", I);
				t.put("Location", "P.getSouce(Souce, URLConnection)");
				t.put("Message", "How often does this occur?");
				t.put("Link", con.toString());
				D.error(t);
				errorwrite = true; 
				}
			}
		}
	reader.close();//closes the reader and throws ioexception
	return source;
	}

	static char[] getSource(char[] chars, URLConnection con) throws IOException {
	final BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()), chars.length);//throws ioexcpetion above 
	boolean errorwrite = false;
		for (int i = 0; i < chars.length; i++) {
			try {
			char c = (char)reader.read();//this throws ioexception
				if (c != -1) {
				chars[i] = c;
				}
				else {
				break;
				}
			}
			catch (IOException I) {
				//this will make sure i only writes to the error log once!
				if (errorwrite == false) {
				Hashtable<String, Object> t = new Hashtable<String, Object>();
				t.put("Exception", I);
				t.put("Location", "P.getSouce(char[], URLConnection)");
				t.put("Message", "How often does this occur?");
				t.put("Link", con.toString());
				D.error(t);
				errorwrite = true; 
				}
			}
		}
	reader.close();//closes the reader and throws ioexception
	return chars;
	}

	/*These methods are used to directly add a page into the dada variable*/
	static Data<Page> add(URL url, Data<Page> data) {
	Debug.check(url, null);
	Debug.check(data, null);
	final String msg = "This happen in P.add(URL)";

		try {
		Page page = new Page(url);
		data.put(page);
		}
		catch (DuplicateURLException Du) {
		Du.printRow();
		}
		catch (ExcludedURLException E) {
		E.printRow();
		}
		catch (URISyntaxException U) {
		Print.printRow(U, url);
		}
		catch (InvalidURLException I) {
		I.printRow();	
		}
		catch (IOException I) {
		Print.printRow(I, url);
		}
		
	return data;
	}

	static Data<Page> add(Page page, Data<Page> data) throws 
	DuplicateURLException, ExcludedURLException, InvalidURLException, 
	NoRobotsURLException, URISyntaxException {
	Debug.check(page, null);
	Debug.check(data, null);
	data.put(page);//this throws duplicateurlexception
	return data;
	}
	
	static Data<Page> add(Collection<Page> pages, Data<Page> data) {
	Debug.check(pages, null);
	Debug.check(data, null);
		for (Page page : pages) {
			try {
			data.put(page);
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
			catch (ExcludedURLException E) {
			E.printRow();
			}
		}
	return data;
	}

	static Data<Page> add(Page[] pages, Data<Page> data) {
	Debug.check(pages, null);
	Debug.check(data, null);
		for (Page page : pages) {
			try {
			data.put(page);
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
			catch (ExcludedURLException E) {
			E.printRow();
			}
		}
	return data;
	}
	
	public static boolean sameHost(Page a, Page b) {
	final String hosta = a.getURL().getHost();
	final String hostb = b.getURL().getHost();
	return hosta.equals(hostb);
	}
	
	static Page createPage(String url) {
	Page p = null;
		try {
		p = new Page(url);
		}
		catch (IOException I) {
		D.error(I);
		}
		catch (URISyntaxException U) {
		D.error(U);
		}
		catch (InvalidURLException I) {
		D.error(I);
		}
	return p;
	}
}
