package drudge.page;

import java.io.*;
import java.net.*;
import drudge.*;
import drudge.data.*;
import drudge.global.*;

/*These static methods will be used to create different page objects depending on their need*/
public class PageFactory {
static public boolean getemails = false;
static public boolean getimages = false;
static public boolean getincluded = false;
static public boolean donotgetexcluded = false;


	static public Page create(final URL url, final String link) {
	Page page = null;
		try {
		URL newurl = new URL(url, P.decode(link));
		P.checkHtmlFile(newurl);
			/*this should be in this order so that you can use include and exclude at the same time*/
			if (PageFactory.donotgetexcluded) {
			P.checkExcluded(newurl, DataEnum.exclude.data, true);
			}
			if (PageFactory.getincluded) {
			P.checkIncluded(newurl, DataEnum.include.data);
			}
		page = PageFactory.create(newurl);
		}
		catch (MalformedURLException M) {
		Print.row(M, link);
		}
		catch (URISyntaxException U) {
		Print.row(U, link);
		}
		catch (EmailURLException E) {
		E.printRow();
			if (PageFactory.getemails) {
				try {
				URL mailurl = (URL)E.getFirstObject();//must cast to ensure it is correct
				DataEnum.emails.data.put(mailurl);
				}
				catch (DuplicateURLException Du) {
				/*do nothing because I don't need to know if exception is thrown*/
				}
			}
		}
		catch (ImageURLException I) {
		I.printRow();
			if (PageFactory.getimages) {
				try {
				URL imageurl = (URL)I.getFirstObject();//must cast to ensure it is correct
				DataEnum.images.data.put(imageurl);
				}
				catch (DuplicateURLException Du) {
				/*do nothing because I don't need to know if exception is thrown*/	
				}
			}
		}
		catch (InvalidURLException U) {
		U.printRow();
		}
		catch (ExcludedURLException E) {
		E.printRow();
		}
	return page;
	}
	
	static public Page create(String link) {
	return create(null, link);	
	}
	
	static public Page create(final URL url) {
	Page page = null;
		try {
		page = new Page(url);
		}
		catch (IOException I) {
		Print.row(I, url);
		}
	return page;
	}
	
	/* createFromString is meant to be used internally 
	 * from an internal string and not form any kind of 
	 * user input 
	 */
	static public Page createFromString(final String string) {
	Page page = null;
		try {
		URL url = new URL(string);
		page = new Page(url);
		}
		catch (MalformedURLException M) {
		D.error("Exception", M, "Location", "PageFactory.createPageFromLine(String, String)", "Why", "Creating URL Object");
		}
		catch (IOException I) {
		D.error("Exception", I, "Location", "PageFactory.createPageFromLine(String, String)", "why", "Creating Page Object");
		}
	return page;
	}	

	static public Page createTestPage(final String link) {
	Page testpage = null;
		if (link.equals(Help.t.parameter) || link.equals(Help.samp.parameter)) {
			try {
			testpage = new Page();
			}
			catch (MalformedURLException M) {
			D.error(M, link);
			}
			catch (IOException I) {
			D.error(I, link);
			}
		}
		else {
		testpage = PageFactory.create(link);
		}
	return testpage;
	}


}
