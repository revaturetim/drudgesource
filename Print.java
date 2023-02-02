package drudge;

import drudge.page.Page;
import drudge.global.*;
import drudge.data.*;
import java.io.*;
import java.net.*;

final public class Print {
static PrintStream GSTREAM = System.out; 
static public UselessMessages uselessmessage = UselessMessages.NONE;
static public int cycle = 1;	
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

	static public void row(final Page p) {
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
	fields[0] = String.valueOf(Print.cycle);
	fields[1] = String.valueOf(DataEnum.links.data.size());
	fields[2] = nump;
	fields[3] = h.response;
	fields[4] = h.contenttype;
	fields[5] = p.toString();
	Print.row(fields);
	Print.cycle++;//must increment to get correct cyclecount
	}

	public static void columnHeaders() {
	Print.row(System.out, H);
	}
	
	public static void row(final String...S) {
	Print.row(GSTREAM, S);
	}
	
	public static void row(Exception exc, Object obj) {
	String mexc =  (exc instanceof UselessURLException) ? exc.toString().toUpperCase() : exc.getClass().getSimpleName().toUpperCase();

		if (Print.uselessmessage == UselessMessages.ALL || Print.uselessmessage.cls == exc.getClass()) {
		String url = obj.toString();
		Print.row("", "", "", "", mexc, url);
		}
	}
	
	private static void row(PrintStream stream, final String...S) {
	
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

	static private void center(String s, final int l, PrintStream printer) {
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
	static void error(Exception except) {
	error(except, except.getMessage());
	}

	static void error(Exception except, Object url) {
	System.out.println("A " + except.getClass().getName() + " has occured at " + url.toString());
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
	error(u, u.getFirstObject());
	}

	static void error(UselessURLException u, Object url) {
	System.out.println(url.toString() + " is a useless url for this program");
	System.out.println("Why? " + u.getClass().getName());
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

	static void helpMenu(final String key) {
		
		if (key.equals(DevHelp.HELP.parameter)) {
			for (DevHelp h : DevHelp.values()) {
				if (!h.message.isEmpty()) {
				System.out.printf("%-10s" + h.message + "\n", h.parameter);
				}
			}
		}
		else if (key.equals(Help.help.parameter) || key.equals(Help.h.parameter)) {
			for (Help h : Help.values()) {
				if (!h.message.isEmpty()) {
				System.out.printf("%-10s" + h.message + "\n", h.parameter);
				}
			}
		}
		else if (key.equals(Help.m.parameter)) {
			for (UselessMessages U : UselessMessages.values()) {
			System.out.printf("%-4s %s\n", U.ordinal(), U.hlp);
			}
		System.out.printf("See if you can find them all!\n");
		}
		else {
			for (Help h : Help.values()) {
				if (key.equals(h.parameter)) {
				System.out.println(h.submessage);
				}

			}
		
		}
	}


}
