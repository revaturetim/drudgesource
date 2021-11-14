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
		try {
		p.getSource();
		DataList<Page> pages = (DataList<Page>)p.getLinks();
		Collections.sort(pages, compare);//this sorts it the way it should be sorted for this spider	
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









