package drudge;

import java.io.*;
import java.util.function.Predicate;

final public class Debug {
private static long clickbegin = 0;
private static long clickend = 0;
private static boolean on = true;
private static boolean begstopwatch = false;
private static long cycletimebeg = 0;
private static long cycletimeend = 0;
public static boolean cycletimeon = false;
private static boolean begcycletime = false;
final private static String sep = " | ";

	public static void stop(Object obj) {
		if (on) {
			try {
			InputStream in = System.in;
			System.out.print("Stopped: " + obj.toString());
			byte[] b = new byte[1];
			in.read(b);//this blocks until there is something to read
			}
			catch (IOException I) {
			System.out.println("Stop method broke");
			}
		}
	}

	public static void stop() {
		if (on) {
		stop("");//this will call the stop method as it is suppose to
		}
	}
		
	public static void print(Object...objs) {
		if (on) {
			for (Object obj : objs) {
				if (obj != null) {
				Class<? extends Object> c = obj.getClass();
				System.out.printf("%10s=%-10s" + sep, c.getName(), obj.toString());
				}
				else {
				System.out.printf("%10s=%-10s" + sep, "Null", "Null");
				}
			}
		}
	}
	
	public static void println(Object...objs) {
		if (on) {
			for (Object obj : objs) {
				if (obj != null) {
				Class<? extends Object> c = obj.getClass();
				System.out.printf("%10s=%-10s\n", c.getName(), obj.toString());
				}
				else {
				System.out.printf("%10s=%-10s\n", "Null", "Null");
				}
			}
		}
	}
	
	public static <T> void check(final Predicate<T> P, final T obj, final String msg, final Class c) throws IllegalArgumentException {
		if (on) {
			if (!P.test(obj)) {//failed test
				if (c != null) {
				println("Failed Check Inside of " + c.getName());
				}
			throw new IllegalArgumentException(msg);
			}
		}
	}

	public static <T> void check(final Predicate<T> P, final T obj, final String msg) throws IllegalArgumentException {
		check(P, obj, msg, null);
	}

	public static <T> void check(Predicate<T> P, final T obj) throws IllegalArgumentException {
		check(P, obj, obj.toString(), null);
	}

	public static void check(final Object a, final Object b, final String msg, final Class c) throws IllegalArgumentException {
	
		if (on) {
			if (a != null && b != null) {
				if (a.equals(b) == false) {
				throw new IllegalArgumentException(msg);
				}
			}
			else if (a == null && b == null) {
				throw new NullPointerException(msg);
			}
		}	
	}
	
	public static void check(final Object a, final Object b, final String msg) throws IllegalArgumentException {
	
		if (on) {
			if (a != null && b != null) {
				if (a.equals(b) == false) {
				throw new IllegalArgumentException(msg);
				}
			}
			else if (a == null && b == null) {
				throw new NullPointerException(msg);
			}

		}	
	}
	
	public static void check(final Object a, final Object b) throws IllegalArgumentException {
		check(a, b, "");
	}

	
	
	public static void between(final int a, final int b, final int e, final String msg) throws IndexOutOfBoundsException {
		if (on) {
			if (a < b || a > e) {
			throw new IndexOutOfBoundsException(String.valueOf(a) 
			+ " was not between " 
			+ String.valueOf(b) 
			+ " and " + String.valueOf(e)
			+ ". \n" + msg);
			}
		}		
	}
	
	public static void between(final int a, final int b, final int e) throws IndexOutOfBoundsException {
		between(a, b, e, "");
	}

	public static void here() {
	here("here");
	}

	public static void here(String m) {
		if (on) {
		System.out.printf("%10s=%S\n", "Location", m);
		}
	}

	public static void here(Object obj) {
	here(obj.toString());
	}	

	public static void startWatch() {

		if (on) {
			if (begstopwatch == false) {
			clickbegin = System.currentTimeMillis();
			begstopwatch = true;
			}
			else {
			throw new IllegalStateException("You did not implement stopwatch method");
			}
		}
	}

	public static void stopWatch() {
		if (on) {
			if (begstopwatch == true) {
			clickend = System.currentTimeMillis();
			long time = clickend - clickbegin;
			System.out.printf("milliseconds:%04d\n", time);
			begstopwatch = false;//this resets the stopwatch
			}
			else {
			throw new IllegalStateException("You did not implement the startwatch method first!");
			}
		}	
	}
	
	//these are meant to be more permenent to see how long it takes to get through each part of the cycle
	public static void begCycleTime() {
		//don't even do anything unless cycletimeon varible is true
		if (cycletimeon) {
		cycletimebeg = System.currentTimeMillis();
			if (begcycletime == true) {
			throw new IllegalStateException("You did not implement endCycleTimeMethod!");
			}
			else {
			begcycletime = true;
			}
		}
	}

	public static void endCycleTime(String loc) {
	//don't even do anything unless cycletime variable is true
		if (cycletimeon) {
		int maxlength = 20;
			if (loc == null) {
			throw new IllegalArgumentException("Location was " + loc);
			}
			if (loc.length() > maxlength) {
			throw new IllegalArgumentException("Loaction size was greater than " + String.valueOf(maxlength));
			}
			if (begcycletime == false) {
			throw new IllegalStateException("You did not implement begcycltime method");
			}
			else {
			cycletimeend = System.currentTimeMillis();
			long time = cycletimeend - cycletimebeg;
			System.out.printf("Location:%-" + String.valueOf(maxlength) + "s\t milliseconds:%04d\n", loc, time);
			cycletimebeg = cycletimeend;//this resets beg time for next time
			}
		}	
	}
}	
