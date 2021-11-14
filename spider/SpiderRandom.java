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
		try {
		p.getSource();
		DataList<Page> pages = (DataList<Page>)p.getLinks();
		Collections.shuffle(pages, random);
		DataObjects.dada.put(pages);
		}
		catch (SocketTimeoutException S) {
		spinIssue("Found a Socket Timeout while getting source", p.getURL(), S);
		Print.printRow(S, p);
		}
		catch (IOException I) {
		spinIssue("Found and Input/Output while getting source", p.getURL(), I);
		Print.printRow(I, p);
		}
	Debug.endCycleTime("End Links");
	}

}
