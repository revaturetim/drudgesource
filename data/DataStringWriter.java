package drudge.data;

import java.io.*;
import java.util.*;
import java.net.*;
import drudge.global.FileNames;

/*This class is for the data storage of the links it finds using streams
 *I thought it was clever to write up
 */

public class DataStringWriter<T> extends DataWriter<T> {
final private PipedWriter pwriter = new PipedWriter();
final private PipedReader preader = new PipedReader();

	public DataStringWriter() {
	
	}

	protected LineNumberReader createReader() {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(preader));
		try {
		reader.setLineNumber(0);
		reader.mark(0);
		}
		catch (IOException I) {
		D.error(I);
		}
	return reader;
	}

	protected PrintWriter createWriter() {
	StringWriter stringwriter = new StringWriter();
		try {
		pwriter.connect(preader);
		}
		catch (IOException I) {
		D.error(I);
		}
	return new PrintWriter(new BufferedWriter(pwriter));
	}

}
