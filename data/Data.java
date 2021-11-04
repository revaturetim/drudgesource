package drudge.data;

import java.util.function.*;
import java.util.*;
import drudge.Debug;
import drudge.page.*;
import drudge.global.FileNames;
import java.net.*;
import java.io.*;

/*This interface has the methods that this program will use*/
public interface Data<T> extends Iterable<T>, RandomAccess {

	/*These are important methods because they interface with the rest of the program*/
	public void put(T link) throws DuplicateURLException;
	public T remove(int cycle);
	public T get(int cycle);
	public String source();

	default public int size() {
	int size = 0;
		for (T p : this) {
		size++;
		}	
	return size;
	}

	/*this is a simple contains method that should be overridden by most Data objects*/
	default public boolean contains(Object link) {
	boolean has = false;
		for (Object page : this) {
			if (page.equals(link)) {
			has = true;
			break;
			}
		}
	return has;
	}

	default public boolean put(Data<T> d) {
	boolean scs = true;

		for (T page : d) {
			try {
			put(page);
			}
			catch (DuplicateURLException Du) {
			scs = false;
			Du.printRow();
			}
		}
	return scs;
	}

	default public boolean put(T[] links) {
	boolean scs = false;
		for (T link : links) {
			try {
			put(link);
			}
			catch (DuplicateURLException Du) {
			scs = false;
			Du.printRow();
			}
		}
	
	return scs;
	}

	default public void put(T link, Data<T> excludes, boolean exc) throws DuplicateURLException, ExcludedURLException {
		
		if (exc) {
			if (excludes.contains(link) == exc) {
			throw new ExcludedURLException(link);
			}
			else {
			put(link);
			}			
		}
	}

	default public void put(Data<T> links, Data<T> excludes, boolean exc) {
	
			for (T link : links) {
				try {
				put(link, excludes, exc);
				}
				catch (DuplicateURLException Du) {
				Du.printRow();
				}
				catch (ExcludedURLException E) {
				E.printRow();
				}
			}

	}	

	default public void put(T[] links, Data<T> excludes, boolean exc) {
			for (T link : links) {
				try {
				put(link, excludes, exc);
				}
				catch (DuplicateURLException Du) {
				Du.printRow();
				}
				catch (ExcludedURLException E) {
				E.printRow();
				}
			}
	}

	/*this is an internal check to check the data within the data object itself not its source*/
	default boolean check() throws UselessURLException, IOException, URISyntaxException {
	Iterator<T> pages = this.iterator();

		for (int linecount = 1; pages.hasNext(); linecount++) {
		T page = pages.next();
		String link = pages.toString();
		Page p = new Page(link);//this throws nothtmlurlexception, malformedurlexception, URISyntaxException
		URL u = p.getURL();
			if (u.getAuthority() == null) {
			throw new MalformedURLException("No Host");
			}
			Iterator<T> pages2 = this.iterator();
			for (int linecount2 = 1; pages2.hasNext(); linecount2++) {
			T page2 = pages2.next();
			String link2 = page2.toString();
				if (link.equals(link2) && linecount != linecount2) {
	String msg =  "[" + link + "] Duplicate entry found at " + String.valueOf(linecount) + " and " + String.valueOf(linecount2);
				throw new DuplicateURLException(msg);
				}
			}
		}
	return true;
	}
	
	default boolean checkError() throws IOException {
	boolean duplicate = false;
	boolean linkerror = false;
	int linecount = 0;
	final String source = source();

	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source)));
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
			Page p = new Page(link);//this throws nothtmlurlexception, malformedurlexception, URISyntaxException
			URL u = p.getURL();
				if (u.getAuthority() == null) {
				throw new MalformedURLException("No Host");
				}
			LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source)));
				for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine()) {
				int linecount2 = reader2.getLineNumber();
					if (linecount2 < linecount) {
					String[] columns2 = line2.split(CountFile.sep);
					String link2 = columns2[0];
						if (link.equals(link2)) {
						duplicate = true;
						System.out.print(link + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) + " and " + String.valueOf(linecount2));
						}
					}
				}
			}
			catch (NotHTMLURLException N) {
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
	return (duplicate || linkerror);
	}
	
	default Iterator<T> iterator() {
		Iterator<T> it = new Iterator<T>() {
		int i = -1;
			public boolean hasNext() {
			boolean next =  (get(i + 1) != null); 
			return next;	
			}

			public T next() {
			i++;//increment first!
			return get(i);
			}
		};	
	return it;
	}
		
	@SuppressWarnings("unchecked")	
	/*The default assumes that it is working with a page object*/
	default void begin() throws Exception {
	LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		String[] ns = line.split(CountFile.sep);
			try {
			T p = (T)new Page(ns[0]);
				try {
				put(p);
				}
				catch (DuplicateURLException Du) {
				D.error(Du);
				}
			}
			catch (URISyntaxException U) {
			D.error(U);
			}
			catch (NotHTMLURLException N) {
			D.error(N);
			}
			catch (MalformedURLException M) {
			D.error(M);
			}
			catch (IOException I) {
			D.error(I);
			}
		}
	}

	/*The default assumes you are working with a page object.  Subclasses should overrid*/
	default void finish() throws Exception {
	BufferedWriter link_writer = new BufferedWriter(new FileWriter(source()));
		for (T t : this) {
		Debug.check(t, null);
		Page tp = (Page)t;
		Data<String> row = tp.getRow();
			for (String s : row) {
			link_writer.append(s);
			link_writer.append(CountFile.sep);
			}		
		link_writer.append("\n");
		}
	link_writer.close();
	}

	default public String rawString() {
	StringBuilder builder = new StringBuilder();
		for (T t : this) {
		builder.append(t.toString());
		builder.append(" ");
		}
	return builder.toString();
	}

	default void truncate() {
	File linkfile = new File(source());
		try {
		linkfile.delete();
		linkfile.createNewFile();
		}
		catch (IOException I) {
		D.error(I);
		}
	}


}
