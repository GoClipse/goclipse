/*******************************************************************************
 * Copyright (c) 2007 DSource.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.core;


import java.util.Arrays;

/**
 * Utils for miscellaneous Java core language functionality. 
 */
public class CoreUtil /* extends Assert */ {
	
	/** @return whether the two given objects are the same (including null) or equal. */
	public static boolean areEqual(Object o1, Object o2) {
		return (o1 == o2) || (o1 != null && o2 != null && o1.equals(o2));
	}
	
	/** @return whether the two given arrays are the same (including null) or equal 
	 * according to {@link Arrays#equals(Object[], Object[])}. */
	public static boolean areEqualArrays(Object[] a1, Object[] a2) {
		return (a1 == a2) || (a1 != null && a2 != null && Arrays.equals(a1, a2));
	}
	
	/** Casts given object to a supertype as typed by given klass (actual value not used). This cast is safe. */
	public static <U, T extends U> U upCast(T object, @SuppressWarnings("unused") Class<U> klass) {
		return object;
	}
	
	/** Casts given object to a subtype as typed by given klass (actual value not used). This cast is unsafe. */
	@SuppressWarnings("unchecked")
	public static <T, D extends T> D downCast(T object, @SuppressWarnings("unused") Class<D> klass) {
		return (D) object;
	}
	
	/** Casts given object to whatever subtype is expected. Use with care, this is unsafe. */
	@SuppressWarnings("unchecked")
	public static <T, D extends T> D downCast(T object) {
		return (D) object;
	}
	
	/** Casts given object to whatever type is expected. Use with care, this is very unsafe. */
	@SuppressWarnings("unchecked")
	public static <T> T blindCast(Object object) {
		return (T) object;
	}
	
	/** If given object is an instance of given klass, return it cast to T, otherwise return null. */
	public static <T> T tryCast(Object object, Class<T> klass) {
		if(klass.isInstance(object)) {
			return CoreUtil.<Object, T>downCast(object);
			// The next line should work instead, but doesn't compile due to a JDK javac bug:
			// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
			// return downCast(obj); 
		} else {
			return null;
		}
	}
	
	/** Shortcut for creating an array of T. */
	@SafeVarargs
	public static <T> T[] array(T... elems) {
		return elems;
	}
	
	/** @return an array of boolean's. */
	public static boolean[] arrayB(boolean... elems) {
		return elems;
	}
	/** @return an array of byte's. */
	public static byte[] arrayB(byte... elems) {
		return elems;
	}
	/** @return an array of short's. */
	public static short[] arrayS(short... elems) {
		return elems;
	}
	/** @return an array of int's. */
	public static int[] arrayI(int... elems) {
		return elems;
	}
	/** @return an array of long's. */
	public static long[] arrayL(long... elems) {
		return elems;
	}
	/** @return an array of char's. */
	public static char[] arrayC(char... elems) {
		return elems;
	}
	/** @return an array of float's. */
	public static float[] arrayF(float... elems) {
		return elems;
	}
	/** @return an array of double's. */
	public static double[] arrayD(double... elems) {
		return elems;
	}
	
}
