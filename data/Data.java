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
	public void put(T link) throws DuplicateURLException, ExcludedURLException, InvalidURLException, URISyntaxException;
	public T remove(int cycle);
	public T get(int cycle);
	public void setSource(String s);
	public String source();
	public void setLevel(int l);
	public int level();
	public boolean excluded();
	public void setExcluded(boolean b);
	public boolean included();
	public void setIncluded(boolean b);
	public boolean robotAllowed();
	public void setRobotAllowed(boolean b);
	public boolean check();
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
			throw new UnsupportedOperationException("This Iterator is a read-only iterator");
			}
		};	
	return it;
	}

	

}
