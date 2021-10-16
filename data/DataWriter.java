package drudge.data;

import java.io.*;
import java.util.*;
import java.net.*;
import drudge.page.*;
import drudge.*;
import drudge.global.*;

/*This class is for the data storage of the links it finds using streams
 *I thought it was clever to write up
 */
abstract class DataWriter<T> implements Data<T> {
PrintWriter PRINTER;
LineNumberReader READER;

	//this has to be filled with something in order for subclass works.
	abstract LineNumberReader createReader();
	abstract PrintWriter createWriter();

	public void put(T page) throws DuplicateURLException {
	Debug.print(page);
	PRINTER.println(page.toString());
	PRINTER.close();
	}

	public T get(int n) {
	T entry = null;
		try {
		READER.reset();//ensures that it is resetted
			for (String line = READER.readLine(); line != null; line = READER.readLine()) {
				if (READER.getLineNumber() == n + 1) {//index number has to be increased by one because line nmbers are 1 based
				entry = D.getPageFromEntry(line);
				Debug.print(entry);
				break;
				}
			}
		READER.close();
		}
		catch (IOException I) {
		Debug.here(I);
		D.error(I);
		}
	return entry;
	}


	public T remove(int l) {
	T p = null;
		try {
		READER.reset();
			for (String line = READER.readLine(); line != null; line = READER.readLine()) {
				if (READER.getLineNumber() != l + 1) {
				PRINTER.write(line);	
				}
				else {
				p = D.getPageFromEntry(line);			
				}
			} 
		//PRINTER.close();//this should write to new buffer
		//READER.close();
		}
		catch (IOException I) {
		D.error(I);
		}
	return p;
	}

	
}

