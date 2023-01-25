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
	this.objs = new Object[i];
	}

	public void put(Object page) throws DuplicateURLException, IllegalArgumentException {
		if (page == null) {
		throw new IllegalArgumentException("Attempting to add a null object to " + this.getClass().getName());
		}
		else if (contains(page) == false) {
			
			if (this.size >= objs.length - 1) {
			this.objs = Arrays.copyOf(objs, objs.length * 2);
			}
			if (this.objs[size] == null) {	
			this.objs[this.size] = page;
			this.size++;//increases size by 1
			}
			else {
			throw new IllegalStateException("Size variable was not set correctly.  It is erasing previous data.");
			}	
		}
		else {
		throw new DuplicateURLException(page);
		}
	}
		
	/* Do not erase remove, get, set, or add(T) methods!!!!!!!!!!!!!!!!!!!*/
	/* Let add(int, T) throw unsupportedoperationexception */
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

	public boolean add(Object p) {
	boolean added = false;
		if (p != null) {
			try {
			this.put(p);
			added = true;
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
		}
	return added;
	}
	
	public int size() {
	return this.size;
	}
	
	/*The default assumes that it is working with a page object*/
	public void begin() throws IOException, Exception {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));//this throws IOException and breaks the method if it can't read from soruce
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		String[] alink = line.split(CountFile.sep);
		Page page = PageFactory.createFromString(alink[0]);
		this.add(page);
		}
	}

	/*The default assumes you are working with a page object.  Subclasses should override*/
	public void end() throws IOException, Exception {
	final BufferedWriter link_writer = new BufferedWriter(new FileWriter(source()));
		for (Object t : this) {
		Page page = (Page)t;
		D.writeEntry(page, link_writer, level());
		}
	link_writer.close();
	}
	
	public void truncate() {
	File linkfile = new File(source());
		try {
		linkfile.delete();
		linkfile.createNewFile();
		}
		catch (IOException I) {
		D.error(I);
		}
	}
	
	public boolean checkError() {
	D.writeBeginningResponse(source());
	boolean duplicate = false;
	boolean linkerror = false;
		try { 
		final LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		int firstcolumnlength = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			String[] columns = line.split(CountFile.sep);
			/*This will see if the total number of columns for each line is the same as the previous one*/
				if (reader.getLineNumber() == 1) {
				firstcolumnlength = columns.length;
				}
				if (columns.length != firstcolumnlength) {
				D.writeColumnLengthResponse(reader.getLineNumber());
				}
			/*this will check for decoding errors*/
			String link1 = columns[0];
				if (link1.contains("%")) {
				D.writeEncodingResponse(reader.getLineNumber());
				}
				
			/*This will check for duplicates in the data*/
				try {
				URL url = new URL(link1);//this throws malformedurlexception
				Page p = new Page(url);//this throws ioexception
				p.isValid();//this throws InvalidURLException, URISyntaxException
					if (url.getAuthority() == null) {
					throw new MalformedURLException("No Host");
					}
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					for (String line2 = reader2.readLine(); reader2.getLineNumber() < reader.getLineNumber(); line2 = reader2.readLine()) {
					String[] columns2 = line2.split(CountFile.sep);
					String link2 = columns2[0];
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
		D.error(I);
		}
	return (duplicate || linkerror);
	}

}
