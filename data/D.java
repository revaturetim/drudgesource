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


	public static void error(Map<String, Object> M) {
	final String beg = "!-----";
	final String end = "-----!";	
		try {
		final BufferedWriter err = new BufferedWriter(new FileWriter(error, true));
		err.write(beg);	
		final String time = getDate();
		err.write(time);
		err.write(end);
		err.write("\n");

			for (String k : M.keySet()) {
			Object v = M.get(k);
				if (v == null) v = "NULL VALUE";
			err.append("[");
			err.append(k);
			err.append("]-[");
			err.append(v.toString());
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
		err.close();//this should append errors to file
		}
		catch (IOException I) {

		}
	}

	public static void error(Exception E) {
	error(E.getClass().getName(), E.toString());
	}

	public static void error(String...errors) {
	
		if (errors.length % 2 == 0) {
		Hashtable<String, Object> t = new Hashtable<String, Object>();
		int i = 0;
			while (i < errors.length) {
			String err = errors[i];
			i++;
			String msg = errors[i];
			t.put(err, msg);
			i++;
			}
		error(t);
		}
		else {
		throw new IllegalArgumentException("D.error() arguments must be in pairs!");
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
		D.error(U);
		}
		catch (InvalidURLException I) {
		D.error(I);
		}
		catch (MalformedURLException M) {
		D.error(M);
		}
		catch (IOException I) {
		D.error(I);
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
			writer.append(page.toString() + CountFile.sep);
			}
			else if (r < 2) {//title
			writer.append(page.getTitle() + CountFile.sep);
			}				
			else if (r < 3) {//keywords
			writer.append(rawString(page.getKeywords()));
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
			if (!Page.robotsallowed) {
			p.isRobotExcluded();//this throws RobotsExcludedURLException	
			Debug.time("Checking Robot Allowed");
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
		catch (DuplicateURLException Du) {
		Du.printRow();
		}
		catch (RobotsExcludedURLException R) {
		R.printRow();
		}
		catch (ExcludedURLException E) {
		E.printRow();
		}
		catch (EmailURLException E) {
		E.printRow();
			if (Page.getemails) {
				try {
				URL mailurl = (URL)E.getObject();//must cast to ensure it is correct
				DataObjects.dada_emails.put(mailurl);
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
				URL imageurl = (URL)I.getObject();//must cast to ensure it is correct
				DataObjects.dada_images.put(imageurl);
				}
				catch (DuplicateURLException Du) {
				/*do nothing because I don't need to know if exception is thrown*/	
				}
			}
		}
		catch (InvalidURLException U) {
		U.printRow();
		}
	return data;
	}

	static public Data<Page> add(URL url, Data<Page> data) {
		try {
		Page p = new Page(url);
		data.put(p);
		}
		catch (MalformedURLException M) {
		Print.row(M, url);
		}
		catch (URISyntaxException U) {
		Print.row(U, url);
		}
		catch (IOException I) {
		Print.row(I, url);
		}
		catch (DuplicateURLException Du) {
		Du.printRow();
		}
		catch (EmailURLException E) {
		E.printRow();
			if (Page.getemails) {
				try {
				DataObjects.dada_emails.put(url);
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
				DataObjects.dada_images.put(url);
				}
				catch (DuplicateURLException Du) {
				/*do nothing because I don't need to know if exception is thrown*/
				}
			}
		}
		/*catch (ExcludedURLException Ex) {
		Ex.printRow();
		}*/
		catch (InvalidURLException U) {
		U.printRow();
		}
	return data;
	}
	
	static public Data<Page> add(String link, Data<Page> data) {
		try {
		Page p = new Page(link);
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
		catch (DuplicateURLException Du) {
		Du.printRow();
		}
		catch (EmailURLException E) {
		E.printRow();
			if (Page.getemails) {
			throw new UnsupportedOperationException("Can't Get Emails");	
			}
		}
		catch (ImageURLException I) {
		I.printRow();
			if (Page.getimages) {
			throw new UnsupportedOperationException("Can't Get Images");
			}
		}
		/*catch (ExcludedURLException Ex) {
		Ex.printRow();
		}*/
		catch (InvalidURLException U) {
		U.printRow();
		}
	return data;
	}

}
