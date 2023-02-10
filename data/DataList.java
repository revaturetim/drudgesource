package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;
import drudge.Debug;
import drudge.page.*;
import drudge.global.*;
import drudge.spider.NaturalComparator;

/*this class is a superclass of objects that use an array as the storage device*/
/*T has to be assumed to be an object so it retains is general behavior for all types*/
/*This is the default Data object for this program when not using database object*/
public class DataList extends AbstractData {
private int size = 0;
private Object[] objs = new Object[10];

	public DataList() {

	}
	
	public DataList(String s) {
	this.source = s;
	}
	
	public DataList(String s, int i) {
	this(s);
	this.firstindex = i;
	}

	public void put(Object page) throws DuplicateURLException, IllegalArgumentException {
		if (page != null) {
			if (contains(page) == false) {
			this.add(page);
			}
			else {
			throw new DuplicateURLException(page);
			}
		}
		else {
		throw new IllegalArgumentException("Attempting to put a null object into data");
		}
	}
		
	/* Do not erase remove, get, set, or add(T) methods!!!!!!!!!!!!!!!!!!!*/
	public void add(final int i, final Object obj) {
		if (obj != null) {
			if (this.size >= objs.length - 1) {
			this.objs = Arrays.copyOf(objs, objs.length * 2);
			}
			for (int j = i + 1; j < this.size; j++) {
			this.objs[j] = this.objs[j - 1];
			}
		this.objs[i] = obj;
		size++;
		}
	}

	public Object remove(final int i) {
	Object toberemoved = this.objs[i];
		for (int j = i; j < this.size - 1; j++) {
		this.objs[j] = this.objs[j + 1];
		}
	objs[this.size - 1] = null;
	this.size--;//this has to be last in order to make it work right
	return toberemoved;
	}
	
	public Object get(int i) {	
	return this.objs[i];
	}

	public Object set(final int i, final Object newt) {
	Object oldt = this.get(i);
	objs[i] = newt;
	return oldt;
	}

	public int size() {
	return this.size;
	}
	
	/*The default assumes that it is working with a page object*/
	public void begin() throws Exception {
	D.readEntries(this);
	}

	/*The default assumes you are working with a page object.  Subclasses should override*/
	public void end() throws Exception {
	D.writeEntries(this);	
	}
	
	public void truncate() {
	D.flush(this.source());
	}

	public void checkError() {
	D.writeBeginningResponse(source());
		try { 
		final LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		int firstcolumnlength = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			final String[] columns = line.split(CountFile.sep);
			/*This will see if the total number of columns for each line is the same as the previous one*/
				if (reader.getLineNumber() == 1) firstcolumnlength = columns.length;
				if (columns.length != firstcolumnlength) D.writeColumnLengthResponse(reader.getLineNumber());
			/*this will check for decoding errors*/
			final String link1 = columns[0];
			final String dlink = D.decode(link1);
			final String alink = D.toASCII(link1);
				if (!link1.equals(dlink) || !link1.equals(alink)) D.writeEncodingResponse(reader.getLineNumber());
			/*This will check for duplicates in the data*/
				try {
				URL url = new URL(link1);//this throws malformedurlexception
				Page p = new Page(url);//this throws ioexception
				p.isValid();//this throws InvalidURLException, URISyntaxException
					if (url.getAuthority() == null) throw new MalformedURLException("No Host");
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					for (String line2 = reader2.readLine(); reader2.getLineNumber() < reader.getLineNumber(); line2 = reader2.readLine()) {
					String[] columns2 = line2.split(CountFile.sep);
					String link2 = columns2[0];
						if (link1.equals(link2)) D.writeDuplicateResponse(String.valueOf(reader.getLineNumber()), link1, link2);	
					}
				}
				catch (InvalidURLException I) {
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, I.toString());
				}
				catch (URISyntaxException U) {
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, U.toString());
				}
				catch (MalformedURLException M) {
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, M.toString());
				}
				catch (IOException I) {
				D.writeResponse(String.valueOf(reader.getLineNumber()), link1, I.toString());
				}
			}
		D.writeFinalResponse(reader.getLineNumber());
		}
		catch (FileNotFoundException F) {
		
		}
		catch (IOException I) {
		D.error(I);
		}
	}

}
