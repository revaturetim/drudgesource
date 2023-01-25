package drudge.data;

import java.io.*;
import java.util.*;
import java.net.*;
import drudge.global.*;
import drudge.page.*;
import drudge.*;
/*This class is for the data storage of the links it finds*/

public class DataFileWriter extends DataWriter {

	public DataFileWriter(String f) {
	source = f;
	}

	protected LineNumberReader createReader() {
		try {
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source)));
		reader.setLineNumber(0);
		reader.mark(0);
		return reader;
		}
		catch (IOException I) {
		D.error(I);
		return null;
		}
	}
	
	protected PrintWriter createWriter() {
		try {
		return new PrintWriter(new BufferedWriter(new FileWriter(source, true)));
		}
		catch (IOException I) {
		D.error(I);
		return null;
		}
	}
	
	

}
