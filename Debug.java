package drudge;

import java.io.*;

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

	public static void stop() {
		if (on) {
			try {
			InputStream in = System.in;
			System.out.print("Stopped");
			byte[] b = new byte[1];
			in.read(b);//this blocks until there is something to read
			}
			catch (IOException I) {
			System.out.println("Stop method broke");
			}
		}
	}

	public static void stop(Object obj) {
		if (on) {
		print(obj);
		stop();//this will call the stop method as it is suppose to
		}
	}
		
	public static void print(Exception E) {
		if (on) {
		System.out.println(E.getMessage());
		}
	}

	public static void print(Object j) {
		if (on) {
			if (j != null) {
			Class<? extends Object> c = j.getClass();
			System.out.printf("%10s=%-10s" + sep, c.getName(), j.toString());
			}
			else {
			System.out.printf("%10s=%-10s" + sep, "Null", "Null");
			}
		}
	}

	public static void print(Object j, Object k) {
		if (on) {
		print(j);
		println(k);
		}	
	}

	public static void print(Object j, Object k, Object l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(Object j) {
		if (on) {
			if (j != null) {
			Class<? extends Object> c = j.getClass();
			System.out.printf("%10s=%-10s\n", c.getName(), j.toString());
			}
			else {
			System.out.printf("%10s=%-10s\n", "Null", "Null");
			}
		}
	}

	public static void println(Object j, Object k) {
		if (on) {
		println(j);
		println(k);
		}	
	}

	public static void println(Object j, Object k, Object l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}

	public static void println(int j) {
		if (on) {
		System.out.printf("%10s=%-10d\n", "integer", j);
		}
	
	}

	public static void println(int j, int k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(int j, int k, int l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}


	public static void print(int j) {
		if (on) {
		System.out.printf("%10s=%-10d" + sep, "integer", j);
		}
	
	}

	public static void print(int j, int k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(int j, int k, int l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(char j) {
		if (on) {
		System.out.printf("%10s=%-10d\n", "character", j);
		}
	
	}

	public static void println(char j, char k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(char j, char k, char l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}

	public static void print(char j) {
		if (on) {
		System.out.printf("%10s=%-10d" + sep, "character", j);
		}
	
	}

	public static void print(char j, char k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(char j, char k, char l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(double j) {
		if (on) {
		System.out.printf("%10s=%-10d\n", "double", j);
		}
	}

	public static void println(double j, double k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(double j, double k, double l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}

	public static void print(double j) {
		if (on) {
		System.out.printf("%10s=%-10d" + sep, "double", j);
		}
	}

	public static void print(double j, double k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(double j, double k, double l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(float j) {
		if (on) {
		System.out.printf("%10s=%-10d\n", "float", j);
		}
	}

	public static void println(float j, float k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(float j, float k, float l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}

	public static void print(float j) {
		if (on) {
		System.out.printf("%10s=%-10d" + sep, "float", j);
		}
	}

	public static void print(float j, float k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(float j, float k, float l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(byte j) {
		if (on) {
		System.out.printf("%10s=%-10d\n", "byte", j);
		}
	}

	public static void println(byte j, byte k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(byte j, byte k, byte l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}
	
	public static void print(byte j) {
		if (on) {
		System.out.printf("%10s=%-10d" + sep, "byte", j);
		}
	}

	public static void print(byte j, byte k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(byte j, byte k, byte l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(boolean j) {
		if (on) {
		System.out.printf("%10s=%-10b\n", "boolean", j);
		}	
	}

	public static void println(boolean j, boolean k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(boolean j, boolean k, boolean l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}

	public static void print(boolean j) {
		if (on) {
		System.out.printf("%10s=%-10b" + sep, "boolean", j);
		}	
	}

	public static void print(boolean j, boolean k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(boolean j, boolean k, boolean l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(long j) {
		if (on) {
		System.out.printf("%10s=%-10d\n", "long", j);
		}
	}
	
	public static void println(long j, long k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(long j, long k, long l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}

	public static void print(long j) {
		if (on) {
		System.out.printf("%10s=%-10d" + sep, "long", j);
		}
	}
	
	public static void print(long j, long k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(long j, long k, long l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
	}

	public static void println(short j) {
		if (on) {
		System.out.printf("%10s=%-10d\n", "short", j);
		}	
	}

	public static void println(short j, short k) {
		if (on) {
		println(j);
		println(k);
		}
	}

	public static void println(short j, short k, short l) {
		if (on) {
		println(j);
		println(k);
		println(l);
		}
	}

	public static void print(short j) {
		if (on) {
		System.out.printf("%10s=%-10d" + sep, "short", j);
		}	
	}

	public static void print(short j, short k) {
		if (on) {
		print(j);
		println(k);
		}
	}

	public static void print(short j, short k, short l) {
		if (on) {
		print(j);
		print(k);
		println(l);
		}
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
