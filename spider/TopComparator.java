package drudge.spider;

import java.util.*;
import java.net.*;
import drudge.page.Page;

public class TopComparator implements Comparator {



	public int compare(Object p1, Object p2) {
	URL purl = ((Page)p1).getURL();
	String domain1 = purl.getAuthority();
	URL purl2 = ((Page)p2).getURL();
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

