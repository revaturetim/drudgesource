package drudge;

import drudge.global.*;
import drudge.data.UselessMessages;

enum Help {
help("-help", "<--------------Help Options-------------->"),
about("-about", "About this program."),
a("-a", about.message),
b("-b", ""),
c("-c", "This will tell you about the different crawl methods available."),
d("-d", "This wil let you store different amounts of data from the webpages."),
e("-e", "This will let you collect emails from the webpages you crawl through."),
exc("-exc", "This will set " + ThisProgram.name + " to exclude pages from certain websites."),
exclude("-exlcude", exc.message),
f("-f", ""),
g("-g", ""),
h("-h", "This will print out the help message."),
i("-i", "This will let you store program options in a file called " + FileNames.in + "."),
inc("-inc", "This will set " + ThisProgram.name + " to only include page from certain websites."),
include("-include", inc.message),
j("-j", ""),
k("-k", ""),
l("-l", "This will list the license agreement for " + ThisProgram.name + "."),
m("-m", "This will show you the different output messages that it can show you while crawling."),
n("-n", "This will set the maximum number of cycles."),
nok("-nok", "This will set " + ThisProgram.name + " to accept all webites for faster crawling."),
o("-o", "This will write the output to a file called " + FileNames.out),
p("-p", "This will let you set a proxy server to use while crawling."),
r("-rob", "This will set " + ThisProgram.name + " to respect a robots.txt policy if the website has one."),
samp("-samp", "This will use the test option from a sample file called " + FileNames.samphtml + "."),
s("-s", "This will set from what cycle to continue from."),
t("-t", samp.message),
u("-u", ""),
v("-v", ""),
w("-w", "This will slow your crawler down so you don't overload a website."), 
x("-x", "This will let you pass in extra options for " + ThisProgram.name + "."),
y("-y", ""),
z("-z", "");

String parameter;
String message;

	private Help(String p, String m) {
	parameter = p;
	message = m;
	}

}


