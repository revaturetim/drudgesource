package drudge;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

final public class Debug {
private static String prompt = null;
private static long clickbegin = 0L;
private static long clickend = 0L;
private static long stopend = 0L;
private static long stopbegin = 0L;
private static boolean on = true;
private static boolean begstopwatch = false;
private static long timebegin = 0L;
public static boolean cycletimeon = false;
final private static String sep = " | ";

	public static void stop(String msg, BiConsumer<String, String> thing) {
		if (on) {
		stopend = System.currentTimeMillis();
		String stop = (msg == null) ? getPrompt() + ":" : msg.toString() + ":";
			
			try {
			
OUTER:				while (true) {
				System.out.print(stop);
				byte[] b = new byte[20];
				System.in.read(b);//this blocks until there is something to read
				String action = new String(b).trim();
INNER:					while (true) {
						if (action.equals("cont")) {
						break OUTER;	
						}
						else if (action.equals("exit")) {
						System.exit(0);
						}
						else if (action.equals("time")) {
						final long t = (stopbegin > 0) ? stopend - stopbegin : 0L;
						System.out.printf(stop + "milliseconds:%04d\n", t);
						break INNER;
						}
						else if (action.equals("help")) {
						
						break INNER;
						}
						else if (action.equals("source")) {
						
						break INNER;
						}
						else {
						thing.accept(stop, action);
						break INNER;//for some reason this has to break inner loop
						}
					}
				}
			}
			catch (IOException I) {
			System.out.println("Stop method broke");
			}
		stopbegin = System.currentTimeMillis();
		}
	}
	
	public static void stop(Class klass) {
	
		BiConsumer<String, String> thing = new BiConsumer<String, String>() {
		
			public void accept(String stop, String action) {
				if (action.equals("field")) {
				Field[] fs = klass.getFields();
					for (Field f : fs) {
					System.out.print(stop);
					System.out.print(f.toString());
					System.out.print("=");
						try {
						System.out.println(f.get(klass));
						}
						catch (IllegalAccessException E) {
									
						}
						catch (IllegalArgumentException E) {
									
						}
						catch (NullPointerException E) {
									
						}
						catch (ExceptionInInitializerError E) {
									
						}
					}
				}
				else if (action.startsWith("set ")) {
				String[] params = action.split(" ");
					try {
					Field f = klass.getField(params[1]);
					f.set(klass, params[2]);
					}
					catch (NoSuchFieldException N) {
					
					}
					catch (IllegalAccessException E) {
									
					}
					catch (IllegalArgumentException E) {
									
					}
					catch (NullPointerException E) {
									
					}
					catch (ExceptionInInitializerError E) {
									
					}
				}
				else if (action.startsWith("call ")) {
				String[] params = action.split(" ");
					try {
					
					Class[] cs = new Class[params.length - 2];
						for (int i = 2; i < params.length; i++) {
							try {
							Class c = Class.forName(params[i]);
							cs[i - 2] = c;
							}
							catch (ClassNotFoundException C) {
							
							}
						
						}
					Method m = klass.getMethod(params[1], cs);
					m.invoke(klass, new Object[0]);
					
					}
					catch (NoSuchMethodException N) {
					
					}
					catch (IllegalAccessException E) {
									
					}
					catch (IllegalArgumentException E) {
									
					}
					catch (NullPointerException E) {
									
					}
					catch (ExceptionInInitializerError E) {
									
					}
					catch (InvocationTargetException I) {
					
					}
					
				
				
				}
				else if (action.equals("annotation")) {
				Annotation[] as = klass.getAnnotations();
					for (Annotation a : as) {
					System.out.print(stop);
					System.out.println(a.toString());
					}
				
				}
				else if (action.equals("method")) {
				Method[] ms = klass.getMethods();
					for (Method m : ms) {
					System.out.print(stop);
					System.out.println(m.toString());
					}
				}
				else if (action.equals("interface")) {
				Class[] cs = klass.getInterfaces();
					for (Class c : cs) {
					System.out.print(stop);
					System.out.println(c.toString());
					}
				}
				else if (action.equals("super")) {
				Class c = klass.getSuperclass();
				System.out.print(stop);
				System.out.println(c.toString());	
				}
				else if (action.equals("signer")) {
				Object[] signers = klass.getSigners();
					if (signers != null) {
						for (Object signer : signers) {
						System.out.print(stop);
						System.out.println(signer.toString());
						}	
					}
					else {
					System.out.print(stop);
					System.out.println("No Signers");
					}
						
				}
				else if (action.equals("inner")) {
				Class[] cs = klass.getClasses();
					for (Class c : cs) {
					System.out.print(stop);
					System.out.println(c.toString());
					} 
				}
				else if (action.equals("constructor")) {
				Constructor[] cons = klass.getConstructors();
					for (Constructor con : cons) {
					System.out.print(stop);
					System.out.println(con.toString());
					}
				}
				else if (action.equals("package")) {
				Package pack = klass.getPackage();
				System.out.print(stop);
				System.out.println(pack.toString());
				}
				else if (action.equals("enclose")) {
							
					if (klass.isLocalClass()) {
					Method m = klass.getEnclosingMethod();
					System.out.print(stop);
					System.out.println(m.toString());
					}
					else {
					System.out.print(stop);
					System.out.println(klass.toString() + " is not an local class");
					}
				}
			
			}	
		};	
	stop(null, thing);
	}
	
	public static void stop(Class c, String m) {
	
	
	}
	
	public static void stop(Object...values) {
	
		BiConsumer<String, String> thing = new BiConsumer<String, String>() {
		
			public void accept(String stop, String action) {
				if (action.equals("value")) {
					for (Object value : values) {
					System.out.print(stop);
						if (value != null) {
						String type = value.getClass().getName();
						System.out.println(type + "\t" + value);
						}
						else {
						System.out.println(null + "\t\t\t" + value);
						}
					}
				}
			}
		};
	stop(null, thing);
	}
	
	public static void stop() {
	
		Consumer<String> thing = new Consumer<String>() {
		
			public void accept(String s) {
			
			}
			
		};
		
	stop(null, thing);
	}

	public static void check(final BiPredicate<Object, Object> P, final Object...objs) {
		if (on) {
			for (int i = 1; i < objs.length; i++) {
				
				if (objs[i] == null && objs[i - 1] == null) {
				System.out.println(getPrompt() + "null object detected!");
				}
				else if ((objs[i] != null && objs[i - 1] != null) && !P.test(objs[i - 1], objs[i])) {//failed test
				System.out.println(getPrompt() + objs[i - 1].toString() + " not equal " + objs[i]);
				}
			}
		}
	}
	
	public static void check(final Object...objs) {
	
		BiPredicate<Object, Object> thing = new BiPredicate<Object, Object>() {
		
			public boolean test(Object a, Object b) {
			return b.equals(a);	
			}	
		};
	check(thing, objs); 
	}
	
	public static void between(final int a, final int b, final int e) throws IndexOutOfBoundsException {
		if (on) {
			if (a < b || a > e) {
			System.out.println(getPrompt() + 
			String.valueOf(e)
			+ " was not between " 
			+ String.valueOf(a) 
			+ " and " + String.valueOf(b));
			}
		}		
	}

	public static void here(Object...objs) {
		
		Consumer<Object> thing = new Consumer<Object>() {
			
			public void accept(Object obj) {
			System.out.println("Location=" + obj.toString());
			}
		};
	print(thing, objs);		
	}
		
	public static void here() {
	here("here");
	}

	public static void stopWatch() {
		if (on) {
			if (begstopwatch) {
			clickend = System.currentTimeMillis();
			final long time = clickend - clickbegin;
			System.out.printf("milliseconds:%04d\n", time);
			begstopwatch = false;//this resets the stopwatch
			}
			else {
			clickbegin = System.currentTimeMillis();
			begstopwatch = true;
			}	
		}	
	}

	public static void time(final String loc) {
	
		if (cycletimeon) {
		final int maxlength = 20;
			if (loc == null) {
			throw new IllegalArgumentException("Location parameter was not set");
			}
			if (loc.length() > maxlength) {
			throw new IllegalArgumentException("Loaction size was greater than " + String.valueOf(maxlength));
			}
		final long current_time = System.currentTimeMillis();
		final long time = (timebegin > 0) ? current_time - timebegin : 0;
		System.out.printf("Location:%-" + String.valueOf(maxlength) + "s\t milliseconds:%04d\n", loc, time);
		timebegin = current_time;//this resets beg time for next time	
		}	
	}
	
	private static String getPrompt() {
		if (prompt == null) { 
		GregorianCalendar cal = new GregorianCalendar();
		String time = cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + "::";
		return time;
		}
		else {
		return prompt;
		}
	}
	
	private static void print(Consumer<Object> con, Object...objs) {
		if (on) {
			for (int i = 0; i < objs.length; i++) {
			String p = getPrompt();
			System.out.print(p);
			con.accept(objs[i]);
			}
		}
	}
	
}	
