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
public class DataList<T> extends AbstractData<T> {
public final List<T> list = new List<T>();

	public DataList() {

	}

	public DataList(String s) {
	this();
	source = s;
	}

	private class List<T> extends AbstractList<T> implements Serializable {
	private int size = 0;
	private Object[] objs = new Object[10];
	
		List() {
		
		}
		
		List(int i) {
		objs = new Object[i];
		}
		
		public int size() {
		return size;	
		}
	
		public T set(final int i, final T obj) {
		T old = (T)objs[i];
		objs[i] = obj;
		return old;
		}
		
		/*public boolean contains(Object page) {
		boolean c = false;
			
			for (int i = size; i > 0; i--) {
			Object p = objs[i];
				if (page.equals(p)) {
				c = true;
				break;
				}
			}
		return c;
		}*/
		
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
		@SuppressWarnings("unchecked")	
		T entry = (T)objs[i];
		return entry;
		}
	
	
	}
	
	public boolean add(T obj) {
	return list.add(obj);
	}
	
	public T get(int i) {
	return list.get(i);
	}
	
	public T remove(int i) {
	return list.remove(i);
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
	public void end() throws Exception {
	final BufferedWriter link_writer = new BufferedWriter(new FileWriter(source()));
		for (T t : this.list) {
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
