package drudge.spider;

import drudge.data.*;
import drudge.*;
import drudge.global.*;
import drudge.page.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class Spider {
static protected final int ok = 0;
static protected final int notok = 1;
static protected final int redir = 2;
static protected final int nobot = 3;
protected boolean checkok = true;
protected long delay = 0L;
protected boolean norobotsallowed = false;
protected boolean skip = false;
protected boolean stop = false;
protected boolean follow = false;
protected boolean readsitemap = true;

	//a convenience method for handling links and it makes other spiders do different things
	protected void links(final Page p) {
	p.source().fill();
	Data pages = p.getLinks();
	DataEnum.links.data.insert(pages);
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
		try {
		DataEnum.links.data.insert(redirect);
		crawl(redirect);
		}
		catch (DuplicateURLException D) {
		D.printRow();
		}
		catch (IllegalArgumentException I) {
		Print.row(I, h.redirectlocation);
		}
	}

	public int loop(final int max) {
	final int cminus = DataEnum.links.data.firstIndexNumber() - 1;//it is + cminus so that the Data.offset works correctly!
		for (Page p = (Page)DataEnum.links.data.select(Print.cycle + cminus); p != null && Print.cycle < max; p = (Page)DataEnum.links.data.select(Print.cycle + cminus)) {
			final int cval = this.crawl(p);
			/* This has the same effect as skipping since when -nok option is on it 
			 * sees all urls as acceptables since the default return value in crawls
			 * is false.
			 */
			if (cval == Spider.notok) {
			DataEnum.links.data.delete(Print.cycle + cminus);
			Debug.time("Removing Link");
			}
			else {
			p.printRow();
			}
			/*this is for STOP debug option and nothing else*/
			if (this.stop) {
			System.out.print("\tEnter N to continue; Q to quit; M for useless messages; or a cycle number: ");
			Scanner scan = new Scanner(System.in);
				if (scan.hasNextInt()) {
				int c = scan.nextInt();
				Print.cycle = c;
				}
				else if (scan.hasNext()) {
				String response = scan.next();
					if (response.equalsIgnoreCase("n")) continue;
					else if (response.equalsIgnoreCase("q")) break;
					else if (response.equalsIgnoreCase("m")) {
						if (UselessMessages.uselessmessage == UselessMessages.NONE) {
						UselessMessages.uselessmessage = UselessMessages.ALL;
						}
						else {
						UselessMessages.uselessmessage = UselessMessages.NONE;
						}
					}
				}
				else continue;
			System.out.println();
			}
		Debug.time("End of Cycle " + String.valueOf(Print.cycle + cminus));
		}
	return Print.cycle;//Print.cycle is incremented inside of Print.row which is why we have Print.cycle - 1 everywere
	}

	public int crawl(Page p) {
	int crawlvalue = Spider.ok;
	delay();//this will be universal for all crawlers since delay=0 is the same as no delay
	Debug.time("Crawler Delayed");
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
			if (readsitemap) {
			DataEnum.links.data.insert(p.sitemapurls());
			}
		links(p);
		Debug.time("End Links");
		crawlvalue = Spider.ok;
		}
		catch (RedirectedURLException R) {
		R.printRow();
			if (this.follow) {
			redirect(p);
			Debug.time("Redirected");
			crawlvalue = Spider.redir;
			}
			else {
			crawlvalue = Spider.notok;
			}
		}
		//these must be caught here so it can remove it once it is found in data object
		catch (NotOKURLException N) {
		N.printRow();
		crawlvalue = Spider.notok;
		}
		catch (InvalidURLException I) {
		I.printRow();
		crawlvalue = Spider.notok;
		}
		catch (NoContentURLException N) {
		N.printRow();
		}	
		catch (BadEncodingURLException B) {
		B.printRow();
		}
		catch (RobotsExcludedURLException R) {
		R.printRow();
		//crawlvalue = Spider.nobot;
		}
	return crawlvalue;
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

	public void setStop(boolean b) {
	stop = b;
	}

	public void setFollowRedirect(boolean b) {
	follow = b;
	}

	public void setReadSitemap(boolean b) {
	readsitemap = b;
	}

}
