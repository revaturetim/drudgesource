package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;
import drudge.Debug;
import drudge.page.*;
import drudge.global.*;

/*this class is a superclass of objects that use an array as the storage device*/
/*T has to be assumed to be an object so it retains is general behavior for all types*/
/*This is the default Data object for this program when not using database object*/
public class DataListExclude<T extends String> extends DataList<T>{

	public DataListExclude() {

	}

	public DataListExclude(String s) {
	super(s);
	}
	
	final public T remove(final int i) {
	//empty since it is going to be a read only object
	return null;
	}
	
	@SuppressWarnings("unchecked")	
	/*The default assumes that it is working with a page object*/
	public void begin() throws Exception {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		this.add((T)line);
		}
	}

	/*The default assumes you are working with a page object.  Subclasses should override*/
	public void end() throws Exception {
	//empty since it is going to be a read only object
	}
	
	public void truncate() {
	//empty since it is going to be a read only object
	}
	
	/*since these values can't be changed they alwasy return a default value*/
	final public int level() {
	return 1;
	}
	
	public boolean checkError() {
	D.writeBeginningResponse(source());
	boolean duplicate = false;
	boolean linkerror = false;
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
			for (String link1 = reader.readLine(); link1 != null; link1 = reader.readLine()) {
				/*this will check for decoding errors*/
				if (link1.contains("%")) {
				D.writeEncodingResponse(reader.getLineNumber());
				}
			/*This will check for duplicates in the data*/
				try {
				URL url = new URL(link1);//this throws malformedurlexception
				url.toURI();//throws urisyntaxexception
				Page p = new Page(url);//this throws ioexception
				p.isValid();//this throws InvalidURLException, URISyntaxException
					if (url.getAuthority() == null) {
					throw new MalformedURLException("No Host");
					}
				final LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					for (String link2 = reader2.readLine(); reader2.getLineNumber() < reader.getLineNumber(); link2 = reader2.readLine()) {
						if (link1.equals(link2)) {
						duplicate = true;
						D.writeDuplicateResponse(String.valueOf(reader.getLineNumber()), link1, link2);
						}
					}
				}
				catch (InvalidURLException I) {
				linkerror = true;
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, I.toString());
				}
				catch (URISyntaxException U) {
				linkerror = true;
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, U.toString());
				}
				catch (MalformedURLException M) {
				linkerror = true;
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, M.toString());
				}
				catch (IOException I) {
				linkerror = true;
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, I.toString());
				}
			}
		D.writeFinalResponse(reader.getLineNumber());
		}
		catch (FileNotFoundException F) {
		
		}
		catch (IOException I) {
		System.out.println(I);
		}
	return (duplicate || linkerror);
	}
}
