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
protected String source;
private int datalevel = 1;
private boolean include = false;
private boolean exclude = false;

	//this has to be filled with something in order for subclass works.
	abstract LineNumberReader createReader();
	abstract PrintWriter createWriter();
	
	@SuppressWarnings("unchecked")
	protected T getPageFromEntry(String line) {
	T page = null;
		try {
		String[] entries = line.split(CountFile.sep);
		page = (T)new Page(entries[0]);
		}
		catch (MalformedURLException M) {
		D.error(M);
		}
	return page;
	}
	
	protected void writePageEntry(Page page) throws IOException {
	Debug.check(page, null);
	BufferedWriter link_writer = new BufferedWriter(new FileWriter(source()));
		for (int r = 0; r < level(); r++) {
			if (r == 0) {
			link_writer.append(page.toString() + CountFile.sep);
			}
			else if (r == 1) {
			link_writer.append(page.getTitle() + CountFile.sep);
			}
			else if (r == 2) {
			link_writer.append(page.getKeywords().rawString());
			}
		}
	link_writer.append("\n");
	link_writer.close();
	}
	
	public void put(T link) throws DuplicateURLException, ExcludedURLException, InvalidURLException, URISyntaxException {
	((Page)link).isValid();
		if (excluded() == true && DataObjects.exclude.contains(link)) throw new ExcludedURLException(link);
		if (included() == true) {
			for (Page p : DataObjects.include) {
			final String host1 = p.getURL().getHost();
			final String host2 = ((Page)link).getURL().getHost();
				if (host1.equals(host2) == false) {
				throw new ExcludedURLException(link);
				}
			}		
		} 
		if (add(link) == false) throw new DuplicateURLException(link);	
	}


	public T get(final int n) {
	T entry = null;
		try {
		LineNumberReader READER = createReader();
		READER.setLineNumber(-1);
			for (String line = READER.readLine(); line != null; line = READER.readLine()) {
			final int c = READER.getLineNumber();
				if (c == n) {
				entry = this.getPageFromEntry(line);
				break;
				}
			}
		READER.close();
		}
		catch (IOException I) {
		D.error(I);
		}
	return entry;
	}


	public T delete(final int l) {
	T p = null;
	StringBuffer buff = new StringBuffer();
		try {
		LineNumberReader reader = createReader();
		reader.setLineNumber(-1);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (reader.getLineNumber() != l) {
				buff.append(line);
				buff.append("\n");
				}
				else {
				p = this.getPageFromEntry(line);			
				}
			} 
		reader.close();
		}
		catch (IOException I) {
		D.error(I);
		}
		try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(source()));
		writer.append(buff);
		writer.close();
		}
		catch (IOException I) {
		D.error(I);
		}
	return p;
	}
	
	public String source() {
	return source;
	}

	public void setLevel(int l) {
	datalevel = l;
	}

	public int level() {
	return datalevel;
	}

	public void begin() {

	}

	public void finish() {

	}
	
	public void setExcluded(boolean b) {
	
	}
	
	public boolean excluded() {
	return exclude;
	}
	
	public boolean add(T obj) {
	return false;
	}
	
	public void setIncluded(boolean b) {
	include = b;
	}
	
	public boolean included() {
	return include;
	}

	
}

