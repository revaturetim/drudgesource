package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;
import drudge.Debug;
import drudge.page.*;
import drudge.global.FileNames;

/*this class is a superclass of objects that use an array as the storage device*/
/*T has to be assumed to be an object so it retains is general behavior for all types*/
/*This is the default Data object for this program when not using database object*/
public class DataCollection<T> extends AbstractCollection<T> implements Data<T> {
private int size = 0;
private String source = null;
private Object[] objs = new Object[10];

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
		for (int i = size - 1; i > -1; i--) {
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
			boolean next = false;
				if (i < size - 1) {
				next = true;
				}	
			return next;	
			}

			public T next() {
			i++;//increment first!
			return (T)objs[i];
			}

			/*This is the remove method that gets used and it has to be independent of every other remove function*/
			public void remove() {
				if (i == size - 1) {
				objs[i] = null;
				}
				else {
					for (int j = i; j < size; j++) {
					objs[j] = objs[j + 1];
					}
				}
			size--;//this has to be last in order to make it work right
			}
		};	
	return it;
	}

	public boolean add(T page) {
	D.checkEntry(page, "DataArray.add(T, int)");//throws nullpointerexception
	boolean added = false;
		if (contains(page) == false) {
		size++;//increases size by 1
			if (size >= objs.length) {
			objs = Arrays.copyOf(objs, objs.length * 2);
			}
			if (objs[size - 1] == null) {	
			objs[size - 1] = page;
			added = true;
			}
			else {
			throw new IllegalArgumentException("Size variable was not set correctly.  It is erasing previous data.");
			}
		}
	return added;
	}
	
	public T remove(final int r) {
	D.checkLineEntry(r, size, "DataArray.remove(int)");
	T pagereturn = get(r);
	remove(pagereturn);
	return pagereturn;
	}

	public void put(T link) throws DuplicateURLException {
		if (add(link) == false) {
		throw new DuplicateURLException(link);
		}
	}

	public T get(int i) {
	D.checkLineEntry(i, size, "DataArray.get(int)");//this throws indexoutofboundsexception if it is outside of the exceptable range
	T entry = (T)objs[i];
	return entry;
	}

	public String source() {
	return source;
	}

}
