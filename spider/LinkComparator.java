package drudge.spider;

import java.util.*;
import java.net.*;
import drudge.page.Page;
import java.io.*;
import drudge.*;

public class LinkComparator<T extends Page> implements Comparator<T> {

	public LinkComparator() {
	}


	public int compare(Page p1, Page p2) {
	String link1 = p1.toString();
	String link2 = p2.toString();
	int num1 = link1.split("/").length;//count(link1, "/");
	int num2 = link2.split("/").length;//count(link2, "/");
	return num2 - num1;
	}

	private int count(String text, String c) {
	int howmany = 0;
		Debug.here(text);
		Debug.here(c);
		Debug.here(text.indexOf(c));
		for (int i = text.indexOf(c); i > -1; i = text.indexOf(c, i + 1)) {
		howmany++;
		}
	
	return howmany;
	}
}	

