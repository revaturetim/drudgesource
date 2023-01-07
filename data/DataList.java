package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;
import drudge.Debug;
import drudge.page.*;
import drudge.global.*;
import drudge.spider.NaturalComparator;

/*this class is a superclass of objects that use an array as the storage device*/
/*T has to be assumed to be an object so it retains is general behavior for all types*/
/*This is the default Data object for this program when not using database object*/
public class DataList<T> extends AbstractData<T> {
private int size = 0;
private Object[] objs = new Object[10];

	public DataList() {

	}

	public DataList(String s) {
	this();
	source = s;
	}
	
	public DataList(String s, int i) {
	this(s);
	objs = new Object[i];
	}

	public void put(T page) throws DuplicateURLException, IllegalArgumentException {
		
		if (contains(page) == false) {
			
			if (size >= objs.length - 1) {
			objs = Arrays.copyOf(objs, objs.length * 2);
			}
			if (objs[size] == null) {	
			objs[size] = page;
			size++;//increases size by 1
			}
			else {
			throw new IllegalStateException("Size variable was not set correctly.  It is erasing previous data.");
			}	
		}
		else {
		throw new DuplicateURLException(page);
		}
	}
		
	/*public boolean contains(Object page) {
	boolean has = false; 
	List<T> newlist = new DataList<T>();
	
	Collections.copy(newlist, this);
	//Arrays.sort(NewObjects, new NaturalComparator());
		try {	
		int f = Collections.binarySearch(newlist, (Page)page, new NaturalComparator());
		if (f > -1) has = true;
		}
		catch (Exception E) {
		System.out.println(E);
		}
	return has;
	}*/
	 
	public T remove(final int i) {
	T toberemoved = (T)objs[i];
		for (int j = i; j < size - 1; j++) {
		objs[j] = objs[j + 1];
		}
	objs[size - 1] = null;
	size--;//this has to be last in order to make it work right
	return toberemoved;
	}
	
	public T get(int i) {	
	T entry = (T)objs[i];
	return entry;
	}
	
	public int size() {
	return size;
	}
	
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
		for (T t : this) {
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
	
	public boolean checkError() {
	return D.checkErrorFile(source());
	}
	
	
}
