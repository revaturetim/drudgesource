package drudge.data;

import drudge.global.*;
import drudge.*;
import drudge.page.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.*;

//this is basically a utility class for this package and other packages

final public class D implements FileNames {
final static private String encoding = "UTF-8";
final static private String NVAL = "NULL VALUE";

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
			String the_error = (errors[i + 1] == null) ? NVAL : errors[i + 1].toString();
			String the_error_message = (errors[i] == null) ? NVAL : errors[i].toString();
			err.append("[");
			err.append(the_error_message);
			err.append("]-[");
			err.append(the_error);
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

	static public void flush(String filename) {
		try {
		FileWriter writer = new FileWriter(filename);
		writer.close();//removes contents of file
		}
		catch (IOException I) {
		D.error(I);
		}
	}
	
	static public String decode(String string) {
		try {
		string = URLDecoder.decode(string, D.encoding);
		}
		catch (UnsupportedEncodingException U) {
		D.error(U.getClass(), U, "Location", "D.decode(string)");
		}
		catch (IllegalArgumentException I) {
		D.error(I.getClass(), I, "Location", "D.decode(string)");
		}
	return string;
	}	

	static public String encode(String string) {
		try {
		string = URLEncoder.encode(string, D.encoding);
		}
		catch (UnsupportedEncodingException U) {
		D.error(U.getClass(), U, "Location", "D.encode(string)");
		}
	return string;
	}
	
	static public String toASCII(String string) {
		try {
		string = IDN.toASCII(string);
		}
		catch (IllegalArgumentException I) {
		D.error("Exception", I, "Location", "D.toASCII(String)");	
		}
	return string;
	}

	static public String toUnicode(String string){
	return IDN.toUnicode(string);
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
	
	static String rawString(Data data) {
	StringBuilder builder = new StringBuilder();
		for (Object obj : data) {
		builder.append((String)obj);
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

	static void readEntries(Data data) throws IOException {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(data.source())));//this throws IOException and breaks the method if it can't read from soruce
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		String[] alink = line.split(CountFile.sep);
		Page page = PageFactory.createFromString(alink[0]);
		data.add(page);
		}
	reader.close();
	}

	static void writeEntries(Data data) throws IOException {
	final BufferedWriter writer = new BufferedWriter(new FileWriter(data.source()));
		for (Object t : data) {
		Page page = (Page)t;
		D.writeEntry(page, writer, data.level());
		}
	writer.close();
	}

	static void writeResponse(String...responses ) {
	System.out.print("\tLINE:\t" + responses[0] + " : " + responses[1]);
	System.out.println("\t" + responses[2]);
	}

	static void writeBeginningResponse(String source) {
	System.out.println("Checking " + source + " for errors......................");
	}

	static void writeDuplicateResponse(String...responses) {
	System.out.println("\tLINE:\t" + responses[0] + " : " + responses[1] + " is a duplicate entry");
	}

	static void writeEncodingResponse(int linecount) {
	System.out.println("\tLINE:\t" + String.valueOf(linecount) + " Possible Encoding Error!");
	}
	
	static void writeColumnLengthResponse(int linecount) {
	System.out.println("\tLINE:\t" + String.valueOf(linecount) + " Columan Length Doesn't Match!");
	}

	static void writeFinalResponse(int linecount) {
	System.out.println("\tTOTAL LINES:\tis "  + String.valueOf(linecount));
	}
}
