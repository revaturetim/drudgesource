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
	p.source().fill();
	DataList pages = (DataList)p.getLinks();
	Collections.shuffle(pages, random);
	DataEnum.links.data.insert(pages);
	}

}
