package drudge.data;

import java.net.*;
import java.io.*;
import java.sql.*;
import drudge.page.*;
import drudge.*;

/*This uses MySQL as its query language*/
public class DataBase<T> implements Data<T> {
private int datalevel = 0;
private boolean exclude = false;
private boolean include = false;
private String dburl;
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
	dburl = d;
	}

	public DataBase(String p, String d, String u) {
	dburl = p + ":" + d + ":" + u;	
	}
	
	public DataBase(String p, String d, String u, String e) {
	this(p, d, u);
	dburl = dburl + e;
	}
	
	public boolean add(T obj) {
	return false;
	}
	
	public void setLevel(int l) {
	datalevel = l;
	}

	public int level() {
	return datalevel;
	}

	public int size() {
	int size = 0;
	final String query = "SELECT count(link) FROM " + table;
		try {
		ResultSet result = query(query);
		size = result.getInt(1);
		}
		catch (SQLException S) {
		Debug.println(S);
		//D.error(S);

		}
	return size;	
	}

	@SuppressWarnings("unchecked")	
	public T get(final int i) {
	T page = null;
	final String query = "SELECT " + link + " FROM " + table;
		try {
		ResultSet result = query(query);

			if (result.absolute(i + 1)) {
			String s = result.getString(link);
				try {
				page = (T)new Page(s);
				}
				catch (MalformedURLException U) {
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

	public T delete(int i) {
	T page = null;	
	final String query = "SELECT * FROM " + table;
		try {
		ResultSet result = query(query);
			if (result.absolute(i + 1)) {
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
		if (level() > 1) {
		pagetitle = p.getTitle().trim();//for some odd reason this needs .trim in there to make it work
		}
		if (level() > 2) {
		pagekeywords = p.getKeywords().rawString().trim();
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
		Print.printRow(S, page);
		//Debug.println(S);
		//D.error(S);
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
		Print.printRow(S, page);
		}
	return exist;
	}

	private ResultSet query(String query) throws SQLException {
	Connection con = DriverManager.getConnection(dburl);
	Statement state = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	ResultSet result = state.executeQuery(query);
	return result;
	}

	public boolean check() {
	return true;
	}
	
	public boolean checkError() {
	return true;
	}
	
	public String source() {
	return dburl;
	}

	public void begin() {

	}

	public void finish() {

	}
	
	public void setExcluded(boolean b) {
	exclude = b;
	}
	
	public boolean excluded() {
	return exclude;
	}
	
	public void setIncluded(boolean b) {
	include = b;
	}
	
	public boolean included() {
	return include;
	}
	

}
