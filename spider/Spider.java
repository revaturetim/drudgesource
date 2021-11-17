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
			if (norobots) {
			p.isRobotAllowed();//this throws norobotsallowedexception	
			Debug.endCycleTime("Checking Robot Allowed");
			}
		links(p);
		}
		catch (RedirectedURLException R) {
		R.printRow();
		Page.Header h = p.header();
		final String redloc = h.getRedirectLocation();
			try {
			Page rediruri = new Page(redloc);
				try {
				DataObjects.dada.put(rediruri);
				Debug.endCycleTime("Redirected");
				}
				catch (URISyntaxException U) {
				spinIssue("Found a urlsyntaxexception When Getting Redirect Link", redloc, U); 
				Print.printRow(U, redloc);
				}
				catch (InvalidURLException I) {
				spinIssue("Found a InvalidURLException When getting Redirect Link", redloc, I);
				I.printRow();
				}
				catch (DuplicateURLException Du) {
				spinIssue("Found a DuplicateURLException When getting Redirect Link", redloc, Du);
				Du.printRow();
				}
				catch (ExcludedURLException E) {
				spinIssue("Found a ExcludedURLException When getting Redirect Link", redloc, E);
				E.printRow();
				}
			}	
			catch (MalformedURLException M) {
			spinIssue("Found a malformedurlexception When Getting Redirect Link", redloc, M); 
			Print.printRow(M, redloc);
			}
		}
		//these must be caught here so it can remove it once it is found in data object
		catch (NotOKURLException N) {
		spinIssue("Found a not-OK link while checking if it is a useless", p, N);
		remove = true;	
		N.printRow();
		}
		catch (InvalidURLException I) {
		spinIssue("Found a InvalidURLException When getting Redirect Link", p, I);
		I.printRow();
		}
		catch (NoRobotsURLException N) {
		spinIssue("No robots allowed for this link", p, N);
		remove = true;
		N.printRow();
		}
		catch (NoContentURLException N) {
		spinIssue("Found a no content link while checking if it is a useless", p, N);
		N.printRow();
		}	
		catch (BadEncodingURLException B) {
		spinIssue("Found a bad encoded link while checking if it is a useless", p, B);
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
	norobots = b;
	}

	//I thought that calling issue was humorous like you have a issues
	protected void spinIssue(String i, Object o, Exception e) {
	Debug.check("You are attemping to pass a null value into Spider.spinIssue", i, null, o, null, e, null);
	HashMap<String, Object> h = new HashMap<String, Object>();
	h.put(i + " Issue", o);
	h.put("Exception", e);
	h.put("location", "Spider.spin(int)");
	D.error(h);		
	}

}
