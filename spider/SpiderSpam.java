package drudge.spider;

import drudge.data.*;
import drudge.page.*;
import drudge.global.*;
import drudge.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class SpiderSpam extends Spider {

	public SpiderSpam() {
	}

	public int crawl(int max) {
	System.out.println("WARNING: You are going to the dark side of this program!");
	System.out.print("This is overloading website.  Are you sure you want to continue? Y|N ");
	Scanner scan = new Scanner(System.in);
	String answer = scan.next();
		if (answer.equalsIgnoreCase("Y")) {
		final Page victim = (Page)DataEnum.links.data.select(DataEnum.links.data.firstIndexNumber());
			while (true) {
			System.out.print("How many times do you want to spam " + victim.toString() + "? ");
			final int howmanytimes = scan.nextInt();
				for (int i = 0; i < howmanytimes; i++) {
				this.links(victim);
				victim.printRow();
				}
			System.out.println("Do you wish to do that again? Y|N " );
			String repeat = scan.next();
				if (repeat.equalsIgnoreCase("N")) {
				System.out.println("Goodbye");
				break;
				}
			}
		}
		else if (answer.equalsIgnoreCase("N")) {
		System.out.println("That is good to know.  Goodbye.");
		}
	return 0;
	}
	
	public void setCheckOK(boolean b) {
	//garantees that it will never check for oks
	}

	public void setDelay(long l) {
	delay = l;
	}

	public void setNoRobotsAllowed(boolean b) {
	//garantees that it will never respect robot.txt policy
	}
	
}
