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
final static private String encoding = "UTF-8";

	static void checkHtmlFile(final URL url) throws InvalidURLException, URISyntaxException {
	//Debug.startWatch();
	final URI uri = url.toURI();//this throws URISyntaxExeption
	//final String usr = url.getUserInfo();
	final String aut = url.getAuthority();
	//final String pat = url.getPath();
	//final String que = url.getQuery();
	final String schm = url.getProtocol();
	//final String frag = url.getFragment();
	//final String pth = url.getPath();
	//final String file = url.getFile();
	//final FileNameMap map = URLConnection.getFileNameMap();
	final String ftype = URLConnection.guessContentTypeFromName(url.toString());
	final boolean ishttp = schm.equals("http") || schm.equals("https");
	//final boolean isfile = schm.equals("file");
	final boolean isemail = schm.equals("mailto");
	final boolean isimage = ftype != null && ftype.startsWith("image");
	final boolean istext = ftype == null || ftype.startsWith("text");
		if (isemail) {
		throw new EmailURLException(url);
		}
		else if (isimage) {
		throw new ImageURLException(url);
		}
		else if ((ishttp && (uri.isOpaque() || aut == null || aut.isEmpty())) || istext == false) {
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
						norob.put(c);
						}
						catch (MalformedURLException M) {
						D.error("Robot URL", rcon.getURL(), "Directory", dir, "Exception", M);		
						}
						catch (DuplicateURLException Du) {
						D.error("Robot URL", rcon.getURL(), "Duplicate", dir, "Exception", Du);
						}
					}
				}
			}	
		}
	return norob;
	}

	static String decode(String link) {
		try {
		link = URLDecoder.decode(link, P.encoding);
		}
		catch (UnsupportedEncodingException U) {
		D.error(U.getClass(), U, "Location", "P.decode(string)");
		}
		catch (IllegalArgumentException I) {
		D.error(I.getClass(), I, "Location", "P.decode(string)");
		}
	return link;
	}	

	static String encode(String link) {
		try {
		link = URLEncoder.encode(link, P.encoding);
		}
		catch (UnsupportedEncodingException U) {
		D.error(U.getClass(), U, "Location", "P.encode(string)");
		}
	return link;
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
			D.error(M.getClass(), M, "Location", "P.getEmails(charsequence, data)", "url", address);
			}
			catch (URISyntaxException U) {
			D.error(U.getClass(), U, "Location", "P.getEmails(Charsequence, data)", "url", address);	
			}
			catch (DuplicateURLException Du) {
			
			}
		}
	return data;
	}

	static class Links {
		
		private static void findInPath(final String link, LinkFilter<String> action) {
		/*Ths is to catch all subdirectories of a link*/
		final int doubleslash = link.indexOf("//");
			for (int slash = link.indexOf("/");slash != -1; slash = link.indexOf("/", slash + 1)) {
				if (slash > doubleslash + 1 && slash > doubleslash) {
				String sublink = link.substring(0, slash + 1);
				action.act(sublink);
				}
			}
		action.act(link);
		}

		static void find(CharSequence source, LinkFilter<String> action) {
		final String html = source.toString().toLowerCase();

			for (int b = html.indexOf("<"); b != -1; b = html.indexOf("<", b + 1)) {
			final int e = html.indexOf(">", b);
				if (e == -1) {
				continue;//this skips in case there is an _error in the html
				}
			final String tag = html.substring(b, e);
			int r = tag.indexOf("href=");
				if (r == -1) {
				r = tag.indexOf("action=");
					if (r == -1) {
					r = tag.indexOf("src=");	
					}
				}

				if (r != -1) { 
				//this is for normal two quotes
				int rbeg = tag.indexOf("\"", r);
				int rend = tag.indexOf("\"", rbeg + 1);
					//this is for no quotes
					if (rbeg == -1) {
					rbeg = tag.indexOf("=", r);
					rend = tag.length();
					}
					//this is for one quote or more
					else if (rend == -1) {
					rend = tag.length();
					}
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
						|| word.startsWith("/")
						|| word.startsWith("./")) {
					findInPath(word, action);
					}
					else if (word.contains("@")/*this is for emails*/) {
						try {
						String mailword = URI.create(word).toString();
						findInPath("mailto:" + mailword, action);
						}
						catch (IllegalArgumentException I) {
						//Debug.here(I);temporary solution to prevent it from find random text in page 
						}
					}
				}
			}
		}
	}
	
	static class LinksRegex {
	
		void get(CharSequence source, LinkFilter<String> action) {
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
							Links.findInPath(link, action);
							}
						}
					}
				}	
			}
		}		
	}
	
	static String getTitleByRegex(final CharSequence source) {
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

	static String getTitle(final CharSequence source) { 
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

	static Data<String> getKeywords(final CharSequence source, Data<String> words) {
	final String text = source.toString().toLowerCase();
	String[] somewords = text.split("<.*>|\\s|<script.*script>|<style.*style>");//this splits and coincidently removes all whitespace characters
		for (String word : somewords) {
			if (word.length() > 2) {
				if (!DataEnum.words.data.contains(word)) {	
					try {
					words.put(word);
					}
					catch (DuplicateURLException D) {

					}
				}
			}
		}
	return words;
	}
	
	static Data<String> getKeywords_old(final CharSequence source, Data<String> words) {
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
					if (!DataEnum.words.data.contains(word)) {	
						try {
						words.put(word);
						}
						catch (DuplicateURLException D) {

						}
					}
				}
			}
		}
	return words;
	}
	

	static Data<String> getKeywordsByRegex(final CharSequence source, Data<String> words) {
	String text = source.toString().toLowerCase();
		for (Patterns.Keywords p : Patterns.Keywords.values()) {
		text = p.replace(text, " ");
		}
	String[] keywords = text.split(" ");
		for (String word : keywords) {
			try {
			words.put(word);
			}
			catch (DuplicateURLException D) {
	
			}
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
		throw new NotOKURLException(page, Integer.valueOf(response));
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
				D.error(N.getClass(), N, "Location", "P.checkResponse(string, string)");	
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
	static void checkHeaders(final URLConnection con) throws BadEncodingURLException, NoContentURLException, RedirectedURLException, NotOKURLException, InvalidURLException {
	final String u = con.toString();
		try {
		String response = ((HttpURLConnection)con).getResponseMessage();
		checkResponse(response, u);
		}
		catch (IOException I) {
		D.error(I.getClass(), I, "Location", "P.checkHeaders(URLConnection)");
		}
		catch (ClassCastException C) {
		D.error(C.getClass(), C, "Location", "P.checkHeaders(URLConnection)");
		}

	final String contype = con.getContentType();
	final String contenc = con.getContentEncoding();
	final int conlen = con.getContentLength();

	checkContentType(contype, u);	
	checkContentLength(conlen, u);
	checkContentEncoding(contenc, u);
	}


	static void checkHeaders(final Page.Header h, final String u) throws BadEncodingURLException, NoContentURLException, RedirectedURLException, NotOKURLException, InvalidURLException {
	final String response = h.getResponse();
	final String contype = h.getContentType();
	final String contenc = h.getContentEncoding();
	final int conlen = h.getContentLength(); 
	
	checkResponse(response, u);
	checkContentType(contype, u);
	checkContentLength(conlen, u);
	checkContentEncoding(contenc, u);
	}

	static void checkHeaders(final Page p) throws BadEncodingURLException, NoContentURLException, RedirectedURLException, NotOKURLException, InvalidURLException {
	final Page.Header h = p.header();
	final String u = p.toString();
	checkHeaders(h, u);
	}
	
	/* Use String.append() instead of String + String for all getSouce() methods
	 * I discovered it was much faster that way
	 */
	static Page.Source getSource(Page.Source source, URLConnection connection) throws IOException, SocketTimeoutException {
	final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), source.length());//throws ioexcpetion and sockettimeoutexception 
		for (int i = 0; i < source.length(); i++) {
			try {
			reader.mark(0);
			int n =  reader.read();//this throws ioexception
				if (n == -1) {
				break;
				}
			source.append((char)n, i);
			}
			catch (IOException I) {
			D.error(I.getClass().getName(), I, "Location", "Page.getSource(Page.source, URLConnection");
			reader.reset();//this eliminates the need to have errorwrite boolean to avoid repeat thrown exceptions
			}
		}
	reader.close();//closes the reader and throws ioexception
	return source;
	}

	/* Use String.append() instead of String + String for all getSouce() methods
	 * I discovered it was much faster that way
	 */
	static Page.Source getSource_old(Page.Source source, URLConnection con) throws IOException {
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
				//this will make sure i only writes to the _error log once!
				if (errorwrite == false) {
				D.error("Exception", I, "Location", "P.getSouce(Souce, URLConnection)", "Message", "How often does this occur?", "Link", con);
				errorwrite = true; 
				}
			}
		}
	reader.close();//closes the reader and throws ioexception
	return source;
	}

	/* Use String.append() instead of String + String for all getSouce() methods
	 * I discovered it was much faster that way
	 */
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
				//this will make sure i only writes to the _error log once!
				if (errorwrite == false) {
				D.error("Exception", I, "Location", "P.getSouce(char[], URLConnection)", "Message", "How often does this occur?", "Link", con);
				errorwrite = true; 
				}
			}
		}
	reader.close();//closes the reader and throws ioexception
	return chars;
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
		D.error(I.getClass().getName(), I);
		}
		catch (URISyntaxException U) {
		D.error(U.getClass().getName(), U);
		}
		catch (InvalidURLException I) {
		D.error(I.getClass().getName(), I);
		}
	return p;
	}
}
