package drudge;

import drudge.spider.*;
import drudge.data.*;
import drudge.global.*;
import drudge.page.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Drudge {
public static String XFACTOR = null;
private static final String sep = "=";

	public static void main(final String[] arg) {
	long MemoryStart = Runtime.getRuntime().freeMemory();
	//these are for method wide variables other helper metheds will use
	int maxcyc = 1;//default value
	int crawlmethod = 1;//default value
	int begcyc = 0;//default starting number for program
	boolean okays = true;//default value
	long delay = 0;//default value
	boolean skip = false;
	boolean norobotsallowed = false;//default value
	Spider spider = null;
	eraseFile(FileNames.error);
	
		//this is the main program loop
LOOP:		for (int i = 0; i < arg.length; i++) {
		final String a = arg[i];
		/*TRY TO PUT OPTIONS IN ALPHABETICAL ORDER*/
			if ((a.equals(Help.about.parameter) || a.equals(Help.a.parameter)) && arg.length == 1) {
			Print.helpMenu(a);
			break;
			}
			//the only commands that should have startWith vs equals are ones that use a combination like c=xxxxx
			else if (a.startsWith(Help.c.parameter + Drudge.sep)) {
				try {
				crawlmethod = getNumber(a);
				}
				catch (NumberFormatException N) {
				Print.error(N);
				break;
				}
				
			}
			else if (a.startsWith(Help.d.parameter + Drudge.sep)) {
				try {
				int pagedata = getNumber(a);
				DataEnum.links.data.setLevel(pagedata);
					if (pagedata == 3) {
						try {
						DataEnum.words.data.begin();
						}
						catch (IOException I) {
						Print.error(I);
						}
						catch (Exception E) {
						Print.error(E);
						}
					}
				}
				catch (NumberFormatException N) {
				Print.error(N);
				break;
				}
			}
			else if (a.equals(Help.e.parameter)) {
			Page.getemails = true;
				try {	
				DataEnum.emails.data.begin();
				}
				catch (IOException I) {
				Print.error(I);
				}
				catch (Exception E) {
				Print.error(E);
				}
			}	
			else if (a.equals(Help.exc.parameter)) {
			Page.donotgetexcluded = true;
				try {
				DataEnum.exclude.data.begin();
				}
				catch (IOException I) {
				Print.error(I);
				}
				catch (Exception E) {
				Print.error(E);
				}	
			}
			else if (a.startsWith(Help.exclude.parameter + Drudge.sep)) {
			Page.donotgetexcluded = true;
			String file = getString(a);
				try {
				DataEnum.exclude.data.setSource(file);
				DataEnum.exclude.data.begin();
				}
				catch (IOException I) {
				Print.error(I);
				}
				catch (Exception E) {
				Print.error(E);
				}	
			}
			else if ((a.equals(Help.help.parameter) || a.equals(Help.h.parameter)) && arg.length == 1) {
			Print.helpMenu(a);
			break;
			}
			else if ((a.equals(Help.help.parameter) || a.equals(Help.h.parameter)) && i == 0 && arg.length == 2) {
			Print.helpMenu(arg[i + 1]);
			break;	
			}
			else if ((a.equals(Help.i.parameter) || a.startsWith(Help.input.parameter + Drudge.sep)) && arg.length == 1) {
			String file = FileNames.in;
				if (a.startsWith(Help.input.parameter + Drudge.sep)) {
				file = getString(a);
				}
				try {
				LineNumberReader reader = new LineNumberReader(new FileReader(file));
				String contents = new String();
					for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					contents = contents + line + " ";
					}
				String[] inputs = contents.split(" ");
				main(inputs);
				}
				catch (FileNotFoundException F) {
				System.out.println("File " + FileNames.in + " could not be foun");
				Print.helpMenu(a);
				Print.error(F);
				}
				catch (IOException I) {
				System.out.println("Trouble in reading file " + FileNames.in);
				Print.error(I);
				}
			break;
			}
			else if (a.equals(Help.img.parameter)) {
			Page.getimages = true;
			}
			else if (a.equals(Help.inc.parameter)) {
			Page.getincluded = true;
				try {
				DataEnum.include.data.begin();
				}	
				catch (IOException I) {
				Print.error(I);
				}
				catch (Exception E) {
				Print.error(E);
				}
			}
			else if (a.startsWith(Help.include.parameter + Drudge.sep)) {
			Page.getincluded = true;
			String file = getString(a);
				try {
				DataEnum.include.data.setSource(file);
				DataEnum.include.data.begin();
				}	
				catch (IOException I) {
				Print.error(I);
				}
				catch (Exception E) {
				Print.error(E);
				}
			}
			else if (a.equals(Help.l.parameter) && arg.length == 1) {
			System.out.println(ThisProgram.license);
			break;
			}
			else if (a.startsWith(Help.m.parameter + Drudge.sep)) {
				try {
				final int c = getNumber(a);
				Print.UselessClass = UselessMessages.getClass(c);
				}
				catch (NumberFormatException N) {
				Print.helpMenu(a);
				Print.error(N);
				break;
				}
			}
			else if (a.startsWith(Help.n.parameter + Drudge.sep)) {
				try {
				maxcyc = getNumber(a);
				}
				catch (NumberFormatException N) {
				Print.error(N);
				break;
				}
			}
			else if (a.equals(Help.nok.parameter) || a.equals(Help.skp.parameter)) {
			okays = false;//this means it won't check for ok responses in spider
			}
			else if (a.equals(Help.o.parameter)) {
			final String outfile = FileNames.out;	
				try {
				PrintStream p = new PrintStream(outfile);
				System.out.println("Printing to file called " + outfile);
				Print.GSTREAM = p;
				}
				catch (FileNotFoundException F) {
				Print.error(F, outfile);
				break;
				}
			}
			else if (a.startsWith(Help.output.parameter + Drudge.sep)) {
			final String outfile = getString(a);	
				try {
				PrintStream p = new PrintStream(outfile);
				System.out.println("Printing to file called " + outfile);
				Print.GSTREAM = p;
				}
				catch (FileNotFoundException F) {
				Print.error(F, outfile);
				break;
				}
			}
			else if (a.startsWith(Help.p.parameter + Drudge.sep)) {
				try {
				String p = getString(a);
				InetAddress inetadress = InetAddress.getByName(p);//I net a dress funny :)
					try {
					InetSocketAddress sockdress = new InetSocketAddress(inetadress, 80);//more funny stuff
						try {
						Page.proxyserver = new Proxy(Proxy.Type.HTTP, sockdress);
						}
						catch (IllegalArgumentException I) {
						Print.error(I);
						}
					}
					catch (IllegalArgumentException I) {
					Print.error(I);
					}	
				}
				catch (UnknownHostException U) {
				Print.error(U);
				break;
				}
		
			}
			else if (a.equals(Help.rob.parameter)) {
			norobotsallowed = true;
			}
			else if (a.startsWith(Help.w.parameter + Drudge.sep)) {
				try {
				delay = (long)getNumber(a);
				}
				catch (NumberFormatException N) {
				Print.error(N);
				break;
				}
			
			}
			else if (a.startsWith(Help.x.parameter + Drudge.sep)) {
			Drudge.XFACTOR = getString(a);
			}
	
			//secret debugger options down here
			else if (a.equals(DevHelp.A.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
					try {
					p.isRobotExcluded();
					System.out.println("Yes!  " + ThisProgram.name + " is allowed");
					}
					catch (RobotsExcludedURLException N) {
					Print.error(N, p);
					}
				}
			break;
			}
			else if (a.equals(DevHelp.C.parameter) && arg.length == 2 && i == 0) {
			Debug.cycletimeon = true;
			continue;
			}
			else if (a.equals(DevHelp.E.parameter) && arg.length == 1) {
			DataEnum.checkErrorAll();
			System.out.println("Have a nice day :)");
			break;
			}
			else if (a.equals(DevHelp.G.parameter) && i == 0) {
			System.out.println("Creating GREP-able file to " + FileNames.gout + " so you can use grep on the results.");
				try {
				Print.GSTREAM = new PrintStream(FileNames.gout);
				}
				catch (FileNotFoundException F) {
				Print.GSTREAM = System.out;
				Print.error(F, FileNames.gout);
				}
			continue;//this will make sure it continues to the next the loop	
			}
			else if ((a.equals(DevHelp.H.parameter)) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				URLConnection c = p.getConnection();
				System.out.println("----------------Request-Fields------------------");
				Map<String, List<String>> request = c.getRequestProperties();
					for (Map.Entry<String, List<String>> entry: request.entrySet()) {
					System.out.println( entry.getKey() + " : " + entry.getValue());
					}	
		
				System.out.println("----------------Response-Headers----------------");	
				Map<String, List<String>> heads = c.getHeaderFields();
					for (Map.Entry<String, List<String>> entry : heads.entrySet()) {
					System.out.println(entry.getKey() + " : " + entry.getValue());
					}
				}
			break;
			}
			else if (a.equals(DevHelp.HELP.parameter) && arg.length == 1) {
			Print.helpMenu(a);
			break;
			}
			else if (a.equals(DevHelp.I.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);	
				if (p != null) {
					try {
					DataEnum.include.data.begin();
						try {
						p.isIncluded();
						System.out.println(p.toString() + " is in the " + FileNames.include + " file.");	
						}
						catch (ExcludedURLException E) {
						Print.error(E, p);
						}
					}	
					catch (IOException I) {
					Print.error(I);
					}
					catch (Exception E) {
					Print.error(E);
					}	
				}
			break;
			}
			else if (a.startsWith(DevHelp.IMG.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				p.getSource();
				Page.getimages = true;
				p.getLinks();//collects emails as well as links
					for (URL image : (Data<URL>)DataEnum.images.data) {
					System.out.println(image);
					}
				}
			break;	
			}
			else if (a.equals(DevHelp.IP.parameter) && arg.length == 2 && i == 0) {
			URI uri = URI.create(arg[i + 1]);
				try {
				String host = uri.getHost();
					//this is so it can go backwards 
					if (host == null) {
					host = arg[i + 1];
					}
				InetAddress[] addresses = InetAddress.getAllByName(host);
					for (InetAddress address : addresses) {
					System.out.println("The inet address is:		" + address.toString());
					System.out.print("The signed byte address is:	");
						for (byte b : address.getAddress()) {
						System.out.print(b);
						System.out.print(" ");
						}
					System.out.println();
					System.out.println("The inet host is:		" + address.getHostName());
					System.out.println("The inet host address is:	" + address.getHostAddress());
					System.out.println("The inet canonical host is:	" + address.getCanonicalHostName());
					System.out.println();
					}
				InetAddress localaddress = InetAddress.getLocalHost();
				System.out.println("The local inet host is:		" + localaddress.getLocalHost());
				System.out.println("The local inet address is:	" + localaddress.getHostAddress());
				InetAddress loopbackaddress = InetAddress.getLoopbackAddress();
				System.out.println("The loopback inet host is:	" + loopbackaddress.getLocalHost());
				System.out.println("The loopback inet address is:	" + loopbackaddress.getHostAddress());
				}
				catch (UnknownHostException U) {
				Print.error(U, arg[i + 1]);
				}
			break;
			}
			else if (a.equals(DevHelp.K.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				p.getSource();
				Data<String> keywords = p.getKeywords();
					for (String keyword : keywords) {
					System.out.println(keyword);
					}	
				}
			break;
			}
			else if (a.equals(DevHelp.L.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				p.getSource();
				Data<Page> pages = p.getLinks();
					for (Page page : pages) {
					System.out.println(page.toString());
					}
				}
			break;
			}
			else if (a.equals(DevHelp.M.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				p.getSource();
				Page.getemails = true;
				p.getLinks();//called because getlinks also collects emails
					for (URL email : (Data<URL>)DataEnum.emails.data) {
					System.out.println(email);
					}
				}
			break;	
			}
			else if (a.equals(DevHelp.MIME.parameter) && arg.length == 2 && i == 0) {
			FileNameMap mimefile = URLConnection.getFileNameMap();
			System.out.println("The mime type for " + arg[i + 1] + " is " + mimefile.getContentTypeFor(arg[i + 1]));
			break;	
			}
			else if (a.startsWith(DevHelp.P.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
					try {
					final long pt = System.currentTimeMillis();
					final URL url = p.getURL();
					URLConnection connection = url.openConnection();
					connection.connect();
					//successful connection
					final long et = System.currentTimeMillis();
					final long time = et - pt;
					System.out.print("The total time it took to connect to ");
					System.out.print(arg[i + 1]);
					System.out.print(" was ");
					Print.totalTime(time);
					System.out.print("\n");
					}
					catch (IOException I) {
					Print.error(I, arg[i + 1]);
					}
				}
			break;
			}
			else if (a.equals(DevHelp.R.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				URL roboturl = p.getRobotURL();
				Page robotpage = D.createPage(roboturl);
				System.out.println(robotpage.getSource());
				}
			break;	
			}
			else if (a.equals(DevHelp.S.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				System.out.println(p.getSource());
				}
			break;
			}
			else if (a.equals(DevHelp.T.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				p.getSource(); 
				String title = p.getTitle();
				System.out.println(title);
				}
			break;
			}
			else if (a.equals(DevHelp.U.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
					try {	
					p.isUseless();
					System.out.println(p.toString() + " is not a useless url for this program");	
					}
					catch (UselessURLException U) {
					Print.error(U, p);
					}
				}
			break;
			}
			else if (a.equals(DevHelp.V.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
				System.out.println(p.toString() + " is a valid html file for " + ThisProgram.name);
				}
			break;
			}
			else if (a.equals(DevHelp.W.parameter) && arg.length == 2 && i == 0) {
				
				try {
				DataEnum.words.data.begin();
					if (DataEnum.words.data.contains(arg[i + 1])) {
					System.out.println(arg[i + 1] + " is in the " + FileNames.words + " file.");
					}
					else {
					System.out.println(arg[i + 1] + " is NOT in the " + FileNames.words + " file.");
					}		
				}	
				catch (IOException I) {
				Print.error(I);
				}
				catch (Exception E) {
				Print.error(E);
				}			
			break;
			}
			else if (a.equals(DevHelp.X.parameter) && arg.length == 2 && i == 0) {
			Page p = createTestPage(arg[i + 1]);
				if (p != null) {
					try {
					DataEnum.include.data.begin();
						try {
						p.isExcluded();
						System.out.println(p.toString() + " is NOT in the " + FileNames.exclude + " file.");	
						}
						catch (ExcludedURLException E) {
						Print.error(E, p);
						}
					}	
					catch (IOException I) {
					Print.error(I);
					}
					catch (Exception E) {
					Print.error(E);
					}	
				}
			break;
			}
			/*!this is the last parameter in the program!*/
			else if (i == arg.length - 1) {
			Print.columnHeaders();//this should be called first to show errors correctly in output columns
			Debug.time("Print Column Headers");
			String lastarg = a;//this exist only as a naming convention
				if (lastarg.equals(Help.s.parameter)) {
					try {
					begcyc = CountFile.get();
					maxcyc = maxcyc + begcyc;
					DataEnum.links.data.begin();
					}
					catch (IOException I) {
					Print.error(I);
					}
					catch (Exception E) {
					Print.error(E);
					}				

				}
				else if (lastarg.startsWith(Help.s.parameter + Drudge.sep)) {
					try {
					int num = getNumber(lastarg);
					begcyc = num - 1;
					maxcyc = maxcyc + begcyc;
					DataEnum.links.data.begin();
					}
					catch (NumberFormatException N) {
					Print.error(N);
					}
					catch (Exception E) {
					Print.error(E);
					}
			
				}
				else if (lastarg.equals(Help.t.parameter) || lastarg.equals(Help.samp.parameter)) {
				DataEnum.truncateAll();
					try {
					Page testpage = new Page();
					D.add(testpage, DataEnum.links.data);
					}
					catch (MalformedURLException M) {
					D.error(M);
					}
					catch (IOException I) {
					D.error(I);
					}
				}
				else if (lastarg.startsWith(Help.samp.parameter + Drudge.sep)) {
				DataEnum.truncateAll();	
				String file = getString(lastarg);
				File sampfile = new File(file);
				D.add(sampfile.toURI().toString(), DataEnum.links.data);
				}
				else if (
				lastarg.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") || 
				lastarg.matches(
				"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\." + 
				"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\." +
				"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\." + 
				"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")
				) {
				DataEnum.truncateAll();
				D.add("http://" + lastarg, DataEnum.links.data);//program assumes it is going to make an http connection
				}
				else if (lastarg.equals("loopback")) {
				DataEnum.truncateAll();	
				InetAddress address = InetAddress.getLoopbackAddress();//throws unknownhost exception
				D.add(address.getCanonicalHostName(), DataEnum.links.data);
				}
				else {
				DataEnum.truncateAll();	
				D.add(lastarg, DataEnum.links.data);
				}

			spider = createSpider(crawlmethod);
			spider.setCheckOK(okays);
			spider.setDelay(delay);
			spider.setNoRobotsAllowed(norobotsallowed);

			final long begintime = System.currentTimeMillis();
			begcyc = spider.crawl(begcyc, maxcyc);
			
			Debug.time("Spider Crawl");
				try {
				DataEnum.links.data.end();
				}
				catch (Exception E) {
				Print.error(E);
				}
				if (Page.getemails) {
					try {
					DataEnum.emails.data.end();
					}
					catch (Exception E) {
					Print.error(E);
					}
				}
				if (Page.getimages) {
					try {
					DataEnum.images.data.end();
					}
					catch (Exception E) {
					Print.error(E);
					}
				}
			final long endtime = System.currentTimeMillis();
			Debug.time("DataBase disconnect");
				try {
				CountFile.set(begcyc);
				}
				catch (IOException I) {
				Print.error(I);
				}
			Debug.time("Writing Countfile");
				
			//this is to print out last stats of progam	
			System.out.printf("Links:\t\t%d\n",  DataEnum.links.data.size());
			System.out.print("Time:\t\t");
			Print.totalTime(endtime - begintime);
			long MemoryNow = Runtime.getRuntime().freeMemory();
			long MemoryUsed = MemoryNow - MemoryStart;
			System.out.println("Memory:\t\t" + String.valueOf(MemoryUsed));
			System.out.printf("Date:\t\t%s\n", D.getDate());
			System.out.printf("Appreciation:\t%s\n", "Thank you for using this program");
			System.out.printf("Salutation:\t%s\n", "Have a nice day!");
			}
			else {
			System.out.println("Option " + a + " was ignored by this program.");
			}//end of lastarg of loop
		}//end of loop
	}//end of program
	
	static Page createTestPage(final String link) {
	Page testpage = null;
		try {
			if (link.equals(Help.t.parameter) || link.equals(Help.samp.parameter)) {
			testpage = new Page();
			}
			else {
			testpage = new Page(link);
			}
		}
		catch (InvalidURLException I) {
		Print.error(I, link);
		}
		catch (URISyntaxException U) {
		Print.error(U, link);
		}
		catch (MalformedURLException M) {
		Print.error(M, link);
		}
		catch (IOException I) {
		Print.error(I, link);
		}
	return testpage;
	}


	static Spider createSpider(int c) {
	Debug.time("Creating Spider");//this starts cycle time for entire thing
	Spider spider = null;
		switch (c) {
			
			case 1:  
			spider = new Spider();
			break;

			case 2:
			spider = new SpiderSpam();
			break;

			case 4:
			spider = new SpiderComp(new LinkComparator<Page>());
			break;

			case 5:
				try {
				Integer I = Integer.valueOf(Drudge.XFACTOR);
				int s = I.intValue();
				spider = new SpiderRandom(s);	
				}
				catch (NumberFormatException N) {
				spider = new SpiderRandom((int)System.currentTimeMillis());
				}
			break;

			case 6:
			spider = new SpiderComp(new TopComparator<Page>()); 
			break;

			case 7:
			spider = new SpiderCrawlRedirects();
			break;

			case 8:
			spider = new SpiderComp(new NaturalComparator<Page>());
			break;

			default:
			System.out.println("There is no spider option for " + String.valueOf(c));
			Print.helpMenu(Help.c.parameter);	
			break;
		}
		
	Debug.time("Create Spider Object");
	return spider;
	}
	
	static boolean eraseFile(String filename) {
	boolean erased = false;
	File efile = new File(filename);
		if (efile.exists()) {
		erased = efile.delete();
		}
	return erased;
	}
	
	static private int getNumber(String arg) throws NumberFormatException {
	String[] ins = arg.split(Drudge.sep, 2);
	return Integer.parseUnsignedInt(ins[1]);
	}	
	
	static private String getString(String arg) {
	String[] ins = arg.split(Drudge.sep, 2);
	return ins[1];
	}
}
