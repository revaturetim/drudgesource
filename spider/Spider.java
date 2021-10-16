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

	//a convenience method for handling links and it makes other spiders do different things
	protected void links(Page p) {
		try {
		p.getSource();
		Data<Page> pages = p.getLinks();
			for (Page page : pages) {
				try {
				page.isExcluded(Drudge.excorinc);
				DataObjects.dada.put(page);
				}
				catch (ExcludedURLException E) {
				E.printRow();
				}
				catch (DuplicateURLException Du) {
				Du.printRow();
				}
			}
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
			if (norobots) {
			p.isRobotAllowed();//this throws norobotsallowedexception	
			Debug.endCycleTime("Checking Robot Allowed");
			}
		links(p);
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
				}
				catch (DuplicateURLException Du) {
				Du.printRow();
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
		//spinIssue("Found a non-HTML link while checking if it is a useless", p, N);
		remove = true;	
		N.printRow();
		}
		catch (NotOKURLException N) {
		//spinIssue("Found a not-OK link while checking if it is a useless", p, N);
		remove = true;	
		N.printRow();
		}
		catch (NoRobotsURLException N) {
		//spinIssue("No robots allowed for this link", p, N);
		N.printRow();
		}
		catch (NoContentURLException N) {
		//spinIssue("Found a no content link while checking if it is a useless", p, N);
		N.printRow();
		}	
		catch (BadEncodingURLException B) {
		//spinIssue("Found a bad encoded link while checking if it is a useless", p, B);
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
		if (o == null || i == null || e == null) {
		throw new NullPointerException("You are attemping to pass a null value into Spider.spinIssue");
		}
	HashMap<String, Object> h = new HashMap<String, Object>();
	h.put(i + " Issue", o);
	h.put("Exception", e);
	h.put("location", "Spider.spin(int)");
	D.error(h);		
	}

}
