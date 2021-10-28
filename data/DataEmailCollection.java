package drudge.data;

import java.io.*;
import java.net.*;

public class DataEmailCollection<T extends URL> extends DataCollection<T> {

	public DataEmailCollection(String s) {
	super(s);
	}

	public Object connect() throws IOException {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		String[] ns = line.split(CountFile.sep);
			try {
			final URL e = new URL(ns[0]);
				try {
				put((T)e);
				}
				catch (DuplicateURLException Du) {
				D.error(Du);
				}
			}
			catch (MalformedURLException M) {
			D.error(M);
			}
		}
	return null;
	}
	
	public Object disconnect(int data) throws IOException {
	BufferedWriter email_writer = new BufferedWriter(new FileWriter(source(), false));
		for (T t : this) {
		D.checkEntry(t, "DataEmailCollection.disconnect()");//this checks to make sure there were no null elements in data.
		URL email = t;
		email_writer.append(email.toString() + "\n");
		}
	email_writer.close();
	return null;
	}


}
