package drudge.data;

import java.io.*;
import java.util.*;
import drudge.global.FileNames;

public interface CountFile {
final static public String sep = ", ";

	static int get() throws IOException {
	DataInputStream in = new DataInputStream(new FileInputStream(FileNames.cycle));
	int line = in.readInt();
	in.close();
	return line;
	}

	static void set(int c) throws IOException {
		
		if (c > 0) {	
		DataOutputStream out = new DataOutputStream(new FileOutputStream(FileNames.cycle));
		out.writeInt(c);
		out.close();
		}
		else {
		Hashtable<String, Object> t = new Hashtable<String, Object>();
		t.put("Location", "Connector.Countfile(int)");
		t.put("The value of c was ", String.valueOf(c));
		D.error(t);
		}
	}
	
}

