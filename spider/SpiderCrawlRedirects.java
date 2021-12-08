package drudge.spider;

import drudge.data.*;
import drudge.*;
import drudge.global.*;
import drudge.page.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class SpiderCrawlRedirects extends Spider {
protected boolean checkok = true;
protected boolean norobots = false;

	//a convenience method for handling links and it makes other spiders do different things
	protected void links(Page p) {
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

	public boolean crawl(Page p) {
	boolean remove = false;
	delay();//this will be universal for all crawlers since delay=0 is the same as no delay
		try {
			if (checkok) {
			p.isUseless();//this throws uselessurlexception(s)
			Debug.time("Checing is UseLess");
			}

			try {
				if (norobots) {
				p.isRobotAllowed();//this throws norobotsallowedexception
				Debug.time("Checking Robot Allowed");
				}
			links(p);
			}
			catch (NoRobotsURLException N) {
			N.printRow();
			}
		}
		catch (RedirectedURLException R) {
		links(p);
		Page.Header h = p.header();
		final String redloc = h.getRedirectLocation();
			try {
			Page rediruri = new Page(redloc);
				try {
				DataObjects.dada.put(rediruri);
				Debug.time("Redirected");
				crawl(rediruri);//calls itself
				}
				catch (URISyntaxException U) {
				//spinIssue("Found a urlsyntaxexception When Getting Redirect Link", redloc, U); 
				Print.printRow(U, redloc);
				}
				catch (InvalidURLException I) {
				//spinIssue("Found a InvalidUrlException When getting Redirect Link", redloc, I);
				I.printRow();
				}
				catch (DuplicateURLException Du) {
				//spinIssue("Found a DuplicateUrlException When getting Redirect Link", redloc, Du);
				Du.printRow();
				}
				catch (ExcludedURLException E) {
				//spinIssue("Found an ExcludedUrlException When getting Redirect Link", redloc, E);
				E.printRow();
				}
			}
			catch (MalformedURLException M) {
			spinIssue("Found a malformedurlexception When Getting Redirect Link", redloc, M); 
			Print.printRow(M, redloc);
			}
		R.printRow();
		}
		catch (InvalidURLException I) {
		spinIssue("Found a InvalidUrlException While Checking if it is Useless", p, I);
		I.printRow();
		}
		//these must be caught here so it can remove it once it is found in data object
		catch (NotOKURLException N) {
		spinIssue("Found a NotOKURLException While Checking if it is Useless", p, N);
		remove = true;	
		N.printRow();
		}
		catch (NoContentURLException N) {
		spinIssue("Found a NoContentUrlException While Checking if it is Useless", p, N);
		N.printRow();
		}	
		catch (BadEncodingURLException B) {
		spinIssue("Found a BadEncodingUrlException While Checking if it is Useless", p, B);
		B.printRow();
		}
		catch (IOException I) {
		spinIssue("Found an IOException while checking if it is useless", p, I); 
		Print.printRow(I, p);
		}
	return remove;
	}

}
