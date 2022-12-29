package drudge.page;

import drudge.data.*;
import java.io.*;
import java.net.*;


interface LinkAction<T> {
	//this throws a more general uselessurlexception so it can catch all uselessurlexceptions
	public void act(T u) throws UselessURLException, URISyntaxException, MalformedURLException, IOException;
}

