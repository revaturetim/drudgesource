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
	public void setLevel(int l);
	public int level();
	public boolean check();
	public boolean checkError();
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
			catch (DuplicateURLException Du) {
			scs = false;
			Du.printRow();
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
