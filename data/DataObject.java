package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;
import drudge.page.Page;
import drudge.Debug;
import drudge.global.*;

public class DataObject<T> extends AbstractData<T> {
private File dir;

	public DataObject(String s) {
	source = s;
	dir = new File(s);
	}
 
 	//this seems to make it faster vs using the default size method in abstractdata
 	public int size() {
	return dir.list().length;
 	}
	
	public T remove(int cycle) {
	T obj = get(cycle);
	File d = new File(dir, String.valueOf(obj.hashCode()));
	d.delete();	
	return obj;
	}
	
	public T get(int cycle) {
	File[] files = dir.listFiles();
	Debug.check(files, null);
	Arrays.sort	(files, (A, B) -> {	File a = (File)A; 
						File b = (File)B; 
						long al = a.lastModified(); 
						long bl = b.lastModified();
						if (al < bl) return -1;
						if (al > bl) return 1;
						else return 0;
						}
			);
		try {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(files[cycle]));
		Object obj = in.readObject();
		return (T)obj;
		}
		catch (IOException I) {
		D.error(I);
		return null;
		}
		catch (ClassNotFoundException C) {
		D.error(C);
		return null;
		}
		catch (IndexOutOfBoundsException I) {
		//D.error(I);
		return null;
		}
	}
	
	public boolean add(T obj) {
	Page p = (Page)obj;
		if (contains(obj) == false) {
			try {
			File f = new File(dir, String.valueOf(p.hashCode()));
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(p);
			return true;
			}
			catch (IOException I) {
			D.error(I);
			return false;
			}
		
		}
		else {
		return false;
		}
	}
	
	public boolean contains(Object obj) {
	String[] files = dir.list();
	boolean has = false;
		for (String f : files) {
			if (obj.toString().equals(f)) {
			has = true;
			break;
			}
		}
	return has;
	}
	
	public void begin() throws Exception {
	
	}
	
	public void end() throws Exception {
	final BufferedWriter link_writer = new BufferedWriter(new FileWriter(FileNames.links));
		for (T t : this) {
		Debug.check(t, null);
		Page page = (Page)t;
		D.writeEntry(page, link_writer, level());
		}
	link_writer.close();
	}
	
	public boolean checkError() {
	System.out.println("Checking " + FileNames.links + " file for errors.");
	boolean duplicate = false;
	boolean linkerror = false;
	int linecount = 0;
		try { 
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(FileNames.links)));
		int firstcolumnlength = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			linecount = reader.getLineNumber();
			String[] columns = line.split(CountFile.sep);
			/*This will see if the total number of columns for each line is the same as the previous one*/
				if (linecount == 1) {
				firstcolumnlength = columns.length;
				}
				if (columns.length != firstcolumnlength) {
				System.out.println("...There is a column length error at line " + String.valueOf(linecount));
				}
			/*this will check for decoding errors*/
			String link = columns[0];
				if (link.contains("%")) {
				System.out.println("...Possible encoding error at line " + String.valueOf(linecount));
				}
				
			/*This will check for duplicates in the data*/
				try {
				Page p = new Page(link);//this throws malformedurlexception
				p.isValid();//this throws InvalidURLException, URISyntaxException
				URL u = p.getURL();
					if (u.getAuthority() == null) {
					throw new MalformedURLException("No Host");
					}
				LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(FileNames.links)));
					for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine()) {
					int linecount2 = reader2.getLineNumber();
						if (linecount2 < linecount) {
						String[] columns2 = line2.split(CountFile.sep);
						String link2 = columns2[0];
							if (link.equals(link2)) {
							duplicate = true;
							System.out.print("..." + link + " is a duplicate entry found at ");
					       	System.out.println(String.valueOf(linecount) 
					       	+ " and " 
					       	+ String.valueOf(linecount2));
							}
						}
					}
				}
				catch (InvalidURLException N) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't an html file: ");
				System.out.println(link);
				}
				catch (URISyntaxException U) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
				catch (MalformedURLException M) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}
			}
		System.out.println("...The number of lines in " + FileNames.links +  " is "  + linecount + ".");
		}
		catch (IOException I) {
		D.error(I);
		}
	return (duplicate || linkerror);
	}
	
	public void truncate() {
	File[] files = dir.listFiles(); 
		for (File f : files) {
		f.delete();
		}
	
	}

}
