package drudge.data;

import java.util.*;
import java.net.*;
import drudge.page.Page;
import java.io.*;
import drudge.*;

public class RandomComparator<T extends Page> implements Comparator<T> {
private Random random = new Random();

	public RandomComparator() {
	random = new Random(System.currentTimeMillis());
	}

	public RandomComparator(long s) {
	random = new Random(s);
	}

	public int compare(T p1, T p2) {
	int p = random.nextInt(3);
		if (p == 2) {
		p = -1;
		}
	return p;
	}
}	

