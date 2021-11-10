package drudge.data;

import java.io.*;
import java.net.*;
import drudge.Debug;

public class DataEmailCollection<T extends URL> extends DataCollection<T> {

	public DataEmailCollection(String s) {
	super(s);
	}

	@SuppressWarnings("unchecked")	
	public void  begin() throws IOException {
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
	}
	
	public void finish() throws IOException {
	BufferedWriter email_writer = new BufferedWriter(new FileWriter(source(), false));
		for (T t : this) {
		Debug.check(t, null);
		URL email = t;
			for (int i = 0; i < level(); i++) {
				if (i == 0) {
				email_writer.append(email.toString() + CountFile.sep);
				}
			}
		email_writer.append("\n");
		}
	email_writer.close();
	}

	public int level() {
	return 1;
	}


}
