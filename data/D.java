package drudge.data;

import drudge.global.*;
import drudge.*;
import drudge.page.Page;
import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.*;

//this is basically a utility class for this package and other packages

final public class D implements FileNames {


	public static  void error(Object...errors) {
			if (errors.length % 2 != 0) {
			throw new IllegalArgumentException("D.error() arguments must be in pairs!");
			}
	final String beg = "!-----";
	final String end = "-----!";	
		try {
		final BufferedWriter err = new BufferedWriter(new FileWriter(FileNames.error, true));
		err.write(beg);	
		final String time = getDate();
		err.write(time);
		err.write(end);
		err.write("\n");
			
			for (int i = 0; i < errors.length; i += 2) {
			String message = errors[i].toString();
			Object the_error = errors[i + 1];
				if (the_error == null) {
				the_error = "NULL VALUE";
				}
			err.append("[");
			err.append(message);
			err.append("]-[");
			err.append(the_error.toString());
			err.append("]\n");
			}

		int l = beg.length() + time.length() + end.length();
		int lm = (l) / 2;

			for (int i = 0; i < l; i++) {
				if (i == 0 || i == l - 1) {
				err.write("!");
				}
				else if (i == (lm - 1)) {
				err.write("E");
				}
				else if ( i == (lm)) {
				err.write("n");
				}
				else if ( i == (lm + 1)) {
				err.write("d"); 
				}
				else {
				err.write("-");
				}
			}
		err.write("\n\n");
		err.close();//this should append _errors to file
		}
		catch (IOException I) {

		}
	}

	public static String getDate() {
	GregorianCalendar G = new GregorianCalendar();
	Locale L = Locale.getDefault();
	G.setLenient(false);
	String y = String.valueOf(G.get(Calendar.YEAR));
	String o = G.getDisplayName(Calendar.MONTH, Calendar.LONG, L);
	int d = G.get(Calendar.DAY_OF_MONTH);
	String h = String.valueOf(G.get(Calendar.HOUR));
	int i = G.get(Calendar.MINUTE);
	String m = String.valueOf(G.get(Calendar.MINUTE));
		if (i < 10) {
		m = "0" + String.valueOf(i);
		}
	String a = G.getDisplayName(Calendar.AM_PM, Calendar.LONG, L);
	String time = y + " " + o + " " + d + " " + h + ":" + m + " " + a;
	return time;
	}
	
	static Page getPageFromEntry(String line) {
	Page page = null;
		try {
		String[] entries = line.split(CountFile.sep);
		page = new Page(entries[0]);
		}
		catch (URISyntaxException U) {
		D.error("Exception", U, "Location", "D.getPageFromEntry(String)");
		}
		catch (InvalidURLException I) {
		D.error("Exception", I, "Location", "D.getPageFromEntry(String)");
		}
		catch (MalformedURLException M) {
		D.error("Exception", M, "Location", "D.getPageFromEntry(String)");
		}
		catch (IOException I) {
		D.error("Exception", I, "Location", "D.getPageFromEntry(String)");
		}
	return page;
	}
	
	static <T> String rawString(Data<T> data) {
	StringBuilder builder = new StringBuilder();
		for (T t : data) {
		builder.append(t.toString());
		builder.append(" ");
		}
	return builder.toString();
	}
	
	static void writeEntry(Page page, Writer writer, int level) throws IOException {
		for (int r = 0; r < level; r++) {
			if (r < 1) {//link
			writer.append(page.toString());
			}
			else if (r < 2) {//title
			writer.append(CountFile.sep + page.getTitle());
			}				
			else if (r < 3) {//keywords
			writer.append(CountFile.sep + rawString(page.getKeywords()));
			}
		}
	writer.append("\n");
	}
	
	/*These methods are used to directly add a page into the dada variable*/
	static public Data add(URL url, String link, Data<Page> data) {
		try {
		Page p = new Page(url, link);
			/*this should be in this order so that you can use include and exclude at the same time*/
			if (Page.donotgetexcluded) {
			p.isExcluded();
			}
			if (Page.getincluded) {
			p.isIncluded();
			}
		data.put(p);
		}
		catch (MalformedURLException M) {
		Print.row(M, link);
		}
		catch (URISyntaxException U) {
		Print.row(U, link);
		}
		catch (IOException I) {
		Print.row(I, link);
		}
		catch (EmailURLException E) {
		E.printRow();
			if (Page.getemails) {
				try {
				URL mailurl = (URL)E.getFirstObject();//must cast to ensure it is correct
				DataEnum.emails.data.put(mailurl);
				}
				catch (DuplicateURLException Du) {
				/*do nothing because I don't need to know if exception is thrown*/
				}
			}
		}
		catch (ImageURLException I) {
		I.printRow();
			if (Page.getimages) {
				try {
				URL imageurl = (URL)I.getFirstObject();//must cast to ensure it is correct
				DataEnum.images.data.put(imageurl);
				}
				catch (DuplicateURLException Du) {
				/*do nothing because I don't need to know if exception is thrown*/	
				}
			}
		}
		catch (InvalidURLException U) {
		U.printRow();
		}
		catch (ExcludedURLException E) {
		E.printRow();
		}
		catch (DuplicateURLException Du) {
		Du.printRow();
		}
	return data;
	}
	
	static public Data<Page> add(String link, Data<Page> data) {
	return add(null, link, data);//this should get identical results because null is passed in as the url argument
	}
	
	static public Data<Page> add(Page page, Data<Page> data) {
		try {
		data.put(page);
		}
		catch (DuplicateURLException Du) {
		Du.printRow();
		}
	return data;
	}

	static public Data<Page> add(URL url, Data<Page> data) {
	return add(url, new String(), data);//this should work identically as add(url, string, data) because it is using empty string	
	}
	
	static public Page createPage(final String link) {
	Page page = null;
		try {
		page = new Page(link);
		}
		catch (InvalidURLException I) {
		I.printRow();
		}
		catch (URISyntaxException U) {
		Print.row(U, link);
		}
		catch (MalformedURLException M) {
		Print.row(M, link);
		}
		catch (IOException I) {
		Print.row(I, link);
		}
	return page;
	}

	static public Page createPage(final URL url) {
	Page page = null;
		try {
		page = new Page(url);
		}
		catch (InvalidURLException I) {
		I.printRow();
		}
		catch (URISyntaxException U) {
		Print.row(U, url);
		}
		catch (MalformedURLException M) {
		Print.row(M, url);
		}
		catch (IOException I) {
		Print.row(I, url);
		}
	return page;
	}

}
