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
protected boolean robotsallowed = true;
protected boolean included = false;
protected boolean excluded = false;
protected long delay = 0L;

	//a convenience method for handling links and it makes other spiders do different things
	protected void links(final Page p) {
	p.getSource();
	Data<Page> pages = p.getLinks();
	checkExcluded(pages);
	Debug.time("End Links");
	}
	
	//you can override this to create different delaying methods if you choose to do so
	protected void delay() {
		try {
		Thread.sleep(delay);
		}
		catch (InterruptedException I) {
		D.error(I);
		}
	}

	protected void redirect(final Page p) {
	final Page.Header h = p.header();
	final String redloc = h.getRedirectLocation();
		try {
		Page rediruri = new Page(redloc);
			try {
			DataObjects.dada.put(rediruri);
			Debug.time("Redirected");
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
		}	
		catch (URISyntaxException U) {
		Print.printRow(U, redloc);
		}
		catch (InvalidURLException I) {
		I.printRow();
		}
		catch (MalformedURLException M) {
		Print.printRow(M, redloc);
		}
		catch (IOException I) {
		Print.printRow(I, redloc);
		}
	}

	protected void checkExcluded(Data<Page> pages) {
		for (Page page : pages) {
			try {
			checkExcluded(page);
			DataObjects.dada.put(page);
			}
			catch (DuplicateURLException D) {
			D.printRow();	
			}
			catch (ExcludedURLException E) {
			E.printRow();
			}
		}
	}
	
	protected void checkExcluded(Page p) throws ExcludedURLException {

		if (included) {
		p.isIncluded();	
		}
		if (excluded) {
		p.isExcluded();
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
			if (!robotsallowed) {
			p.isRobotExcluded();//this throws RobotsExcludedURLException	
			Debug.time("Checking Robot Allowed");
			}
		checkExcluded(p);//throws excluded url eception
		links(p);
		}
		catch (RedirectedURLException R) {
		R.printRow();
			try {
			checkExcluded(p);//throws excluded url exception
			redirect(p);
			}
			catch (ExcludedURLException E) {
			E.printRow();
			}
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
		catch (RobotsExcludedURLException N) {
		N.printRow();
		}
		catch (ExcludedURLException E) {
		remove = true;
		E.printRow();
		}
	return remove;
	}

	public void setCheckOK(boolean b) {
	checkok = b;
	}

	public void setRobotsAllowed(boolean b) {
	robotsallowed = b;
	}

	public void setDelay(long l) {
	delay = l;
	}
	
	public void setExcluded(boolean b) {
	excluded = b;
	}

	public void setIncluded(boolean b) {
	included = b;
	}

	//I thought that calling issue was humorous like you have a issues
	protected void spinIssue(String i, Object o, Exception e) {
	HashMap<String, Object> h = new HashMap<String, Object>();
	h.put(i + " Issue", o);
	h.put("Exception", e);
	h.put("location", "Spider.spin(int)");
	D.error(h);		
	}

}
