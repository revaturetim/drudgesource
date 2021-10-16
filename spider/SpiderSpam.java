package drudge.spider;

import drudge.data.*;
import drudge.page.*;
import drudge.*;
import java.net.*;

public class SpiderSpam extends Spider {
private int max = 1;

	public SpiderSpam(int m) {
	max = m;
	}

	public boolean crawl(Page p) {
		for (int i = 0; i < max; i++) {
		super.crawl(p);
		System.out.print(".");
		}
	System.out.println();
	return false;
	}
	
	public void setCheckOK(boolean b) {
	checkok = false;
	//garantees that it will never check for oks
	}
	
	public void setRobotsAllowed(boolean b) {
	norobots = false;
	//garantees that it will never respect robot.txt policy
	}
	public void setMax(int m) {
	max = m;
	}

}
