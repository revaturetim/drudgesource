package drudge.spider;

import java.util.*;

public class NaturalComparator<T> implements Comparator<T> {

			
	public int compare(T p1, T p2) {
	String p1string = p1.toString();
	String p2string = p2.toString();
	return p1string.compareTo(p2string);
	}
}
