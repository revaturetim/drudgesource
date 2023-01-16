package drudge.spider;

import drudge.data.*;
import drudge.*;
import drudge.global.*;
import drudge.page.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class Spider {
protected boolean checkok = true;
protected long delay = 0L;

	//a convenience method for handling links and it makes other spiders do different things
	protected void links(final Page p) {
	p.getSource();
	Data<Page> pages = p.getLinks();
	DataEnum.links.data.put(pages);
	Debug.time("End Links");
	}
	
	//you can override this to create different delaying methods if you choose to do so
	protected void delay() {
		try {
		Thread.sleep(delay);
		}
		catch (InterruptedException I) {
		D.error(I.getClass(), I, "Location", "Spider.delay()");
		}
	}

	protected void redirect(final Page p) {
	final Page.Header h = p.header();
	final String redloc = h.getRedirectLocation();
		try {
		Page rediruri = new Page(redloc);
			try {
			DataEnum.links.data.put(rediruri);
			Debug.time("Redirected");
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
		}	
		catch (URISyntaxException U) {
		Print.row(U, redloc);
		}
		catch (InvalidURLException I) {
		I.printRow();
		}
		catch (MalformedURLException M) {
		Print.row(M, redloc);
		}
		catch (IOException I) {
		Print.row(I, redloc);
		}
	}

	public boolean crawl(Page p) {
	boolean remove = false;
	delay();//this will be universal for all crawlers since delay=0 is the same as no delay

		try {
			if (checkok) {
			p.isUseless();//this throws a uselesssurlexception
			Debug.time("Checing is UseLess");
			}
		links(p);
		}
		catch (RedirectedURLException R) {
		R.printRow();
		redirect(p);
		}
		//these must be caught here so it can remove it once it is found in data object
		catch (NotOKURLException N) {
		remove = true;
		N.printRow("", "", "", p.header().getResponse(), N.toString(), p.toString());	
		}
		catch (InvalidURLException I) {
		remove = true;
		I.printRow();
		}
		catch (NoContentURLException N) {
		N.printRow();
		}	
		catch (BadEncodingURLException B) {
		B.printRow();
		}
	return remove;
	}

	public void setCheckOK(boolean b) {
	checkok = b;
	}

	public void setDelay(long l) {
	delay = l;
	}

}
