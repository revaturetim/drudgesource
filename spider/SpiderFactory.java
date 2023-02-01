package drudge.spider;

import drudge.page.*;
import drudge.*;

public class SpiderFactory {




	static public Spider create(int c) {
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
			break;
		}
		
	Debug.time("Create Spider Object");
	return spider;
	}

}
	

