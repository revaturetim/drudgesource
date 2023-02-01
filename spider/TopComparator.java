package drudge.spider;

import java.util.*;
import java.net.*;
import drudge.page.Page;

public class TopComparator<T extends Page> implements Comparator<T> {



	public int compare(T p1, T p2) {
	URL purl = p1.url();
	String domain1 = purl.getAuthority();
	URL purl2 = p2.url();
	String domain2 = purl2.getAuthority();
	int rvalue = 0;
		if (domain1.equals(domain2)) {
		rvalue = 1;
		}
		else {
		rvalue = 0;
		}
	return rvalue;
	}
}	

