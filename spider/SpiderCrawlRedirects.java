package drudge.spider;

import drudge.data.*;
import drudge.*;
import drudge.global.*;
import drudge.page.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class SpiderCrawlRedirects extends Spider {

	protected void redirect(Page p) {
	links(p);//this crawls the first page it finds
	super.redirect(p);
	}
}
