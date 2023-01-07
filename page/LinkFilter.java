package drudge.page;

import drudge.data.*;
import java.io.*;
import java.net.*;

//this is a functional interface used to filter links as they get them
public interface LinkFilter<T> {
	
	public void act(T u);
}

