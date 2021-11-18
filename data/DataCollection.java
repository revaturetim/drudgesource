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
public class DataCollection<T> extends AbstractCollection<T> implements Data<T> {
private int size = 0;
private String source = null;
private Object[] objs = new Object[10];
private int datalevel = 1;
private boolean exclude = false;
private boolean include = false;

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
		
		for (int i = size; i > 0; i--) {
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
	
	public T delete(final int i) {
	Debug.between("DataArray.remove(int)", i, 0, size);
	T toberemoved = (T)objs[i];
		for (int j = i; j < size - 1; j++) {
		objs[j] = objs[j + 1];
		}
	objs[size - 1] = null;
	size--;//this has to be last in order to make it work right
	return toberemoved;
	}
	
	public void put(T link) throws DuplicateURLException, ExcludedURLException, InvalidURLException, URISyntaxException {
	((Page)link).isValid();
		if (excluded() == true && DataObjects.exclude.contains(link)) throw new ExcludedURLException(link);
		if (included() == true) {
			for (Page p : DataObjects.include) {
			final String host1 = p.getURL().getHost();
			final String host2 = ((Page)link).getURL().getHost();
				if (host1.equals(host2) == false) {
				throw new ExcludedURLException(link);
				}
			}		
		} 
		if (add(link) == false) throw new DuplicateURLException(link);	
	}

	public T get(int i) {
	Debug.between("DataArray.get(int)", i, 0, size);//throws indexoutofboundsexception if it is outside of the exceptable range
	@SuppressWarnings("unchecked")	
	T entry = (T)objs[i];
	return entry;
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
	public void finish() throws Exception {
	BufferedWriter link_writer = new BufferedWriter(new FileWriter(source()));
		for (T t : this) {
		Debug.check(t, null);
		Page tp = (Page)t;
			for (int r = 0; r < level(); r++) {
				if (r == 0) {
				link_writer.append(tp.toString() + CountFile.sep);
				}
				else if (r == 1) {
				link_writer.append(tp.getTitle() + CountFile.sep);
				}
				else if (r == 2) {
				link_writer.append(tp.getKeywords().rawString());
				}
			}
		link_writer.append("\n");
		}
	link_writer.close();
	}

	public String source() {
	return source;
	}

	public void setLevel(int l) {
	datalevel = l;
	}

	public int level() {
	return datalevel;
	}
	
	public void setExcluded(boolean b) {
	exclude = b;
	}
	
	public boolean excluded() {
	return exclude;
	}
	
	public void setIncluded(boolean b) {
	include = b;
	}
	
	public boolean included() {
	return include;
	}
	
}
