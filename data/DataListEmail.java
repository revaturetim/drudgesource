package drudge.data;

import java.io.*;
import java.net.*;
import drudge.Debug;

public class DataListEmail extends DataListExclude {

	public DataListEmail() {
	super();
	}
	
	public DataListEmail(String s) {
	super(s);
	}

	public void end() throws IOException {
	final BufferedWriter email_writer = new BufferedWriter(new FileWriter(source()));
		for (Object line : this) {
		email_writer.append(line.toString());
		email_writer.append("\n");
		}
	email_writer.close();
	}
	
	public boolean checkError() {
	D.writeBeginningResponse(source());
	boolean duplicate = false;
	boolean linkerror = false;
		try { 
		final LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(source())));
			for (String email1 = reader.readLine(); email1 != null; email1 = reader.readLine()) {
				try {
				URI u = new URI(email1);
					if (u.isOpaque()) {
					/*This will check for duplicates in the data*/
					final LineNumberReader reader2 = new LineNumberReader(new BufferedReader(new FileReader(source())));
						for (String email2 = reader2.readLine(); reader2.getLineNumber() < reader.getLineNumber(); email2= reader2.readLine())  {
							if (email1.equals(email2)) {
							duplicate = true;
							D.writeDuplicateResponse(String.valueOf(reader.getLineNumber()), email1, email2);	
							}
						}
					}
					else {
					D.writeResponse(String.valueOf(reader.getLineNumber()), email1, "Not Opaque");
					}
				}
				catch (URISyntaxException U) {
				linkerror = true;
				D.writeResponse(String.valueOf(reader.getLineNumber()), email1, U.toString());
				}
			}
		D.writeFinalResponse(reader.getLineNumber());
		}
		catch (FileNotFoundException F) {
		
		}
		catch (IOException I) {
		System.out.println(I);
		}
	return (duplicate || linkerror);
	}


}
