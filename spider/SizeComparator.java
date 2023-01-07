package drudge.data;

import java.util.*;
import java.net.*;
import drudge.page.Page;
import java.io.*;

public class SizeComparator<T extends Page> implements Comparator<T> {

	public int compare(T p1, T p2) {
	p1.getSource();
	p2.getSource();
	p1.getLinks();
	p2.getLinks();
	int c1 = p1.getLinkCount();
	int c2 = p1.getLinkCount();
	return c1 - c2;
	}
}	

