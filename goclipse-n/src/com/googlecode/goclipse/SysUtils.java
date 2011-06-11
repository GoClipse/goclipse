package com.googlecode.goclipse;

/**
 * 
 * @author steel
 * 
 */
public class SysUtils {

	public static void debug(String msg) {
		if (Environment.DEBUG)
			System.out.println("[DEBUG]   " + msg);
	}

	public static void warning(String msg) {
		System.out.println("[WARNING] " + msg);

	}

	public static void debug(Exception e) {
		if (Environment.DEBUG)
			e.printStackTrace();
	}

	public static void severe(Throwable e) {
		e.printStackTrace();
	}

}
