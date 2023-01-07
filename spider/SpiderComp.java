package drudge.spider;

import java.io.*;
import java.util.*;
import java.net.*;
import drudge.data.*;
import drudge.*;
import drudge.page.*;
import drudge.global.*;

public class SpiderComp extends Spider {
private Page top;
private Comparator<Page> compare = new TopComparator<Page>();

	public SpiderComp(Comparator<Page> co) {
	compare = co;
	}

	protected void links(Page p) {
	p.getSource();
	DataList<Page> pages = (DataList<Page>)p.getLinks();
	Collections.sort(pages, compare);//this sorts it the way it should be sorted for this spider	
	DataObjects.dada.put(pages);
	Debug.time("End Links");
	}

}









