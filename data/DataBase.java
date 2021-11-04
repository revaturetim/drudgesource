package drudge.data;

import java.net.*;
import java.io.*;
import java.sql.*;
import drudge.page.*;
import drudge.*;

/*This uses MySQL as its query language*/
public class DataBase<T> implements Data<T> {
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

	public int size() {
	int size = 0;
	final String query = "SELECT count(link) FROM " + table;
		try {
		ResultSet result = connect(query);
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
		ResultSet result = connect(query);

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
	final String query = "SELECT * FROM " + table;
		try {
		ResultSet result = connect(query);
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
		if (Page.data > 1) {
		pagetitle = p.getTitle().trim();//for some odd reason this needs .trim in there to make it work
		}
		if (Page.data > 2) {
		pagekeywords = p.getKeywords().rawString().trim();
		}

	final String putquery = "INSERT INTO " + table + " values ('" + pagelink + "', '" + pagetitle + "', '" + pagekeywords + "')";
		try {
			if (contains(page) == false) {
			connect(putquery);
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

	private ResultSet connect(String query) throws SQLException {
	Connection con = DriverManager.getConnection(dburl);
	Statement state = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	ResultSet result = state.executeQuery(query);
	return result;
	}

	public String source() {
	return dburl;
	}

}
