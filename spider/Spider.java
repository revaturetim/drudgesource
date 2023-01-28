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
protected boolean norobotsallowed = false;
protected boolean skip = false;

	//a convenience method for handling links and it makes other spiders do different things
	protected void links(final Page p) {
	p.source().fill();
	Data pages = p.getLinks();
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
	final Page redirect = PageFactory.create(h.redirectlocation);
	DataEnum.links.data.add(redirect);
	}

	public int crawl(final int max) {
		for (Page p = (Page)DataEnum.links.data.get(Print.cycle - 1); p != null && Print.cycle < max; p = (Page)DataEnum.links.data.get(Print.cycle - 1)) {
			boolean notok = this.crawl(p);
			/* This has the same effect as skipping since when -nok option is on it 
			 * sees all urls as acceptables since the default return value in crawls
			 * is false.
			 */
			if (notok == true) {
			DataEnum.links.data.remove(Print.cycle - 1);
			}
			else {
			Print.row(p);
			}
		}
	return Print.cycle;//Print.cycle is incremented inside of Print.row which is why we have Print.cycle - 1 everywere
	}

	public boolean crawl(Page p) {
	boolean notok = false;
	delay();//this will be universal for all crawlers since delay=0 is the same as no delay

		try {
			/* I generally felt that if a website is robot excluded or considered useless due to 
			 * it being not OK response that it should be collected when it finds it in a html page but it 
			 * shouldn't be crawled.  That is why it checks for robot allowed here and not in the
			 * add method or upon creation of the page object.   It will still add it to its data 
			 * but it won't crawl it.  The same is true for checkok
			 */
			if (checkok) {
			p.header().checkUseless();//throws uselessurlexceptions
			Debug.time("Checing is UseLess");
			}
			if (norobotsallowed) {
			p.isRobotExcluded();//this throws RobotsExcludedURLException	
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
		notok = true;
		N.printRow();
		}
		catch (InvalidURLException I) {
		notok = true;
		I.printRow();
		}
		catch (NoContentURLException N) {
		N.printRow();
		}	
		catch (BadEncodingURLException B) {
		B.printRow();
		}
		catch (RobotsExcludedURLException R) {
		R.printRow();
		}
	return notok;
	}

	public void setCheckOK(boolean b) {
	checkok = b;
	}

	public void setDelay(long l) {
	delay = l;
	}

	public void setNoRobotsAllowed(boolean b) {
	norobotsallowed = b;
	}

	public void setSkip(boolean b) {
	skip = b;
	}

}
