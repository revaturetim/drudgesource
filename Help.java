package drudge;

import drudge.global.*;
import drudge.data.UselessMessages;

public enum Help {
top("Short", "Long", "<------------Help Options------------>"),
help("-h", "-help", "Print out Help Options for " + ThisProgram.name),
about("-a", "-about", "About this program.", 
	"------About------\n" 
	+ "This program is a spider program that attempts\n to index webpage on the world wide web\n"
	+ ThisProgram.name + " " + ThisProgram.version
	+ " by " + ThisProgram.author + "\n"),
b("-b", "-begin", "This will set from what cycle to continue from.", 
	"This is used for continuation purposes so you don't have to start your crawls from the beginning.\n"
	+ "Just specify the cycle you wish to start from and use the -n option to specify how many more cycle it should do after that.\n"
	+ "	Example:  drudge -n=100 -s=10 will finish at cycle 110\n"
	+ "If you do not specify a number it will start at the last cycle count of your last crawl\n"
	+ "	Example:  drudge -n=100 -s to tell this program to pick up where it last left off.\n"
	+ "This will start at your last entrty and continue for the next 100 cycles.\n"),
c("-c", "-cycles", "This will set the maximum number of cycles.", 
	"This is to determine the number of cycles this program will execute\n"
	+ "	Example:  -n=(number of cycles) http://my.website.com/goes/here\n"
	+ "If this option is not used the default value will be set to one cycle\n"),
d("-d", "-data", "This wil let you store different amounts of data from the webpages.", 
	"This is used to set how much data you wish to collect from individual webpages\n"
	+ "A value of -d=1 will collect links only and is the default setting.\n"
	+ "A value of -d=2 will collect links and page titles\n"
	+ "A value of -d=3 will collect links page titles, and page keywords\n"),
eml("-e", "-email", "This will collect emails from the webpages you crawl through.", 
	"This is to collect emails and store them in a seperate email file called " + FileNames.emails),
exc("-exc", "-exclude", "This will set " + ThisProgram.name + " to exclude pages from certain websites.", 
	"This option can be used to exclude all links found in "
	+ FileNames.exclude + " file\n  While -exclude will let you use a custom file\nExample: -exclude=relative/page/to/your/file/here\n"),
f("-f"),
g("-g"),
i("-i", "-input", "This will let you store program options in a file called " + FileNames.in + ".", 
	"This is to read inputs from a file called \"" + FileNames.in + "\""
	+ "The first line of the file should be identical to the commands you want to use for this program\n"
	+ "This is so you can use a custom file for inputs.\n"
	+ "	Example: -input=your/custom/input/file\n"
	+ "The file should only have to be one line and is useful in cases where the input commands are long and complicated.\n"
	+ "You're Welcome!"),
img("-img", "-image", "This will collect images in a file called " + FileNames.images + ".", 
	"This will collect images and store them in the destination source.\n"),
inc("-inc", "-include", "This will set " + ThisProgram.name + " to only include page from certain websites.", 
	"This option will set " + ThisProgram.name + " to only include urls listed in the " + FileNames.exclude + " file\n" 
	+ "You can use this option to set a custum file to set included links instead of using the defalut file.\n"
	+ "	Example: -include=relative/path/to/your/custom/file/here\n"),
j("-j"),
k("-k"),
l("-l", "-license", "This will list the license agreement for " + ThisProgram.name + ".", 
	"This will print out the license agreement\n"),
m("-m", "-message", "This will show you the different output messages that it can show you while crawling."),
nok("-nok", "-allow", "This will set " + ThisProgram.name + " to accept all webites for faster crawling.", 
	"When this option is turned on " + ThisProgram.name + " will accept all HTTP server responses.\n"
	+ "This option will greatly speed up crawling because it will not physically check HTTP responses.\n"
	+ "Having this option on will still capture redirects.\n"
	+ "It is suggested that this be turned off and only turned on for faster crawling.\n"
	+ "This option has the effect of skipping non-200 urls.\n"),
o("-o", "-output", "This will write the output to a file called " + FileNames.out, 
	"This is to send all output to a file called \"" + FileNames.out + "\"\n"
		+ "-output is used when you want to record all output to a file.\n"
		+ "	Example:  -output=file/you/want/to/use\n"),
p("-p", "-proxy", "This will let you set a proxy server to use while crawling.", 
	"This option is used in case you want to use a proxy server\n"
	+ "	Example: -p=www.proxyserver.com\n"),
red("-r", "-redirect", "This will set " + ThisProgram.name + " to accept 301 redirects so they can be crawled."),
rob("-rob", "-robot", "This will set " + ThisProgram.name + " to respect a robots.txt policy if the website has one.", 
	"This will read the robots.txt file of a webserver and will not crawl any file listed in it\n"),
t("-t", "-samp", "This will use the test option from a sample file called " + FileNames.samphtml + ".", 
	"This option is used to test this program with a sample file placed in the working directroy.\n"
	+ "It can be used in place of the url parameter and must be the last option in the command line.\n"
	+ "	Example: " + ThisProgram.name + " -n=1 -samp or " + ThisProgram.name + " -n=1 -t\n"
	+ "This is use a custom sample file you may want to use instead of the standard one.\n"
	+ "It is used just like the -t option except you have to name a custom html file.\n"
	+ "	Example:  -samp=your/sample.html\n"),
s("-s", "-spider", "This will tell you about the different crawl methods available.", 
		"This option is used for the different crawl methods that are available to this program.\n"
		+ "Method 1 is the basic web spider of this program.  It spiders links in the order that it finds them.\n"
		+ "Method 2 is meant to be a website overload tool and not to be used by nice people.\n"
		/*+ "Method 3 is a standard spider that stops between each crawl.\n"*/
		+ "Method 4 will place long links at the top to be crawled first to get subdirectories first.\n"
		+ "Method 5 randomizes the order of the links so that it doesn't spiders them in the order that they are found.\n"
		+ "Method 6 searches links that are not a not from the same domain first.\n"
		//+ "Method 7 crawls redirected links as they are found.\n"
		+ "Method 8 sorts data in purley alpha-numeric order and is faster than other methods\n"),
site("-site", "-sitemap", "This is used to set the crawler to read sitemap links."),
skp("-skp", "-skip", "This is to tell the crawler to skip rather than remove non-OK urls from database as it crawls", 
		"The default behavior of this crawler is to remove links from the data that give a not OK HTTP response from the server.\n" 
		+ "Turning on this option tells the crawler to skip these versus removing them from the data as it collects it.\n"
		+ "This works identically to " + Help.nok.parameter1 + " !\n"),
u("-u"),
v("-v"),
w("-w", "-wait", "This will slow your crawler down so you don't overload a website.", 
	"This will slow your crawler down between page crawls so you don't inadvertanly overload webservers!\n"
	+ "	Example: -w=889 will wait 889 milliseconds between each cycle.\n"), 
x("-x", "-extra", "This will let you pass in extra options for " + ThisProgram.name + ".", "This option is yet to be implemented\n"),
y("-y"),
z("-z");

public String parameter1;
public String parameter2;
String message =  "";
String submessage = "";

	private Help(String p1) {
	parameter1 = p1;
	}

	private Help(String p1, String p2, String m) {
	this(p1);
	parameter2 = p2;
	message = m;
	}

	private Help(String p1, String p2, String m, String s) {
	this(p1, p2, m);
	submessage = s;
	}

}


