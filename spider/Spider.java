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
protected boolean norobots = false;
protected long delay = 0L;

	//a convenience method for handling links and it makes other spiders do different things
	protected void links(final Page p) {
		
		try {
		p.getSource();
		Data<Page> pages = p.getLinks();
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
			catch (URISyntaxException U) {
			Print.printRow(U, redloc);
			}
			catch (InvalidURLException I) {
			I.printRow();
			}
			catch (DuplicateURLException Du) {
			Du.printRow();
			}
			catch (ExcludedURLException E) {
			E.printRow();
			}
		}	
		catch (MalformedURLException M) {
		Print.printRow(M, redloc);
		}
		catch (IOException I) {
		Print.printRow(I, redloc);
		}
	}
	
	public boolean crawl(Page p) {
	boolean remove = false;
	delay();//this will be universal for all crawlers since delay=0 is the same as no delay

		try {
			if (checkok) {
			p.isUseless();
			Debug.time("Checing is UseLess");
			}
			if (norobots) {
			p.isRobotAllowed();//this throws norobotsallowedexception	
			Debug.time("Checking Robot Allowed");
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
		catch (NoRobotsURLException N) {
		N.printRow();
		}
		catch (NoContentURLException N) {
		N.printRow();
		}	
		catch (BadEncodingURLException B) {
		B.printRow();
		}
		catch (IOException I) {
		spinIssue("Found an IOException while checking if it is useless", p, I); 
		Print.printRow(I, p);
		}
	return remove;
	}

	public void setCheckOK(boolean b) {
	checkok = b;
	}

	public void setRobotsAllowed(boolean b) {
	norobots = !b;//since most variables that set this are norobotsallowed vs robots allowed the values must be opposite
	}

	public void setDelay(long l) {
	delay = l;
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
