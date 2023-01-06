package drudge;

import drudge.global.*;
import drudge.data.UselessMessages;

enum Help {
about("-about", "About this program."),
a("-a", about.msg),
c("-c", "This will tell you about the different crawl methods available."),
d("-d", "This wil let you store different amounts of data from the webpages."),
e("-e", "This will let you collect emails from the webpages you crawl through."),
h("-h", "This will print out the help message."),
i("-i", "This will let you store program options in a file called " + FileNames.in + "."),
l("-l", "This will list the license agreement for " + ThisProgram.name + "."),
m("-m", "This will show you the different output messages that it can show you while crawling."),
n("-n", "This will set the maximum number of cycles."),
o("-o", "This will write the output to a file called " + FileNames.out),
p("-p", "This will let you set a proxy server to use while crawling."),
r("-r", "This will set " + ThisProgram.name + " to respect a robots.txt policy if the website has one."),
s("-s", "This will set from what cycle to continue from."),
t("-t", "This will use the test option from a sample file called " + FileNames.samphtml + "."),
w("-w", "This will slow your crawler down so you don't overload a website."), 
x("-x", "This will let you pass in extra options for " + ThisProgram.name + "."),
inc("-inc", "This will set " + ThisProgram.name + " to only include page from certain websites."),
include("-include", inc.msg),
exc("-exc", "This will set " + ThisProgram.name + " to exclude pages from certain websites."),
exclude("-exlcude", exc.msg),
nok("-nok", "This will set " + ThisProgram.name + " to accept all webites for faster crawling."),
samp("-samp", t.msg),
help("-help", h.msg);

private String par;
private String msg;

	private Help(String p, String m) {
	par = p;
	msg = m;
	}

/*This is the actual class part of the enum*/
static final private String HELP = "-HELP";

	static void print(final String key) {
		
		if (key.equals(HELP)) {
		System.out.println("----------------Special-Developer-Options----------------");
		System.out.printf("%-10s This will see if http://your.url.here robot.txt policy will let you crawl their website\n", "-A");
		//System.out.printf("%-10s \n", "-B");
		System.out.printf("%-10s This will list out specific times in the cycle\n", "-C");
		//System.out.printf("%-10s \n", "-D");
		System.out.printf("%-10s This will check to see if there are any errors in the data storage\n", "-E"); 
		//System.out.printf("%-10s \n", "-F");
		System.out.printf("%-10s This will send output to a lind by line grep-able file called " + FileNames.out + "\n", "-G");
		System.out.printf("%-10s http://your.url.here will print out the server headers that it spits out\n", "-H");
		System.out.printf("%-10s This will test to see if a page is in the " + FileNames.include + " file\n", "-I");
		//System.out.printf("%-10s \n", "-J");
		System.out.printf("%-10s This will show the keywords of http://your.url.here\n", "-K");
		System.out.printf("%-10s This will show the links of http://your.url.here\n", "-L");
		//System.out.printf("%-10s \n", "-M");
		//System.out.printf("%-10s \n", "-N");
		//System.out.printf("%-10s \n", "-O"); 
		System.out.printf("%-10s This will \"ping\" a website to test the quality of the connection\n", "-P");
		//System.out.printf("%-10s \n", "-Q");
		System.out.printf("%-10s This will see if url is allowed by the robots.txt file.\n", "-R");
		System.out.printf("%-10s This will show the source of http://your.url.here\n", "-S");
		System.out.printf("%-10s This will show the title of http://your.url.here\n", "-T");
		System.out.printf("%-10s http://your.url.here will test if this url is useless for this program\n", "-U");
		System.out.printf("%-10s http://your.url.here will print out if this is a valid html file for this program\n", "-V");
		System.out.printf("%-10s This will test to see if a word is in the " + FileNames.words + " file\n", "-W");
		System.out.printf("%-10s This will test to see if a page is in the " + FileNames.exclude + " file\n", "-X");
		//System.out.printf("%-10s \n", "-Y");
		//System.out.printf("%-10s \n", "-Z");
		}
		else if (key.equals(help.par) || key.equals(h.par)) {
		System.out.println("-----------Help-----------");

			for (Help h : Help.values()) {
			System.out.printf("%-10s" + h.msg + "\n", h.par);
			}
		}
		else if (key.equals(about.par) || key.equals(a.par)) {
		System.out.println("------About------");
		System.out.println("This program is a spider program that attempts ");
		System.out.println("to index webpage on the world wide web");
		System.out.println(ThisProgram.name + " " + ThisProgram.version);
		System.out.println("by " + ThisProgram.author);
		}
		else if (key.equals(i.par)) {
		System.out.println("This is to read inputs from an input file called \"" + FileNames.in + "\"");
		System.out.println("The first line of the file should be identical to the commands you want to use for this program");
		System.out.println("The file should only have to be one line and is a convenience so you don't have to type them in yourself");
		System.out.println("You're Welcome");
		}
		else if (key.equals(l.par)) {
		System.out.println("This will print out the license agreement");
		}
		else if (key.equals(o.par)) {
		System.out.println("This is to send all output to a file called \"" + FileNames.out + "\"");
		}
		else if (key.equals(d.par)) {
		System.out.println("This is used to set what you will be filling in for final data");
		System.out.println("The default value is -d=1 which will be just place urls in datafile");
		System.out.println("A value of -d=2 will insert page url and page titles into datafile");
		System.out.println("A value of -d=3 will insert page url, page titles, and page keywords");
		}
		else if (key.equals(e.par)) {
		System.out.println("This is to collect emails and store them in a seperate email file called " + FileNames.emails);
		}
		else if (key.equals(m.par)) {
		System.out.println("Each Exception is numbered and if you want to see them in the output you have to use a number");
			for (UselessMessages U : UselessMessages.values()) {
			System.out.printf("%-4s %s\n", U.num, U.hlp); 
			}
		System.out.printf("See if you can find them all!\n");
		}
		else if (key.equals(c.par)) {
		System.out.println("Option c is used for the different crawl methods that are available to this program.");
		System.out.println("Method 1 is the basic web spider of this program.  It spiders links in the order that it finds them.");
		System.out.println("Method 2 is meant to be a website overload tool and not to be used by nice people.");
		System.out.println("Method 5 randomizes the order of the links so that it doesn't spiders them in the order that they are found.");
		System.out.println("Method 6 searches links that are not a not from the same domain first.");
		System.out.println("Method 7 crawls redirected links as they are found");
		System.out.println("Method 8 sorts data in purley alpha-numeric order and is faster than other methods");
		}
		else if (key.equals(n.par)) {
		System.out.println("This is to determine the number of cycles this program will execute");
		System.out.println("Example:  -n=(number of cycles) http://my.website.com/goes/here");
		System.out.println("This is the most important part of the program inputs because it will not do anything without this");
		System.out.println("A url must follow the -n option or it won't work");
		System.out.println("This option plus the url must be the last commands or it wont work");
		System.out.println("Example:  -m=(message) -c=(crawl method) -n=(1, 2, 3...) http://my.website.com/goes/here");
		}
		else if (key.equals(x.par)) {
		System.out.println("This is used for extra options for various commands.");
		System.out.println("See specific help on how it is used with those options.:)");
		}
		else if (key.equals(s.par)) {
		System.out.println("This is used for continuation purposes so you don't have to start over every time you start program.");
		System.out.println("Just specify a entry number to start from and use the -n option to specify where it should stop.");
		System.out.println("Example:  >drudge -n=100 -s=10"); 
		System.out.println("Example:  drudge -n=100 -s to tell this program to pick up where it last left off.");
		System.out.println("This will start at the entry of 10 and stop at 100.");
		}
		else if (key.equals(samp.par) || key.equals(t.par)) {
		System.out.println("The -samp (or -t) option can be used only as the last parameter and in place of the url parameter.");
		System.out.println("It is used as a test page for testing this program.");
		}
		else if (key.equals(p.par)) {
		System.out.println("This option is used in case you want to use a proxy server");
		System.out.println("Example: -p=www.proxyserver.com");
		}
		else if (key.equals(r.par)) {
		System.out.println("This is used when you want to respect robots.txt policies of a website.");
		System.out.println("Warning: Don't assume this will always work because not all robots.txt files are made correctly!");
		}
		else if (key.equals(inc.par) || key.equals(include.par)) {
		System.out.println("This is to include only urls listed in the " + FileNames.exclude + " file");
		System.out.println("This is a very inclusive option because it will include all urls listed in file");
		System.out.println("Example: -inc");
		System.out.println("You can also use -include= if you want to use your own custom file");
		System.out.println("Example: -include=your/custom/file/here");
		}
		else if (key.equals(exc.par) || key.equals(exclude.par)) {
		System.out.println("This is to exclude urls listed in the " + FileNames.exclude + " file");
		System.out.println("This is a very exclusive option because it will exlcude all other urls listed in file");
		System.out.println("Example: -exc");
		System.out.println("You can also use -exclude= if you want to use your own custom file");
		System.out.println("Example: -exclude=your/custom/file/here");
		}
		else if (key.equals(nok.par)) {
		System.out.println("This will force the spider to only accept all HTTP responses.");
		System.out.println("This option will greatly speed up crawling but it may not get quality websites since it accepts useless urls.");
		System.out.println("Having this option on will capture redirects which can lead to better quality results.");
		System.out.println("It is suggested that this be turned off and only turned on for faster crawling.");
		}
		else if (key.equals(w.par)) {
		System.out.println("This will slow your crawler down between page crawls");
		System.out.println("Example: -w=889 will be 889 milliseconds between crawls.");
		}

	}
}


