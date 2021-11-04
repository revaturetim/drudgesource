package drudge.data;

import java.util.*;
import drudge.page.Page;
import drudge.Debug;
import drudge.spider.*;

public class DataSort<T> extends DataArray<T> implements Cloneable {
private Comparator<T> compare = new NaturalComparator<T>();

	public DataSort(T[] ts) {
	super(ts);
	}

	public DataSort(T[] ts, String s) {
	super(ts, s);
	}

	public DataSort(T[] t, String s, Comparator<T> c) {
	this(t, s);
	//D.checkEntry(c, "DataSort(T[], String, Comparator<T>)");//this checks to see compare is null
	Debug.check(c, null);
	compare = c;
	}

	/*this overrides contains in order to sort so it can find things faster*/
	public boolean contains(Object p) {
	List<T> search = (List<T>)this;
	Collections.sort(search, compare);
	@SuppressWarnings("unchecked")	
	int k = Collections.<T>binarySearch(search, (T)p, compare);
	return (k > -1) ? true : false;
	}

}
