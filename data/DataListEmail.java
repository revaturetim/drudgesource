package drudge.data;

import java.io.*;
import java.net.*;
import drudge.Debug;

public class DataListEmail<T extends URL> extends DataList<T> {

	public DataListEmail() {
	super();
	}
	
	public DataListEmail(String s) {
	super(s);
	}

	@SuppressWarnings("unchecked")	
	public void  begin() throws IOException {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		String[] ns = line.split(CountFile.sep);
			try {
			final URL e = new URL(ns[0]);
			this.put((T)e);
			}
			catch (MalformedURLException M) {
			D.error(M.getClass(), M);
			}
			catch (DuplicateURLException Du) {
			D.error(Du.getClass(), Du);
			}
		}
	}
	
	public void end() throws IOException {
	final BufferedWriter email_writer = new BufferedWriter(new FileWriter(source()));
		for (T t : this) {
		URL email = t;
		email_writer.append(email.toString());
		email_writer.append("\n");
		}
	email_writer.close();
	}
	
	/*since these values can't be changed they alwasy return a default value*/
	public int level() {
	return 1;
	}
	
	public boolean checkError() {
	System.out.println("Checking " + source() + " file for errors.");
	boolean duplicate = false;
	boolean linkerror = false;
	int linecount = 0;
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
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
				for (String col : columns) {
					if (col.contains("%")) {
					System.out.println("...Possible encoding error at line " + String.valueOf(linecount));
					}
				}
			/*This will check for duplicates in the data*/
			String email = columns[level() - 1];
				try {
				URL u = new URL(email);
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine()) {
					int linecount2 = reader2.getLineNumber();
						if (linecount2 < linecount) {
						String[] columns2 = line2.split(CountFile.sep);
						String email2 = columns2[0];
							if (email.equals(email2)) {
							duplicate = true;
							System.out.print("..." + email + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) 
					       	+ " and " 
					       	+ String.valueOf(linecount2));
							}
						}
					}
				}
				catch (MalformedURLException M) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a email that isn't quite right: ");
				System.out.println(email);
				}
			}
		System.out.println("...The number of lines in " + source() +  " is "  + linecount);
		}
		catch (IOException I) {
		System.out.println(I);
		}
	return (duplicate || linkerror);
	}


}
