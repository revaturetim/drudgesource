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
	Debug.endCycleTime("End Links");
	}

	public boolean crawl(Page p) {
	boolean remove = false;
		try {
			if (checkok) {
			p.isUseless();//this throws uselessurlexception(s)
			Debug.endCycleTime("Checing is UseLess");
			}

			try {
				if (norobots) {
				p.isRobotAllowed();//this throws norobotsallowedexception
				Debug.endCycleTime("Checking Robot Allowed");
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
				Debug.endCycleTime("Redirected");
				crawl(rediruri);//calls itself
				}
				catch (DuplicateURLException Du) {
				Du.printRow();
				}
				catch (ExcludedURLException E) {
				E.printRow();
				}
			}
			catch (NotHTMLURLException N) {
			spinIssue("Found a NotHTMLUrlException When getting Redirect Link", redloc, N);
			N.printRow();
			}
			catch (URISyntaxException U) {
			spinIssue("Found a urlsyntaxexception When Getting Redirect Link", redloc, U); 
			Print.printRow(U, redloc);
			}
			catch (MalformedURLException M) {
			spinIssue("Found a malformedurlexception When Getting Redirect Link", redloc, M); 
			Print.printRow(M, redloc);
			}
			catch (IOException I) {
			spinIssue("Found a malformedurlexception When Getting Redirect Link", redloc, I); 
			Print.printRow(I, redloc);
			}
		R.printRow();
		}
		//these must be caught here so it can remove it once it is found in data object
		catch (NotHTMLURLException N) {
		remove = true;	
		N.printRow();
		}
		catch (NotOKURLException N) {
		remove = true;	
		N.printRow();
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

	public void setRobotsAllowed(boolean b) {
	norobots = b;
	}

	//I thought that calling issue was humorous like you have a issues
	protected void spinIssue(String i, Object o, Exception e) {
		if (o == null) {
		o = "null";
		}
		if (e instanceof UselessURLException) {
		D.error(e);
		}
		else {
		HashMap<String, Object> t = new HashMap<String, Object>();
		t.put(i + " Issue", o);
		t.put("Exception", e);
		t.put("location", "Spider.spin(int)");
		//t.put("Cycle", cycl);
		D.error(t);
		}
	}

}
