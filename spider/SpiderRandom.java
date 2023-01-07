package drudge.spider;

import java.io.*;
import java.util.*;
import java.net.*;
import drudge.*;
import drudge.page.*;
import drudge.data.*;
import drudge.global.*;

public class SpiderRandom extends Spider {
private Random random = new Random();

	public SpiderRandom(int s) {
	random = new Random(s);
	}

	protected void links(Page p) {
	p.getSource();
	DataList<Page> pages = (DataList<Page>)p.getLinks();
	Collections.shuffle(pages, random);
	DataObjects.dada.put(pages);
	Debug.time("End Links");
	}

}
