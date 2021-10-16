package drudge.page;

import java.util.regex.*;

class Patterns {
private final static String legalurichar = "[\\w_\\-!.~\\[\\]'()*?@%,;:$&+=/]";
private final static String legaluricharnoslash = "[\\w_\\-!.~\\[\\]'()*?@%,;:$&+=]";
private final static String legalurichars = legalurichar + "{1,}";
private final static String legaluricharsnoslash = legaluricharnoslash + "{1,}";
private final static String legaluricharsandspace = "\\s{0,}" + legalurichars + "\\s{0,}";
private final static String mode = "(?is)";
private final static String endofuri = "/(" + legaluricharsandspace + "\\.html)|([\\w_\\-!~\\[\\]'()*?@%,;:$&+=/]{0,})";

/*This is for all type of links*/
private final static String arefregex = "(<a.{1,}?href=\"" + legaluricharsandspace + "\".{0,}?>)";
private final static String ifrmregex = "(<iframe.{1,}?src=\"" + legaluricharsandspace + "\".{0,}?>)";
private final static String frmeregex = "(<frame.{1,}?src=\"" + legaluricharsandspace + "\".{0,}?>)";
private final static String formregex = "(<form.{1,}?action=\"" + legaluricharsandspace + "\".{0,}?>)";
private final static String imapregex = "(<area.{1,}?href=\"" + legaluricharsandspace + "\".{0,}?>)";
private final static String linkregex = arefregex + "|" + ifrmregex + "|" + frmeregex + "|" + formregex + "|" + imapregex;

/*this is for the inside of elements*/
private final static String quoteregex = "((href=)|(src=)|(action=))\"" + legaluricharsandspace + "\"";
private final static String qo2qoregex = "\"{1}" + legaluricharsandspace + "\"{1}";
private final static String qo2slregex =  "(http[s]{0,1}://|)" + legaluricharsnoslash + "[/]{0,1}";

/*this is for title*/
final static String titleregex = "(<(title)[^>]{0,}>)" + "[^<]{0,}" + "(<(/title)[^>]{0,}>)";
private final static String headregex = "(<head>).*(</head>)";
private final static String intraregex = ">" + "[^<]{0,}" + "<";

/*these are for replacing keywords from html files*/
private final static String scriptregex = "<[^>]{0,}script[^>]{0,}>[^<]{0,}<[^/]{0,}/[^s]{0,}script[^>]{0,}>";
private final static String htmlregex = "</{0,1}[^>]{1,}>";
private final static String punctregex = "\\p{Punct}";
private final static String singleregex = "\\s\\p{Alpha}{1,2}\\s";
private final static String digitregex = "\\s\\p{Digit}{1,}\\s";
private final static String spaceregex = "[\t\n\\x0B\f\r]";
private final static String mspaceregex = "[ ]{2,}";
private final static String unimpregex =  "\\p{Space}((and)|(also)|(all)|(where)|(when)|(who)|(how)|(the)|(from)|(more)|(her)|(him)|(for)|(not)|(that)|(she))\\p{Space}";


	enum Keywords {
	SCRIPT(scriptregex), 
	HTML(htmlregex),
	PUNCT(punctregex),
	SINGLE(singleregex),
	DIGIT(digitregex),
	SPACE(spaceregex),
	UNIMP(unimpregex),
	MSPACE(mspaceregex);

	private Pattern p;

		private Keywords(String regex) {
		p = Pattern.compile(mode + regex);
		}
	
		String replace(CharSequence text, String replace) {
		Matcher matcher = p.matcher(text);
		String ntext = "";
			while (matcher.find()) {
			ntext = matcher.replaceFirst(replace);
			matcher.reset(ntext);
		
			}	
		return ntext;
		}

	}

	enum Links {
	LINK(linkregex),
	QUOTE(quoteregex),
	Q2Q(qo2qoregex),
	Q2SL(qo2slregex);

	private Pattern p;

		private Links(String regex) {
		p = Pattern.compile(mode + regex);	
		}

		Matcher match(CharSequence seq) {
		return p.matcher(seq);
		}

	}

	enum Title {
	TITLE(titleregex),
	INTRA(intraregex);

	private Pattern p;
	
		private Title(String regex) {
		p = Pattern.compile(mode + regex);
		}

		Matcher match(CharSequence seq) {
		return p.matcher(seq);
		}

	}
}
