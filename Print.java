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

	static void row(final Page p, final int c) {
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
	Print.row(fields);
	}

	public static void columnHeaders() {
	Print.row(System.out, H);
	}
	
	public static void row(final String...S) {
	Print.row(GSTREAM, S);
	}
	
	public static void row(Exception exc, Object obj) {
	
		if (Print.UselessClass == UselessURLException.class || Print.UselessClass == exc.getClass()) {
		String mexc = exc.getClass().getSimpleName().toUpperCase();
		String url = obj.toString();
		Print.row(GSTREAM, "", "", "", "", mexc, url);
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
		else if (key.equals(Help.about.parameter) || key.equals(Help.a.parameter)) {
		System.out.println("------About------");
		System.out.println("This program is a spider program that attempts ");
		System.out.println("to index webpage on the world wide web");
		System.out.println(ThisProgram.name + " " + ThisProgram.version);
		System.out.println("by " + ThisProgram.author);
		}
		else if (key.equals(Help.i.parameter)) {
		System.out.println("This is to read inputs from an input file called \"" + FileNames.in + "\"");
		System.out.println("The first line of the file should be identical to the commands you want to use for this program");
		System.out.println("The file should only have to be one line and is a convenience so you don't have to type them in yourself");
		System.out.println("You're Welcome");
		}
		else if (key.equals(Help.l.parameter)) {
		System.out.println("This will print out the license agreement");
		}
		else if (key.equals(Help.o.parameter)) {
		System.out.println("This is to send all output to a file called \"" + FileNames.out + "\"");
		}
		else if (key.equals(Help.d.parameter)) {
		System.out.println("This is used to set what you will be filling in for final data");
		System.out.println("The default value is -d=1 which will be just place urls in datafile");
		System.out.println("A value of -d=2 will insert page url and page titles into datafile");
		System.out.println("A value of -d=3 will insert page url, page titles, and page keywords");
		}
		else if (key.equals(Help.e.parameter)) {
		System.out.println("This is to collect emails and store them in a seperate email file called " + FileNames.emails);
		}
		else if (key.equals(Help.m.parameter)) {
		System.out.println("Each Exception is numbered and if you want to see them in the output you have to use a number");
			for (UselessMessages U : UselessMessages.values()) {
			System.out.printf("%-4s %s\n", U.num, U.hlp); 
			}
		System.out.printf("See if you can find them all!\n");
		}
		else if (key.equals(Help.c.parameter)) {
		System.out.println("Option c is used for the different crawl methods that are available to this program.");
		System.out.println("Method 1 is the basic web spider of this program.  It spiders links in the order that it finds them.");
		System.out.println("Method 2 is meant to be a website overload tool and not to be used by nice people.");
		System.out.println("Method 5 randomizes the order of the links so that it doesn't spiders them in the order that they are found.");
		System.out.println("Method 6 searches links that are not a not from the same domain first.");
		System.out.println("Method 7 crawls redirected links as they are found");
		System.out.println("Method 8 sorts data in purley alpha-numeric order and is faster than other methods");
		}
		else if (key.equals(Help.n.parameter)) {
		System.out.println("This is to determine the number of cycles this program will execute");
		System.out.println("Example:  -n=(number of cycles) http://my.website.com/goes/here");
		System.out.println("This is the most important part of the program inputs because it will not do anything without this");
		System.out.println("A url must follow the -n option or it won't work");
		System.out.println("This option plus the url must be the last commands or it wont work");
		System.out.println("Example:  -m=(message) -c=(crawl method) -n=(1, 2, 3...) http://my.website.com/goes/here");
		}
		else if (key.equals(Help.x.parameter)) {
		System.out.println("This is used for extra options for various commands.");
		System.out.println("See specific help on how it is used with those options.:)");
		}
		else if (key.equals(Help.s.parameter)) {
		System.out.println("This is used for continuation purposes so you don't have to start over every time you start program.");
		System.out.println("Just specify a entry number to start from and use the -n option to specify where it should stop.");
		System.out.println("Example:  >drudge -n=100 -s=10"); 
		System.out.println("Example:  drudge -n=100 -s to tell this program to pick up where it last left off.");
		System.out.println("This will start at the entry of 10 and stop at 100.");
		}
		else if (key.equals(Help.samp.parameter) || key.equals(Help.t.parameter)) {
		System.out.println("The -samp (or -t) option can be used only as the last parameter and in place of the url parameter.");
		System.out.println("It is used as a test page for testing this program.");
		}
		else if (key.equals(Help.p.parameter)) {
		System.out.println("This option is used in case you want to use a proxy server");
		System.out.println("Example: -p=www.proxyserver.com");
		}
		else if (key.equals(Help.r.parameter)) {
		System.out.println("This is used when you want to respect robots.txt policies of a website.");
		System.out.println("Warning: Don't assume this will always work because not all robots.txt files are made correctly!");
		}
		else if (key.equals(Help.inc.parameter) || key.equals(Help.include.parameter)) {
		System.out.println("This is to include only urls listed in the " + FileNames.exclude + " file");
		System.out.println("This is a very inclusive option because it will include all urls listed in file");
		System.out.println("Example: -inc");
		System.out.println("You can also use -include= if you want to use your own custom file");
		System.out.println("Example: -include=your/custom/file/here");
		}
		else if (key.equals(Help.exc.parameter) || key.equals(Help.exclude.parameter)) {
		System.out.println("This is to exclude urls listed in the " + FileNames.exclude + " file");
		System.out.println("This is a very exclusive option because it will exlcude all other urls listed in file");
		System.out.println("Example: -exc");
		System.out.println("You can also use -exclude= if you want to use your own custom file");
		System.out.println("Example: -exclude=your/custom/file/here");
		}
		else if (key.equals(Help.nok.parameter)) {
		System.out.println("This will force the spider to only accept all HTTP responses.");
		System.out.println("This option will greatly speed up crawling but it may not get quality websites since it accepts useless urls.");
		System.out.println("Having this option on will capture redirects which can lead to better quality results.");
		System.out.println("It is suggested that this be turned off and only turned on for faster crawling.");
		}
		else if (key.equals(Help.w.parameter)) {
		System.out.println("This will slow your crawler down between page crawls");
		System.out.println("Example: -w=889 will be 889 milliseconds between crawls.");
		}

	}


}
