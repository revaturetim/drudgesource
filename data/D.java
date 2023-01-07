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
	
	static boolean checkErrorFile(String file) {
	System.out.println("Checking " + file + " file for errors.");
	boolean duplicate = false;
	boolean linkerror = false;
	int linecount = 0;
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
		int firstcolumnlength = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			linecount = reader.getLineNumber();
			String[] columns = line.split(CountFile.sep);
			/*This will see if the total number of columns for each line is the same as the previous one*/
				if (linecount == 1) {
				firstcolumnlength = columns.length;
				}
				if (columns.length != firstcolumnlength) {
				System.out.println("...There is a column length error at line " + String.valueOf(linecount));
				}
			/*this will check for decoding errors*/
			String link = columns[0];
				if (link.contains("%")) {
				System.out.println("...Possible encoding error at line " + String.valueOf(linecount));
				}
				
			/*This will check for duplicates in the data*/
				try {
				Page p = new Page(link);//this throws malformedurlexception
				p.isValid();//this throws InvalidURLException, URISyntaxException
				URL u = p.getURL();
					if (u.getAuthority() == null) {
					throw new MalformedURLException("No Host");
					}
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(file)));
					for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine()) {
					int linecount2 = reader2.getLineNumber();
						if (linecount2 < linecount) {
						String[] columns2 = line2.split(CountFile.sep);
						String link2 = columns2[0];
							if (link.equals(link2)) {
							duplicate = true;
							System.out.print("..." + link + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) 
					       	+ " and " 
					       	+ String.valueOf(linecount2));
							}
						}
					}
				}
				catch (InvalidURLException N) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't an html file: ");
				System.out.println(link);
				}
				catch (URISyntaxException U) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
				catch (MalformedURLException M) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
			}
		System.out.println("...The number of lines in " + file +  " is "  + linecount + ".");
		}
		catch (IOException I) {
		D.error(I);
		}
	return (duplicate || linkerror);
	}
	
}
