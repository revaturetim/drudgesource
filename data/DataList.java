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
public class DataList<T> extends AbstractData<T> {
public final List<T> list = new List<T>();

	public DataList() {

	}

	public DataList(String s) {
	this();
	source = s;
	}

	private class List<T> extends AbstractList<T> {
	private int size = 0;
	private Object[] objs = new Object[10];
	
		List() {
		
		}
		
		List(int i) {
		objs = new Object[i];
		}
		
		public int size() {
		return size;	
		}
	
		public T set(final int i, final T obj) {
		T old = (T)objs[i];
		objs[i] = obj;
		return old;
		}
		
		/*public boolean contains(Object page) {
		boolean c = false;
			
			for (int i = size; i > 0; i--) {
			Object p = objs[i];
				if (page.equals(p)) {
				c = true;
				break;
				}
			}
		return c;
		}*/
		
		public boolean add(T page) {
		Debug.check(page, null);
		boolean added = false;
		
			if (contains(page) == false) {
			
				if (size >= objs.length - 1) {
				objs = Arrays.copyOf(objs, objs.length * 2);
				}
				if (objs[size] == null) {	
				objs[size] = page;
				added = true;
				size++;//increases size by 1
				}
				else {
				throw new IllegalArgumentException("Size variable was not set correctly.  It is erasing previous data.");
				}
			
			}
		return added;
		}
		
		public T remove(final int i) {
		Debug.between("DataArray.remove(int)", i, 0, size);
		T toberemoved = (T)objs[i];
			for (int j = i; j < size - 1; j++) {
			objs[j] = objs[j + 1];
			}
		objs[size - 1] = null;
		size--;//this has to be last in order to make it work right
		return toberemoved;
		}
	
		public T get(int i) {
		Debug.between("DataArray.get(int)", i, 0, size);//throws indexoutofboundsexception if it is outside of the 	exceptable range
		@SuppressWarnings("unchecked")	
		T entry = (T)objs[i];
		return entry;
		}
	
	
	}
	
	public boolean add(T obj) {
	return list.add(obj);
	}
	
	public T get(int i) {
	return list.get(i);
	}
	
	public T remove(int i) {
	return list.remove(i);
	}
	
	
	public void put(T link) throws 
	DuplicateURLException, ExcludedURLException, InvalidURLException, URISyntaxException {
	final Page page = (Page)link;
	page.isValid();//throws invalidurlexception, urisyntaxexception
		if (excluded() == true) {
		page.isExcluded();//throws ExcludedURLException
		}
		if (included() == true) {
		page.isIncluded();//throws ExcludedURLException
		} 
		if (add(link) == false) {
		throw new DuplicateURLException(link);//throws DuplicateURLException
		}	
	}
	
	@SuppressWarnings("unchecked")	
	/*The default assumes that it is working with a page object*/
	public void begin() throws Exception {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		String[] ns = line.split(CountFile.sep);
			try {
			T p = (T)new Page(ns[0]);
			add(p);
			}
			catch (MalformedURLException M) {
			D.error(M);
			}
		}
	}

	/*The default assumes you are working with a page object.  Subclasses should override*/
	public void end() throws Exception {
	final BufferedWriter link_writer = new BufferedWriter(new FileWriter(source()));
		for (T t : this.list) {
		Debug.check(t, null);
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
	
	/*this is an internal check to check the data within the data object itself not its source*/
	public boolean check() /*throws UselessURLException, IOException, URISyntaxException*/ {
	Iterator<T> pages = this.list.iterator();

		for (int linecount = 1; pages.hasNext(); linecount++) {
		T page = pages.next();
			try {
			String link = pages.toString();
			Page p = new Page(link);//this throws InvalidURLException, malformedurlexception, URISyntaxException
			URL u = p.getURL();
				if (u.getAuthority() == null) {
			
				}
				Iterator<T> pages2 = this.list.iterator();
				for (int linecount2 = 1; pages2.hasNext(); linecount2++) {
				T page2 = pages2.next();
				String link2 = page2.toString();
					if (link.equals(link2) && linecount != linecount2) {
					String msg =  "[" 
					+ link + "] Duplicate entry found at " 
					+ String.valueOf(linecount) + " and " 
					+ 			String.valueOf(linecount2);
					}
				}
			}
			catch (Exception E) {
			
			}
		}
	return true;
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
			String link = columns[0];
				if (link.contains("%")) {
				System.out.println("...Possible encoding error at line " + String.valueOf(linecount));
				}
				
			/*This will check for duplicates in the data*/
				try {
				Page p = new Page(link);//this throws malformedurlexception
				p.isValid();//this throws InvalidURLException, URISyntaxException
				URL u = p.getURL();
					if (u.getAuthority() == null) {
					throw new MalformedURLException("No Host");
					}
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
					for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine()) {
					int linecount2 = reader2.getLineNumber();
						if (linecount2 < linecount) {
						String[] columns2 = line2.split(CountFile.sep);
						String link2 = columns2[0];
							if (link.equals(link2)) {
							duplicate = true;
							System.out.print("..." + link + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) 
					       	+ " and " 
					       	+ String.valueOf(linecount2));
							}
						}
					}
				}
				catch (InvalidURLException N) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't an html file: ");
				System.out.println(link);
				}
				catch (URISyntaxException U) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
				catch (MalformedURLException M) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
			}
		System.out.println("...The number of lines in " + source() +  " is "  + linecount + ".");
		}
		catch (IOException I) {
		D.error(I);
		}
	return (duplicate || linkerror);
	}
	
	
}
