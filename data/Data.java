package drudge.data;

import java.util.function.*;
import java.util.*;
import drudge.*;
import drudge.page.*;
import drudge.global.FileNames;
import java.net.*;
import java.io.*;

/*This interface has the methods that this program will use*/
public interface Data<T> extends Iterable<T>, RandomAccess {

	/*These are important methods because they interface with the rest of the program*/
	public boolean add(T obj);//this is just to raw add an item into it without checking exceptions
	public void put(T link) throws DuplicateURLException, ExcludedURLException, InvalidURLException, URISyntaxException;
	public T delete(int cycle);
	public T get(int cycle);
	public String source();
	public void setLevel(int l);
	public int level();
	public boolean excluded();
	public void setExcluded(boolean b);
	public boolean included();
	public void setIncluded(boolean b);
	//public boolean check();
	//public boolean checkError();
	public void begin() throws Exception;
	public void finish() throws Exception;

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
			catch (URISyntaxException U) {
			scs = false;
			Print.printRow(U, page);
			}
			catch (InvalidURLException I) {
			scs = false;
			I.printRow();
			}
			catch (DuplicateURLException Du) {
			scs = false;
			Du.printRow();
			}
			catch (ExcludedURLException E) {
			scs = false;
			E.printRow();
			}
		}
	return scs;
	}

	default public boolean put(T[] links) {
	boolean scs = true;
		for (T link : links) {
			try {
			put(link);
			}
			catch (URISyntaxException U) {
			scs = false;
			Print.printRow(U, link);
			}
			catch (InvalidURLException I) {
			scs = false;
			I.printRow();
			}
			catch (DuplicateURLException Du) {
			scs = false;
			Du.printRow();
			}
			catch (ExcludedURLException E) {
			scs = false;
			E.printRow();
			}
		}
	
	return scs;
	}

	default public Iterator<T> iterator() {
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
			
			public void remove() {
			delete(i);
			}
		};	
	return it;
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
	
	/*this is an internal check to check the data within the data object itself not its source*/
	default public boolean check() /*throws UselessURLException, IOException, URISyntaxException*/ {
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
	
	default public boolean checkError() {
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
		System.out.println("The number of lines in " + source() +  " is "  + linecount);
		}
		catch (IOException I) {
		System.out.println(I);
		}
	return (duplicate || linkerror);
	}
	


}
