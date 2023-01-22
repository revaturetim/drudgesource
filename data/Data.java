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
	public T remove(int cycle);
	public void put(T page) throws DuplicateURLException, IllegalArgumentException;
	public T get(int cycle);
	public void setSource(String s);
	public String source();
	public void setLevel(int l);
	public int level();
	public boolean checkError();
	public void begin() throws Exception;
	public void end() throws Exception;
	public void truncate();
	public int size();
	public boolean contains(Object link);
	public Iterator<T> iterator();

	default public void put(Data<T> links) {
		for (T link : links) {
			try {
			put(link);
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
			catch (IllegalArgumentException I) {
			D.error(I);
			}
		}
	}



	default public boolean add(T page) {
	boolean added = false;
		if (page != null) {
			try {
			this.put(page);
			added = true;
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
		}
	return false;
	}

}
