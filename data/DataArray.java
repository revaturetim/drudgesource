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
public class DataArray<T> extends AbstractList<T> implements Data<T> {
protected T[] pages;
private int size = 0;
private String source = null;

	public DataArray(T[] t) {
	pages = t;
	}

	public DataArray(T[] t, String s) {
	this(t);
	source = s;
	}
	
	public T get(int i) {
	Debug.check(i, 0, size(), "DataArray.get(int)");
	T entry = pages[i];//this throws indexoutofboundsexception if is greater than pages array size
	return entry;
	}

	public int size() {
	return size;	
	}

	public boolean contains(Object page) {
	boolean c = false;
		for (int i = size - 1; i > -1; i--) {
		T p = get(i);
			if (page.equals(p)) {
			c = true;
			break;
			}
		}
	return c;
	}

	public T remove(final int i) {
	Debug.check(i, 0, size(), "DataArray.remove(int)");
	T p = pages[i];
	//pages[i] = null;
	size--;//decreases size by 1
		for (int j = i; j < size(); j++) {
	 	pages[j] = pages[j + 1];
		Debug.check(pages[j], null);
		}
	return p;
	}

	//We are assuming that duplicates have been checked before this method was called
	public void add(final int i, final T page) {
	Debug.check(page, null);
	size++;//increases size by 1
		////this has to enlarge array when it when it gets to its next to the last element
		if (size() == pages.length) {
		pages = Arrays.<T>copyOf(pages, pages.length * 2);
		}
		for (int j = i; j < size(); j++) {
		pages[j + 1] = pages[j];//assigns all elements to the next slot position after j
		}
	pages[i] = page;
	}
	
	public T set(final int i, final T page) {
	Debug.check(page, null, "Attempting to replace element with null object. No! No! No!");
	Debug.check(i, 0, size(), "DataArray.replace(T, int)");//throws indexoutofboundsexception
	T pp = pages[i];//this is the previous page
	pages[i] = page;//this assigns the new value to this slot in the array
	return pp;
	}
	
	public void put(T link) throws DuplicateURLException {
		if (contains(link)) {
		throw new DuplicateURLException(link);
		}
		else {
		add(link);
		}
	}
	
	public boolean check() {
	return true;
	}
	
	public boolean checkError() {
	return true;
	}
	
	public String source() {
	return source;
	}

}
