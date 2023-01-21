package drudge.global;

import drudge.data.*;
import drudge.page.*;
import java.net.URL;
import java.util.*;

public enum DataEnum {
links(new DataList<Page>(FileNames.links)), 
emails(new DataListEmail<URL>(FileNames.emails)),
exclude(new DataListExclude<URL>(FileNames.exclude)),
include(new DataListExclude<URL>(FileNames.include)),
words(new DataListExcludeWords<String>(FileNames.words)),
images(new DataListImages<URL>(FileNames.images));

public Data data;

	private DataEnum(Data d) {
	data = d;
	}
	
	public static void truncateAll() {
		for (DataEnum thing : values()) {
		thing.data.truncate();
		}
	}

	public static void beginAll() {
		for (DataEnum thing : values()) {
			try {
			thing.data.begin();
			}
			catch (Exception E) {
			D.error(E.getClass(), E);
			}
		}
	}

	public static void endAll() {
		for (DataEnum thing : values()) {
			try {
			thing.data.end();
			}
			catch (Exception E) {
			D.error(E.getClass(), E);
			}
		}
	}

	public static void checkErrorAll() {

		for (DataEnum thing : values()) {
		thing.data.checkError();
		}			
	}

/*THIS IS FOR NOROBOT OBJECTS*/
final public static Hashtable<URL, Data<URL>> norobots = new Hashtable<URL, Data<URL>>();

/*This is a record of past objects I have tried and I keep them in case I want to use them again*/
/*THIS IS FOR DADA OBJECT*/
//final public static Data<Page> dada = (Data<Page>)new DataBase<Page>("jdbc:default:/home/time/java/db;");
//final public static Data<Page> dada = (Data<Page>)new DataBase<Page>("jdbc:MySQL://localhost/home/time/java/db;");
//final public static Data<Page> dada = (Data<Page>)new DataBase<Page>("jdbc:derby:/home/tim/java/db");
//final public static Data<Page> dada = (Data<Page>)new DataBase<Page>("jdbc", "derby", "/home/tim/java/db", ";create=true");
//final public static Data<Page> dada = (Data<Page>)new DataObject("files");
//final public static Data<Page> dada = (Data<Page>)new DataFileWriter<Page>(FileNames.links);
//final public static Data<Page> dada = (Data<Page>)new DataStringWriter<Page>();
//final public static Data<Page> dada = (Data<Page>)new DataStringBuilder<Page>();

/*THIS IS FOR DADA_EMAILS OBJECT*/
//final public static Data<URL> dada_emails = (Data<URL>)new DataList<URL>();

/*THIS IS FOR EXCLUDE/INCLUDE OBJECTS*/


/*THIS IS FOR ALL OTHER OBJECTS MISCILANIOUS*/

/*This is for image urls*/


}
