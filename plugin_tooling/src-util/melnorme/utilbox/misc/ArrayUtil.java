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
package melnorme.utilbox.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.fntypes.Function;
import melnorme.utilbox.core.fntypes.Predicate;

public class ArrayUtil {
	
	public static final Object[] EMPTY_ARRAY = new Object[] {};
	
	/** @return the given array if it is non-null, an empty array otherwise. */
	public static Object[] nullToEmpty(Object[] array) {
		return array == null ? EMPTY_ARRAY : array;
	}
	
	/** @return the given array if it is non-null, an empty array otherwise with given componentType. */
	public static <T> T[] nullToEmpty(T[] array, Class<T> componentType) {
		return array == null ? create(0, componentType) : array;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] create(int length, Class<T> componentType) {
		return (T[]) Array.newInstance(componentType, length);
	}
	@SuppressWarnings("unchecked")
	public static <T> T[] create(Class<T> componentType, int length) {
		return (T[]) Array.newInstance(componentType, length);
	}
	
	/** Creates a new array of given length, and same component type as given similarArray. */
	@SuppressWarnings("unchecked")
	public static <T> T[] createWithSameComponentType(int length, T[] similarArray) {
		return (T[]) Array.newInstance(similarArray.getClass().getComponentType(), length);
	}
	
     /** Create an array from the given element, with the given componentType.
     * If the element is null, a zero-length array is created. */
	public static <T> T[] singletonArray(T element, Class<T> componentType) {
		if(element == null) {
			return create(componentType, 0);
		}
		T[] newArray = create(componentType, 1);
		newArray[0] = element;
		return newArray;
	}
	
	/** Create an array from the given list, with the given componentType.
     * If given list is null, a zero-length array is created. */
	public static <T> T[] createFrom(Collection<? extends T> list, Class<T> componentType) {
		if(list == null) {
			return create(componentType, 0);
		}
		return list.toArray(create(componentType, list.size()));
	}
	
	/** Create an array from the given list, with Object.class as the component type.
     * If given list is null, a zero-length array is created. */
	public static Object[] createFrom(Collection<?> list) {
		return createFrom(list, Object.class);
	}
	
	public static <E> E[] copyFrom(E[] array, Class<E> componentType) {
		E[] newArray = create(array.length, componentType);
	    System.arraycopy(array, 0, newArray, 0, array.length);
	    return newArray;
	}
	
    /** Create an array from the given list, with the given cpType as the run-time component type.
     * If the list is null, null is returned. */
	public static <T> T[] toArray(Collection<? extends T> list, Class<T> componentType) {
		if(list == null) {
			return null;
		}
		return list.toArray(create(componentType, list.size()));
	}
	
	/** Creates a new array with the given length, and of the same type as the given array. */
	public static <T> T[] copyFrom(T[] array, int newLength) {
		T[] copy = createWithSameComponentType(newLength, array);
    	System.arraycopy(array, 0, copy, 0, Math.min(array.length, newLength));
    	return copy;
	}
	
	/** Creates a copy of given array with the given newlength, and of type char[]. */
	public static char[] copyFrom(char[] array, int newLength) {
        char[] copy = (char[]) Array.newInstance(Character.TYPE, newLength);
    	System.arraycopy(array, 0, copy, 0, Math.min(array.length, newLength));
    	return copy;
	}
	
	/** Creates a copy of given array with the given newlength, and of type byte[]. */
	public static byte[] copyFrom(byte[] array, int newLength) {
		byte[] copy = (byte[]) Array.newInstance(Byte.TYPE, newLength);
    	System.arraycopy(array, 0, copy, 0, Math.min(array.length, newLength));
    	return copy;
	}
	
	/** Creates a copy of given array with the given newlength, and of type int[]. */
	public static int[] copyFrom(int[] array, int newLength) {
		int[] copy = (int[]) Array.newInstance(Integer.TYPE, newLength);
    	System.arraycopy(array, 0, copy, 0, Math.min(array.length, newLength));
    	return copy;
	}
	
	/** Creates a copy of given array, and of type int[]. */
	public static int[] copyFrom(int[] array) {
		return copyFrom(array, array.length);
	}
	
	/** Creates a copy of given array, and of type T[]. */
	public static <T> T[] copyFrom(T[] array) {
		return Arrays.copyOf(array, array.length);
	}
	
	/** Same as {@link Arrays#copyOfRange(Object[], int, int)} */
	public static <T> T[] copyOfRange(T[] original, int from, int to) {
        return Arrays.copyOfRange(original, from, to);
    }
	
	/** Same as {@link Arrays#copyOfRange(Object[], int, int, Class)} */
	public static <T,U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
    	return Arrays.copyOfRange(original, from, to, newType);
    }
    
	/** Creates an int[] from given coll of Integers. */
	public static int[] createIntArray(List<? extends Integer> coll) {
		int[] array = new int[coll.size()];
		for (int i = 0; i < coll.size(); i++) {
			array[i] = coll.get(i);
		}
		return array;
	}
	
	/** Creates an array with the same size as the given list.
	 * If the list is null, a zero-length array is created. */
	public static <T> T[] newSameSize(List<?> list, Class<T> cpType) {
		if(list == null)
			return create(cpType, 0);
		else 
			return create(cpType, list.size());
	}

    /** Copies src array range [0 .. src.length] to dest array starting at destIx. */
	public static void copyToRange(byte[] src, byte[] dest, int destIx) {
		assertTrue(src.length < dest.length - destIx);
		System.arraycopy(src, 0, dest, destIx, src.length);
	}
	
	
	/** Appends an element to array, creating a new array. */
	public static <T> T[] append(T[] base, T element) {
		T[] newArray = copyFrom(base, base.length + 1);
		newArray[base.length] = element;
		return newArray;
	}
	
	/** Creates a new array with given first element prepended to given rest array. */
	@SafeVarargs
	public static <T> T[] prepend(T first, T... rest) {
		T[] newArray = createWithSameComponentType(rest.length + 1, rest);
		newArray[0] = first;
		System.arraycopy(rest, 0, newArray, 1, rest.length);
		return newArray;
	}
	
	/** Appends given array other to given array base, 
	 * creating a new array of the same runtime type as original. */
	@SafeVarargs
	public static <T> T[] concat(T[] base, T... other) {
		return concat(base, other, other.length);
	}
	
	/** Appends appendCount number of elements of given array other to given array base, 
	 * creating a new array of the same runtime type as original. */
	public static <T> T[] concat(T[] base, T[] other, int appendCount) {
		T[] newArray = copyFrom(base, base.length + appendCount);
		System.arraycopy(other, 0, newArray, base.length, appendCount);
		return newArray;
	}
	
	/** Appends appendCount number of elements of given array other to given array base */
	public static byte[] concat(byte[] base, byte[] other, int appendCount) {
		final int length = base.length;
		byte[] newArray = copyFrom(base, base.length + appendCount);
		System.arraycopy(other, 0, newArray, length, appendCount);
		return newArray;
	}
	
	/** Appends appendCount number of elements of given array other to given array base */
	public static char[] concat(char[] base, char[] other, int appendCount) {
		final int length = base.length;
		char[] newArray = copyFrom(base, base.length + appendCount);
		System.arraycopy(other, 0, newArray, length, appendCount);
		return newArray;
	}
	
	/** Removes from the given array the first element that isEqual to given objToRemove. */
	public static<T> T[] remove(T[] array, T objToRemove) {
		for (int i = 0; i < array.length; i++) {
			T elem = array[i];
			if(areEqual(elem, objToRemove))
				return removeAt(array, i);
		}
		return array;
	}
	
	
	/** Removes the element at index ix from array, creating a new array. */
	public static <T> T[] removeAt(T[] array, int ix) {
		T[] newArray = copyFrom(array, array.length - 1);
		System.arraycopy(array, 0, newArray, 0, ix);
		System.arraycopy(array, ix + 1, newArray, ix, array.length - ix - 1);	
		return newArray;
	}
	
	/** Removes the last given count elements from given array, creating a new array. */
	public static <T> T[] removeLast(T[] array, int count) {
		assertTrue(array.length >= count);
		T[] newArray = ArrayUtil.copyFrom(array, array.length - count);
		return newArray;
	}
	
	/** Removes from the given array the first element that areEqual given objToRemove. 
	 * @return a new array with elements removed, or the same array if no elements where removed. */
	public static<T> T[] removeAll(T[] array, T objToRemove) {
		int removeCount = 0;
		for (T elem : array) {
			if(areEqual(elem, objToRemove)) {
				removeCount++;
			}
		}
		if(removeCount == 0) {
			return array;
		}
		int ix = 0;
		T[] newArray = createWithSameComponentType(array.length - removeCount, array); 
		for (T elem : array) {
			if(areEqual(elem, objToRemove)) {
				continue;
			}
			newArray[ix] = elem;
			ix++;
		}
		
		return newArray;
	}

	
	/* ====================== search/index ====================== */
	
	/** @return the last element of given array, or null if array is empty. */
	public static <T> T getLastElement(T[] array) {
		if(array.length == 0) {
			return null;
		}
		return array[array.length-1];
	}
	
	/** @return the same as {@link List#indexOf(Object)}, using given array as a collection. */
	public static <T> int indexOf(T[] array, T elem) {
		// return Arrays.asList(array).indexOf(elem);
		if(elem == null) {
			for(int ix = 0; ix < array.length; ix++) {
				if(array[ix] == null)
					return ix;
			}
		} else {
			for(int ix = 0; ix < array.length; ix++) {
				if(elem.equals(array[ix]))
					return ix;
			}
		}
		return -1;
	}
	
	/** @return true if array contains an element equal to obj. */
	public static <T> boolean contains(T[] array, T obj) {
		return indexOf(array, obj) != -1;
	}
	
	/** @return the index in given array of the first occurrence of given elem, or -1 if none is found. */
	public static int indexOf(byte[] array, byte elem) {
		for (int i = 0; i < array.length; i++) {
			if(array[i] == elem)
				return i;
		}
		return -1;
	}
	
	/** @return the index in given array of the first element that is the same as given elem, 
	 * or -1 if none is found. */
	public static <T> int indexOfIdentity(T[] array, T elem) {
		for (int i = 0; i < array.length; i++) {
			if(array[i] == elem)
				return i;
		}
		return -1;
	}
	
	/** @return true if array contains an element that matched given predicate. */
	public static <T> boolean search(T[] array, Predicate<T> predicate) {
		for(T elem: array) {
			if(predicate.evaluate(elem))
				return true;
		}
		return false;
	}
	
	/** Is {@link #map(Object[], Function, Class)}  with klass = Object.class */
	public static <T> Object[] map(T[] array, Function<? super T, ? extends Object> evalFunction) {
		return ArrayUtil.map(array, evalFunction, Object.class);
	}
	
	/** Creates a new array, based on given array, whose elements are produced element-wise from the original 
	 * array using given evalFunction. */
	public static <T, R> R[] map(T[] array, Function<? super T, ? extends R> evalFunction, Class<R> klass) {
		R[] newArray = create(array.length, klass);
		for(int i = 0; i < newArray.length; i++) {
			newArray[i] = evalFunction.evaluate(array[i]);
		}
		return newArray;
	}
	
	/** Is {@link #map(Collection, IEvalFunc, Class klass)}, IEvalFunc, Class) with klass = Object.class */
	public static <T> Object[] map(Collection<T> coll, Function<? super T, ? extends Object> evalFunction) {
		return map(coll, evalFunction, Object.class);
	}
	/** Is {@link #map(Collection, IEvalFunc, Class klass)}, IEvalFunc, Class) with klass = Object.class */
	public static <T> Object[] map(Collection2<T> coll, Function<? super T, ? extends Object> evalFunction) {
		return map(coll, evalFunction, Object.class);
	}
	
	/** Creates a new array, based on given coll, whose elements are produced element-wise from the original 
	 * coll using given evalFunction. */
	public static <T, R> R[] map(Collection<T> coll, Function<? super T, ? extends R> evalFunction, Class<R> klass) {
		R[] newArray = create(coll.size(), klass);
		int i = 0;
		for(T elem : coll) {
			newArray[i] = evalFunction.evaluate(elem);
			i++;
		}
		return newArray;
	}
	/** Creates a new array, based on given coll, whose elements are produced element-wise from the original 
	 * coll using given evalFunction. */
	public static <T, R> R[] map(Collection2<T> coll, Function<? super T, ? extends R> evalFunction, Class<R> klass) {
		R[] newArray = create(coll.size(), klass);
		int i = 0;
		for(T elem : coll) {
			newArray[i] = evalFunction.evaluate(elem);
			i++;
		}
		return newArray;
	}
	
	/** Filters given array, using given predicate, creating a new array. */
	public static <T> T[] filter(T[] array, Predicate<T> predicate) {
		T[] newArray = createWithSameComponentType(array.length, array);
		assertTrue(newArray.length <= array.length);
		int newIx = 0, arrayIx = 0;
		while(arrayIx < array.length) {
			if(predicate.evaluate(array[arrayIx])) {
				newArray[newIx] = array[arrayIx];
				newIx++; arrayIx++;
			} else {
				arrayIx++;
			}
		}
		return newIx == arrayIx ? newArray : Arrays.copyOfRange(newArray, 0, newIx);
	}
	
}