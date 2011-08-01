package com.googlecode.goclipse.utils;

/**
 * A general utility class.
 * 
 * @author devoncarew
 */
public class ObjectUtils {

	public static boolean objectEquals(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		} else if (obj1 == null || obj2 == null) {
			return false;
		} else {
			return obj1.equals(obj2);
		}
	}
	
}
