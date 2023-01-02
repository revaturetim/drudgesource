package drudge.data;

import java.util.function.*;
import java.util.*;
import java.net.*;
import java.io.*;
import drudge.*;
import drudge.page.*;
import drudge.global.*;


/*This interface has the methods that this program will use*/
public interface Data<T> extends Iterable<T>, RandomAccess {

	/*These are important methods because they interface with the rest of the program*/
	public boolean add(T obj);//this is just to raw add an item into it without checking exceptions
	public T remove(int cycle);
	public T get(int cycle);
	public void setSource(String s);
	public String source();
	public void setLevel(int l);
	public int level();
	public boolean checkError();
	public void begin() throws Exception;
	public void end() throws Exception;
	public void truncate();
	
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

	default public void put(T link) throws DuplicateURLException, IllegalArgumentException {
		if (link == null) {
		throw new IllegalArgumentException("Attempting to add a null object to " + this.getClass().getName());
		}
		if (add(link) == false) {
		throw new DuplicateURLException(link);//throws DuplicateURLException
		}	
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
			catch (IllegalArgumentException I) {
			D.error(I);
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
			catch (DuplicateURLException Du) {
			scs = false;
			Du.printRow();
			}
			catch (IllegalArgumentException I) {
			D.error(I);
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
			throw new UnsupportedOperationException("This Iterator is a read-only iterator");
			}
		};	
	return it;
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

	

}
