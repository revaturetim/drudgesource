package drudge.data;

import java.io.*;
import java.net.*;
import java.util.*;
import drudge.page.Page;
import drudge.Debug;
import drudge.global.*;

public class DataObject extends AbstractData {
private File dir;

	public DataObject(String s) {
	source = s;
	dir = new File(s);
	dir.mkdir();
	}
 
 	//this seems to make it faster vs using the default size method in abstractdata
 	public int size() {
	return dir.list().length;
 	}
	
	public Object remove(int cycle) {
	Object obj = get(cycle);
	File d = new File(dir, String.valueOf(obj.hashCode()));
	d.delete();	
	return obj;
	}
	
	public Object get(int cycle) {
	File[] files = dir.listFiles();
	Object obj = null;
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
		obj = in.readObject();
		//in.readFields();
		in.close();
		}
		catch (IOException I) {
		D.error(I);
		}
		catch (ClassNotFoundException C) {
		D.error(C);
		}
		catch (IndexOutOfBoundsException I) {
		D.error(I);
		}
	return obj;
	}
	
	public void put(Object obj) {
	Page p = (Page)obj;
	File f = new File(dir, String.valueOf(p.hashCode()));
	
		if (contains(f) == false) {
			try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(p);
			//out.writeFields();
			out.close();
			}
			catch (IOException I) {
			D.error(I);
			}
		}
		else {
		Debug.here("found a matching file");
		}
	}
	
	public boolean contains(Object obj) {
	boolean has = false;
		if (obj instanceof Page) {
		Page p = (Page)obj;
		File f = new File(dir, String.valueOf(p.hashCode()));
		has = f.exists();
		}
		else if (obj instanceof File) {
		File f = (File)obj;
		has = f.exists();
		}	
	return has;
	}
	
	public void begin() throws Exception {
		
	}
	
	public void end() throws Exception {
	final BufferedWriter link_writer = new BufferedWriter(new FileWriter(FileNames.links));
	
		for (File file : dir.listFiles()) {
			try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			Page page = (Page)in.readObject();
			D.writeEntry(page, link_writer, level());
			in.close();
			}
			catch (IOException I) {
			D.error(I);
			}
		}
	link_writer.close();
	}
	
	public void checkError() {
	}
	
	public void truncate() {
	File[] files = dir.listFiles(); 
		for (File f : files) {
		f.delete();
		}
	
	}

}
