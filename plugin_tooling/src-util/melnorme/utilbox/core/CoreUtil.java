/*******************************************************************************
 * Copyright (c) 2007, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.core;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.CollectionUtil;

/**
 * Utils for miscellaneous Java core language functionality. 
 */
public class CoreUtil /* extends Assert */ {
	
	/** @return whether the two given objects are the same (including null) or equal. */
	public static boolean areEqual(Object o1, Object o2) {
		return (o1 == o2) || (o1 != null && o2 != null && o1.equals(o2));
	}
	/** @return whether the two given int are equal. */
	public static boolean areEqual(int o1, int o2) {
		return o1 == o2;
	}
	/** @return whether the two given floats are equal. */
	public static boolean areEqual(double o1, double o2) {
		return o1 == o2;
	}
	
	/** @return whether the two given arrays are the same (including null) or equal 
	 * according to {@link Arrays#equals(Object[], Object[])}. */
	public static boolean areEqualArrays(Object[] a1, Object[] a2) {
		return Arrays.equals(a1, a2);
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
	
	/** Asserts that given object is null or an instance of given klass. Returns casted object. */
	@SuppressWarnings("unchecked")
	public static <T> T assertCast(Object object, Class<T> klass) {
		assertTrue(object == null || klass.isInstance(object));
		return (T) object;
	}
	
	/** Asserts that given object an instance of given klass. Returns casted object. */
	@SuppressWarnings("unchecked")
	public static <T> T assertInstance(Object object, Class<T> klass) {
		assertTrue(klass.isInstance(object));
		return (T) object;
	}
	
	/* ----------------- Array and collection core utils ----------------- */
	
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
	
	/** @return given coll if it's not null, or an empty immutable {@link Collection} otherwise. */
	public static <U> Collection<U> nullToEmpty(Collection<U> coll) {
		return coll == null ? Collections.EMPTY_LIST : coll;
	}
	/** @return given coll if it's not null, or an empty immutable {@link Iterable} otherwise. */
	public static <E> Iterable<E> nullToEmpty(Iterable<E> coll) {
		return coll == null ? Collections.EMPTY_LIST : coll;
	}
	/** @return given coll if it's not null, or an empty immutable {@link Indexable} otherwise. */
	public static <E> Indexable<E> nullToEmpty(Indexable<E> coll) {
		return coll == null ? CollectionUtil.EMPTY_INDEXABLE : coll;
	}
	
	/** Create an array from the given list, with the given cpType as the run-time component type.
	 * If the list is null, a zero-length array is created. */
	public static <T> T[] arrayFrom(Collection<? extends T> list, Class<T> cpType) {
		return ArrayUtil.createFrom(list, cpType);
	}
	
	/** @return a new read-only {@link List} from given array (a null array is considered like an empty one). */
	@SafeVarargs
	public static <T> List<T> listFrom(T... array) {
		return Collections.unmodifiableList(CollectionUtil.createArrayList(array));
	}
	
}