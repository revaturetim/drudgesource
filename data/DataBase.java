package drudge.data;

import java.net.*;
import java.io.*;
import java.sql.*;
import drudge.page.*;
import drudge.*;

/*This uses MySQL as its query language*/
public class DataBase extends AbstractData {
private final String table = "drudge";
/*data column headers will be url, title, keywords*/
private final String link = "link";
private final String title = "title";
private final String words = "keywords";
//private final String query = "select " + link + " from " + table;
/* these are some basic sql commands that I use for this class
create table drudge (link varchar(100) not null primary key, title varchar(25), keywords varchar(255));    
insert into drudge values (link, title, keywords);
*/
	public DataBase() {

	}

	public DataBase(String d) {
	source = d;
	}

	public DataBase(String p, String d, String u) {
	source = p + ":" + d + ":" + u;	
	}
	
	public DataBase(String p, String d, String u, String e) {
	this(p, d, u);
	source = source + e;
	}
	
	public int size() {
	int size = 0;
	final String query = "SELECT count(link) FROM " + table;
		try {
		ResultSet result = query(query);
		size = result.getInt(1);
		}
		catch (SQLException S) {
		D.error(S);
		}
	return size;	
	}

	@SuppressWarnings("unchecked")	
	public Object get(final int i) {
	Object page = null;
	final String query = "SELECT " + link + " FROM " + table;
		try {
		ResultSet result = query(query);

			if (result.absolute(i + 1)) {
			String s = result.getString(link);
			page = PageFactory.createFromString(s);
			}
		}
		catch (SQLException S) {
		Print.row(S, page);
		}
	return page; 
	}

	public Object remove(int i) {
	Object page = null;	
	final String query = "SELECT * FROM " + table;
		try {
		ResultSet result = query(query);
			if (result.absolute(i + 1)) {
			result.deleteRow();
			}
		}
		catch (SQLException S) {
		Print.row(S, page);
		}
	return page;
	}

	public void put(Object page) {
	final Page p = (Page)page;
	final String pagelink = p.toString();
	String pagetitle = "null";
	String pagekeywords = "null";
	
		if (level() > 1) {
		pagetitle = p.getTitle().trim();//for some odd reason this needs .trim in there to make it work
		}
		if (level() > 2) {
		pagekeywords = D.rawString(p.getKeywords()).trim();
		}

	
		try {
			if (contains(page) == false) {
			final String insertquery = "INSERT INTO " + 
			table + " values ('" + pagelink + "', '" + pagetitle + "', '" + pagekeywords + "')";
			query(insertquery);
			}
			else {
			final String updatequery = "UPDATE " + table + " SET " + link + "=" + pagelink + ", " +
			title + "=" + pagetitle + ", " + words + "=" + pagekeywords + 
			"WHERE " + link + "=" + pagelink;
			query(updatequery);
			}
		}
		catch (SQLException S) {
		Print.row(S, page);
		}
	}
	
	/*this should always return false since only unique entries are allowed--no repeats possible*/
	public boolean contains(Object page) {
	boolean exist = false;
	final String pagelink = page.toString();
	final String query = "SELECT count(" + link + ") FROM " + table + " WHERE " + link + "=" + pagelink;
		try {
		ResultSet result = query(query);
		int resultcount = result.getInt(link);
			if (resultcount > 0) {
			exist = true;
			}
		}
		catch (SQLException S) {
		Print.row(S, page);
		}
	return exist;
	}

	private ResultSet query(String query) throws SQLException {
	Connection con = DriverManager.getConnection(source());
	Statement state = con.createStatement();
	ResultSet result = state.executeQuery(query);
	return result;
	}
	
	public void truncate() {
	final String query = "DELETE FROM " + table;
		try {
		ResultSet result = query(query);
		}
		catch (SQLException S) {
		D.error(S);
		}
	}
	
	public boolean check() {
	return false;
	}
	
	public boolean checkError() {
	System.out.println("Checking " + source() + " file for errors.");
	boolean duplicate = false;
	boolean linkerror = false;
	
	
		try { 
		final String query = "SELECT * FROM " + table;
		final ResultSet result = query(query);
		final ResultSetMetaData metadata = result.getMetaData();
		final int width = metadata.getColumnCount();
		int linecount = 0;
		
			while (result.next()) {
			linecount = result.getRow();
				/*this will check to make sure all columns have values*/
				for (int i = 1; i <= width; i++) {
				
					if (result.getString(i) == null) {
					System.out.println(
					"...There is a column length error at line " + String.valueOf(linecount));
					}
				}
			
			/*This will check for duplicates in the data*/
			String pagelink = result.getString(link);
				/*this will check for decoding errors*/
				if (pagelink.contains("%")) {
				System.out.println("...Possible encoding error at line " + String.valueOf(linecount));
				}
				
				try {
				URL url = new URL(pagelink);//this throws malformedurlexception
				Page p = new Page(url);//this throws IOException
				p.isValid();//this throws InvalidURLException, URISyntaxException
				URL u = p.getURL();
					if (u.getAuthority() == null) {
					throw new MalformedURLException("No Host");
					}
				final String query2 = 
				"SELECT COUNT(" + link + ") FROM " + table + " WHERE " + link + " = " + pagelink;
				ResultSet result2 = query(query2);
				int duplicatecount = result2.getInt(link);
					if (duplicatecount > 1) {
					duplicate = true;
					System.out.print(
					"...There are " + String.valueOf(duplicatecount) + " duplicates of " + pagelink);
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
				catch (IOException I) {
				linkerror = true;
				System.out.print("...Line " + String.valueOf(linecount) + " has a link that isn't quite right: ");
				System.out.println(link);
				}

			} 
		System.out.println("...The number of lines in " + source() +  " is "  + linecount + ".");
		result.close();
		}
		catch (SQLException S) {
		D.error(S);
		}
	return (duplicate || linkerror);
	}
	
	public void begin() throws Exception {
	
	}
	
	public void end() throws Exception {
	
	}

}
