package drudge;

import drudge.page.Page;
import drudge.global.*;
import drudge.data.*;
import java.io.*;
import java.net.*;

final public class Print {
static PrintStream GSTREAM = System.out; 
static public Class<?> UselessClass = null;  
	
final private static int LEFT = 0;
final private static int CENTER = 1;
final private static int RIGHT = 2;
final private static String div = " : ";

//this is for standard column headers and String.length() is used to determine uniorm column widths
final private static String[] H = {
	"Cycle   ", 
	"Total", 
	" Found", 
	" HTTP Response ", 
	"     Content Type      ",  
	"Actual Link                                                "
};

	static void printRow(final Page p, final int c) {
	final int n = p.getLinkCount();
	String nump = String.valueOf(n);
		if (p.didConnect()) {
			if (p.source().isComplete()) {
			nump = nump + "/" + nump; 
			}
			else {
			nump = nump + "/?";
			}
		}
		else {
		nump = "-> <-";
		}
	Page.Header h = p.header();
	String[] fields = new String[6];
	fields[0] = String.valueOf(c + 1);
	fields[1] = String.valueOf(DataObjects.dada.size());
	fields[2] = nump;
	fields[3] = h.getResponse();
	fields[4] = h.getContentType();
	fields[5] = p.toString();
	printRow(fields);
	}

	public static void printColumnHeaders() {
	printRow(System.out, H);
	}
	
	public static void printRow(final String...S) {
	printRow(GSTREAM, S);
	}
	
	public static void printRow(Exception exc, Object obj) {
	
		if (Print.UselessClass == UselessURLException.class || Print.UselessClass == exc.getClass()) {
		String mexc = exc.getClass().getSimpleName().toUpperCase();
		String url = obj.toString();
		printRow(GSTREAM, "", "", "", "", mexc, url);
		}
	}
	
	private static void printRow(PrintStream stream, final String...S) {
	
		if (S.length == H.length) {	
			for (int i = 0; i < H.length; i++) {
				if (i < H.length - 1) {
					if (S[i] == null) {
					stream.print(fill('?', H[i].length()));
					stream.print(div);
					}
					else {
					stream.print(fixLength(S[i], H[i].length(), CENTER));
					stream.print(div);
					}
				}
				else {
				stream.println(format(S[i], H[i].length()));//this is the last column
				}
			}
		}
		else if (S.length != H.length) {
		throw new IllegalArgumentException("Column size number does not match in D.printRow(PrintStream, String...).  Fix it!");
		}
	}
	
	private static String fill(final char c, final int l) {
	char[] f = new char[l];
		for (int i = 0; i < f.length; i++) {
		f[i] = c;
		}
	return new String(f);
	}
	
	private static void fill(final char c, final int l, PrintStream printer) {
	char[] f = new char[l];
		for (char n : f) {
		printer.print(c);
		}
	}

	private static String format(String m, int l) {
	int s = m.length();
		if (s > l) {
		m = "...".concat(m.substring((s - 20)));
		}
	return m;
	}

	static private String fixLength(String s, final int l, int a) {

		if (s.length() < l) {
		
			switch (a) {
			
				case CENTER: 
				final int c = (l - s.length()) / 2;
					for (int i = 0; i < c; i++) {
					s = " ".concat(s);
					}

				case LEFT:
					while (s.length() < l) {
					s = s.concat(" ");
					}	
				break;

				case RIGHT:
					while (s.length() < l) {
					s = " ".concat(s);
					}
				break;
			}
		}
		else if(s.length() >= l) {
		s = s.substring(0, l);
		}
	return s;
	}

	static private void printCenter(String s, final int l, PrintStream printer) {
	int c = (l - s.length()) / 2;
		if (c < 0) {
		c = 0;
		}

		for (int i = 0; i < l; i++) {
			if (i == c) {
			printer.print(s);
			i = i + s.length();
			}
			else {
			printer.print(' ');
			}
		}
	}
	
//these are error methods used when an error occurs
	static void error(Object aba) {
	System.out.println(aba.toString());
	}

	static void error(Exception aba) {
	System.out.println(aba.toString());
	}		
	
	static void error(IOException aba, Object uri) {
	System.out.println("An IO exception occurred at " + uri.toString());
	}

	static void error(IllegalArgumentException aba, Object uri) {
	System.out.println("An IllegalArgument Exception has occurrred for " + uri.toString());
	}

	static void error(MalformedURLException M, Object url) {
	System.out.println("A MalformedURLException occurred at " + url.toString());
	}

	static void error(URISyntaxException u, Object uri) {
	System.out.println(uri.toString() + " did not conform to RFC2396 standard");
	}

	static void error(Integer aba) {
	System.out.println("You have to use a positive number greater than zero! Ex: 1,2,3...");
	System.out.println(aba.toString());
	}
				
	static void error(NumberFormatException n, String num) {
	System.out.print(num);
	System.out.println(" is not a number!");
	System.out.println(n.toString());
	}

	static void error(UselessURLException u) {
	System.out.println(u.getUrl() + " is a useless url for this program");
	System.out.println("Why? " + u.toString());
	}

	static void error(UselessURLException u, Object url) {
	System.out.println("URL: " + url.toString());
	error(u);
	}

	static void error(InvalidURLException n) {
	System.out.println("URL:" + n.getUrl() + " is not a valid html file this program!");
	}
	
	static void totalTime(long time) {
	if (time < 60_000) {
		double tsec = (double)time / 1000.0;
		System.out.printf("%2.4f seconds\n", tsec);
		}
		else if (time < 3_600_000) {
		double tmin = time / 60_000.0;
		System.out.printf("%2.4f minutes\n", tmin);
		}
		else {
		System.out.println(time);
		}	
	}



}
