package drudge.global;

import drudge.data.*;
import drudge.page.*;
import java.net.URL;
import java.util.*;

public enum DataEnum {
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

/*THIS IS FOR NOROBOT OBJECTS*/
//final public static Data<URL> norobots = (Data<URL>)new DataList<URL>();
//final public static Hashtable<URL, Data<URL>> norobots = new Hashtable<URL, Data<URL>>();

/*THIS IS FOR ALL OTHER OBJECTS MISCILANIOUS*/

/*This is for image urls*/

links(new DataList<Page>(FileNames.links)), 
emails(new DataListEmail<URL>(FileNames.emails)),
exclude(new DataListExclude<URL>(FileNames.exclude)),
include(new DataListExclude<URL>(FileNames.include)),
words(new DataListExcludeWords<String>(FileNames.words)),
images(new DataListImages<URL>(FileNames.images));

Data<?> data;

	private DataEnum(Data<?> d) {
	data = d;
	}
	
	public static void truncateAll() {
		for (Data<?> d : this) {
		d.truncate();
		}
	}

	public static void beginAll() {
		for (Data<?> d : this) {
			try {
			d.begin();
			}
			catch (Exception E) {
			D.error(E.getClass(), E);
			}
		}
	}

	public static void endAll() {
		for (Data<?> d : this) {
			try {
			d.end();
			}
			catch (Exception E) {
			D.error(E.getClass(), E);
			}
		}
	}

}
