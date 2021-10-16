package drudge.data;

import java.net.*;
import java.io.*;
import java.sql.*;
import drudge.page.*;
import drudge.*;

public class DataBase<T> implements Data<T> {
private Statement state = null;
private Connection con = null;
private String dburl = "jdbc:derby:/home/tim/java/db";
private final String table = "drudge";
/*data column headers will be url, title, keywords*/
private final String link = "link";
private final String title = "title";
private final String words = "keywords";
private final String query = "select " + link + " from " + table;
/* these are some basic sql commands that I use for this class
create table drudge (link varchar(100) not null primary key, title varchar(25), keywords varchar(255));    
insert into drudge values (link, title, keywords);
*/
	public DataBase() {

	}
	
	public DataBase(String b) {
	dburl = b + ";create=true;";
	}

	public int size() {
	int size = 0;
		try {
		ResultSet result = state.executeQuery(query);
			if (result.last()) {
			size = result.getRow();
			}
		}
		catch (SQLException S) {
		Debug.println(S);
		//D.error(S);

		}
	return size;	
	}

	public T get(final int i) {
	T page = null;
		try {
		ResultSet result = state.executeQuery(query);

			if (result.absolute(i + 1)) {
			String s = result.getString(link);
				try {
				page = (T)new Page(s);
				}
				catch (UselessURLException U) {
				D.error(U);
				}
				catch (IOException I) {
				D.error(I);
				}
				catch (URISyntaxException U) {
				D.error(U);
				}					
			}
		}
		catch (SQLException S) {
		Print.printRow(S, page);
		//Debug.println(S);
		//D.error(S);
		}
	return page; 
	}

	public T remove(int i) {
	T page = null;	
		try {
		ResultSet result = state.executeQuery(query);
			if (result.absolute(i + 1)) {
			/*This method should return null and this should be commented out
			 * String s = result.getString(link);
				try {
				page = (T)new Page(s);
				}
				catch (UselessURLException U) {
				D.error(U);
				}
				catch (IOException I) {
				D.error(I);
				}
				catch (URISyntaxException U) {
				D.error(U);
				}*/
			result.deleteRow();
			}
		}
		catch (SQLException S) {
		Print.printRow(S, page);
		//Debug.println(S);
		//D.error(S);
		}
	return page;
	}

	public void put(T page) {
	final Page p = (Page)page;
	final String pagelink = p.toString();
	String pagetitle = "null";
	String pagekeywords = "null";
		if (Page.data > 1) {
		pagetitle = p.getTitle().trim();//for some odd reason this needs .trim in there to make it work
		}
		if (Page.data > 2) {
		pagekeywords = p.getKeywords().rawString().trim();
		}

	final String putquery = "INSERT INTO " + table + " values ('" + pagelink + "', '" + pagetitle + "', '" + pagekeywords + "')";
		try {
			if (contains(page) == false) {
			state.executeUpdate(putquery);
			}
		}
		catch (SQLException S) {
		Print.printRow(S, page);
		//Debug.println(S);
		//D.error(S);
		}
	}
	
	/*this should always return false since only unique entries are allowed--no repeats possible*/
	public boolean contains(Object page) {
	
	return false;
	}

	public boolean connect()  {
		try {
		con = DriverManager.getConnection(dburl);
		state = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return true;
		}
		catch (SQLException S) {
		Debug.print(S);
		//D.error(S);
		return false;
		}


	}

	public boolean disconnect() {
		try {
		con.close();
		return true;
		}
		catch (SQLException S) {
		Debug.print(S);
		//D.error(S);
		return false;
		}	
	}
	public String source() {
	return dburl;
	}

}
