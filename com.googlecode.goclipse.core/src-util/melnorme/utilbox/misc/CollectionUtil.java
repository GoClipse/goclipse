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
package melnorme.utilbox.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import melnorme.utilbox.core.fntypes.Predicate;

/**
 * Utils for creation, query, and modification of Collection classes.
 */
public class CollectionUtil {
	
	/** @return a new {@link ArrayList} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <T> ArrayList<T> createArrayList(Collection<? extends T> collection) {
		return new ArrayList<T>(collection == null ? Collections.EMPTY_LIST : collection);
	}
	/** @return a new {@link ArrayList} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <T> ArrayList<T> createArrayList(T... array) {
		ArrayList<T> newCollection = new ArrayList<T>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
	}
	
	/** @return a new {@link LinkedList} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <T> LinkedList<T> createLinkedList(Collection<? extends T> collection) {
		return new LinkedList<T>(collection == null ? Collections.EMPTY_LIST : collection);
	}
	/** @return a new {@link LinkedList} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <T> LinkedList<T> createLinkedList(T... array) {
		LinkedList<T> newCollection = new LinkedList<T>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
	}
	
	/** @return a new {@link HashSet} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <T> HashSet<T> createHashSet(Collection<? extends T> collection) {
		return new HashSet<T>(collection == null ? Collections.EMPTY_LIST : collection);
	}
	/** @return a new {@link HashSet} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <T> HashSet<T> createHashSet(T... array) {
		HashSet<T> newCollection = new HashSet<T>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
	}
	
	/** @return a new unmodifiable {@link Collection} from given array 
	 * (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <T> Collection<T> createCollection(T... array) {
		ArrayList<T> newCollection = new ArrayList<T>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return Collections.unmodifiableCollection(newCollection);
	}
	
	/** @return given coll if it's non-null, an empty collection otherwise.
	 * The returned collection cannot be modified. */
	public static <T> Collection<T> nullToEmpty(Collection<T> coll) {
		return coll == null ? Collections.EMPTY_LIST : coll;
	}
	
	/** Creates a List copy of orig, with all elements except elements equal to excludedElem. */
	public static <T> List<T> copyExcept(T[] orig, T excludedElem) {
		List<T> rejectedElements = new ArrayList<T>(orig.length);
		
		for (int i= 0; i < orig.length; i++) {
			if (!orig[i].equals(excludedElem)) {
				rejectedElements.add(orig[i]);
			}
		}
		return rejectedElements;
	}
	
	/** @return a new collection with all elements that match given predicate removed. */
	public static <T> List<T> filter(Collection<? extends T> coll, Predicate<T> predicate) {
		ArrayList<T> newColl = new ArrayList<T>();
		for (T elem : coll) {
			if(!predicate.evaluate(elem)) {
				newColl.add(elem);
			}
		}
		return newColl;
	}
	
	/** Removes from given list the first element that matches given predicate. */
	public static <T> void removeElement(List<? extends T> list, Predicate<T> predicate) {
		for (Iterator<? extends T> iter = list.iterator(); iter.hasNext(); ) {
			T obj = iter.next();
			if(predicate.evaluate(obj)) {
				iter.remove();
			}
		}
	}
	
	/** Sorts given list and returns it. */
	public static <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		Collections.sort(list);
		return list;
	}
	
	@SuppressWarnings("unused")
	private static void testCompile_sort_generics() {
		List<? extends Integer> list = null;
		sort(list);
	}
	
}