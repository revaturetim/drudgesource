package drudge.data;

import java.io.*;

public interface Connect<T> {

	public boolean check() throws Exception;
	public boolean checkError() throws Exception;
	public boolean connect() throws Exception;
	public boolean disconnect() throws Exception;
	public String source();
}
