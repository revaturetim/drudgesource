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
		
		if (c > -1) {	
		DataOutputStream out = new DataOutputStream(new FileOutputStream(FileNames.cycle));
		out.writeInt(c);
		out.close();
		}
		else {
		D.error("Location", "Countfile(int).set(int)", "The value of c was ", String.valueOf(c));
		}
	}
	
}

