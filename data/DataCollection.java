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
public class DataCollection<T> extends AbstractCollection<T> implements Data<T> {
private int size = 0;
private String source = null;
private Object[] objs = new Object[10];
private int datalevel = 1;
private boolean exclude = false;
private boolean include = false;

	public DataCollection() {

	}

	public DataCollection(String s) {
	this();
	source = s;
	}

	public DataCollection(String s, int b) {
	this(s);
	objs = new Object[b];
	}

	public int size() {
	return size;	
	}
	
	public boolean contains(Object page) {
	boolean c = false;
		
		for (int i = size; i > 0; i--) {
		Object p = objs[i];
			if (page.equals(p)) {
			c = true;
			break;
			}
		}
	return c;
	}

	public Iterator<T> iterator() {
		Iterator<T> it = new Iterator<T>() {
		private int i = -1;
			public boolean hasNext() {
			return (i < size - 1);	
			}
	
			public T next() {
			i++;//increment first!
			return get(i);
			}

			/*This is the remove method that gets used and it has to be independent of every other remove function*/
			public void remove() {
				for (int j = i; j < size - 1; j++) {
				objs[j] = objs[j + 1];
				}
			objs[size - 1] = null;
			size--;//this has to be last in order to make it work right
			}
		};	
	return it;
	}

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
	
	public T remove(final int r) {
	Debug.between(r, 0, size, "DataArray.remove(int)");
	T pagereturn = get(r);
	remove(pagereturn);
	return pagereturn;
	}
	
	public void put(T link) throws DuplicateURLException, ExcludedURLException, InvalidURLException, URISyntaxException {
	((Page)link).isValid();
		if (excluded() == true && DataObjects.exclude.contains(link)) throw new ExcludedURLException(link);
		if (included() == true) {
			for (Page p : DataObjects.include) {
			final String host1 = p.getURL().getHost();
			final String host2 = ((Page)link).getURL().getHost();
				if (host1.equals(host2) == false) {
				throw new ExcludedURLException(link);
				}
			}		
		} 
		if (add(link) == false) throw new DuplicateURLException(link);	
	}

	public T get(int i) {
	Debug.between(i, 0, size, "DataArray.get(int)");//throws indexoutofboundsexception if it is outside of the exceptable range
	@SuppressWarnings("unchecked")	
	T entry = (T)objs[i];
	return entry;
	}
	
	/*this is an internal check to check the data within the data object itself not its source*/
	public boolean check() /*throws UselessURLException, IOException, URISyntaxException*/ {
	Iterator<T> pages = this.iterator();

		for (int linecount = 1; pages.hasNext(); linecount++) {
		T page = pages.next();
			try {
			String link = pages.toString();
			Page p = new Page(link);//this throws InvalidURLException, malformedurlexception, URISyntaxException
			URL u = p.getURL();
				if (u.getAuthority() == null) {
			
				}
				Iterator<T> pages2 = this.iterator();
				for (int linecount2 = 1; pages2.hasNext(); linecount2++) {
				T page2 = pages2.next();
				String link2 = page2.toString();
					if (link.equals(link2) && linecount != linecount2) {
		String msg =  "[" + link + "] Duplicate entry found at " + String.valueOf(linecount) + " and " + 			String.valueOf(linecount2);
					}
				}
			}
			catch (Exception E) {
			
			}
		}
	return true;
	}
	
	public boolean checkError() {
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
				System.out.println("There is a column length error at line " + String.valueOf(linecount));
				}
			/*this will check for decoding errors*/
				for (String col : columns) {
					if (col.contains("%")) {
					System.out.println("Possible encoding error at line " + String.valueOf(linecount));
					}
				}
			/*This will check for duplicates in the data*/
			String link = columns[0];
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
							System.out.print(link + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) 
					       	+ " and " 
					       	+ String.valueOf(linecount2));
							}
						}
					}
				}
				catch (InvalidURLException N) {
				linkerror = true;
				System.out.print("Line " + String.valueOf(linecount) + " has a link that isn't an html file: ");
				System.out.println(link);
				}
				catch (URISyntaxException U) {
				linkerror = true;
				System.out.print("Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
				catch (MalformedURLException M) {
				linkerror = true;
				System.out.print("Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
			}
		System.out.println("The number of lines in " + source +  " is "  + linecount);
		}
		catch (IOException I) {
		System.out.println(I);
		}
	return (duplicate || linkerror);
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
	public void finish() throws Exception {
	BufferedWriter link_writer = new BufferedWriter(new FileWriter(source()));
		for (T t : this) {
		Debug.check(t, null);
		Page tp = (Page)t;
			for (int r = 0; r < level(); r++) {
				if (r == 0) {
				link_writer.append(tp.toString() + CountFile.sep);
				}
				else if (r == 1) {
				link_writer.append(tp.getTitle() + CountFile.sep);
				}
				else if (r == 2) {
				link_writer.append(tp.getKeywords().rawString());
				}
			}
		link_writer.append("\n");
		}
	link_writer.close();
	}

	public String source() {
	return source;
	}

	public void setLevel(int l) {
	datalevel = l;
	}

	public int level() {
	return datalevel;
	}
	
	public void setExcluded(boolean b) {
	exclude = b;
	}
	
	public boolean excluded() {
	return exclude;
	}
	
	public void setIncluded(boolean b) {
	include = b;
	}
	
	public boolean included() {
	return include;
	}
	
}
