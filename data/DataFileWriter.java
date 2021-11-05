package drudge.data;

import java.io.*;
import java.util.*;
import java.net.*;
import drudge.global.FileNames;
/*This class is for the data storage of the links it finds*/

public class DataFileWriter<T> extends DataWriter<T> {
private String file = FileNames.links;

	public DataFileWriter() {
	READER = createReader();
	PRINTER = createWriter();
	}

	public DataFileWriter(String f) {
	file = f;
	READER = createReader();
	PRINTER = createWriter();
	}

	protected LineNumberReader createReader() {
	LineNumberReader reader = null;
		try {
		reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
		reader.setLineNumber(0);
		reader.mark(0);
		}
		catch (IOException I) {
		D.error(I);
		}
	return reader;
	}
	
	protected PrintWriter createWriter() {
		try {
		return new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		}
		catch (IOException I) {
		D.error(I);
		return null;
		}
	}
	
	public boolean check() {
	return true;
	}
	
	public boolean checkError() {
	return true;
	}
	
	public String source() {
	return file;
	}
}
