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
	Hashtable<String, Object> t = new Hashtable<String, Object>();
	t.put("Exception", E);
	error(t);
	}
	
	public static void error(boolean test, String msg) {
		if (test) {
		error(msg);
		throw new IllegalStateException(msg);//this will terminate program
		}
	}
	
	public static void error(String msg) {
	error("ERROR", msg);	
	}

	public static void error(String err, String msg) {
	Hashtable<String, Object> t = new Hashtable<String, Object>();
	t.put(err, msg);
	error(t);
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
}
