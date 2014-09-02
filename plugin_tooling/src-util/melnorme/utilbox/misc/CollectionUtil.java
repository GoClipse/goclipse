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
	public static <E> ArrayList<E> createArrayList(Collection<? extends E> collection) {
		return new ArrayList<E>(collection == null ? Collections.EMPTY_LIST : collection);
	}
	/** @return a new {@link ArrayList} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <E> ArrayList<E> createArrayList(E... array) {
		ArrayList<E> newCollection = new ArrayList<E>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
	}
	
	/** @return a new {@link LinkedList} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> LinkedList<E> createLinkedList(Collection<? extends E> collection) {
		return new LinkedList<E>(collection == null ? Collections.EMPTY_LIST : collection);
	}
	/** @return a new {@link LinkedList} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <E> LinkedList<E> createLinkedList(E... array) {
		LinkedList<E> newCollection = new LinkedList<E>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
	}
	
	/** @return a new {@link HashSet} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> HashSet<E> createHashSet(Collection<? extends E> collection) {
		return new HashSet<E>(collection == null ? Collections.EMPTY_LIST : collection);
	}
	/** @return a new {@link HashSet} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <E> HashSet<E> createHashSet(E... array) {
		HashSet<E> newCollection = new HashSet<E>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
	}
	
	/** @return given coll if it's non-null, an empty collection otherwise.
	 * The returned collection cannot be modified. */
	public static <E> Collection<E> nullToEmpty(Collection<E> coll) {
		return coll == null ? Collections.EMPTY_LIST : coll;
	}
	
	/** @return given list if it's non-null, an empty List otherwise.
	 * The returned list cannot be modified. */
	public static <E> List<E> nullToEmpty(List<E> list) {
		return list == null ? Collections.EMPTY_LIST : list;
	}
	
	/* ----------------- retrieval ----------------- */
	
	/**
	 * @return the first element of given coll, if collection is non-null and non-empty. Otherwise null. 
	 */
	public static <T> T getFirstElementOrNull(Collection<T> coll) {
		if(coll == null || coll.isEmpty())
			return null;
		return coll.iterator().next();
	}
	
	/* ----------------- modifications ----------------- */
	
	/** Creates a List copy of orig, with all elements except elements equal to excludedElem. */
	public static <E> List<E> copyExcept(E[] orig, E excludedElem) {
		List<E> rejectedElements = new ArrayList<E>(orig.length);
		
		for (int i= 0; i < orig.length; i++) {
			if (!orig[i].equals(excludedElem)) {
				rejectedElements.add(orig[i]);
			}
		}
		return rejectedElements;
	}
	
	/** Remove from given list all elements that match given predicate. */
	public static <E, L extends List<E>> L filter(L list, Predicate<E> predicate) {
		for (Iterator<? extends E> iter = list.iterator(); iter.hasNext(); ) {
			E obj = iter.next();
			if(predicate.evaluate(obj)) {
				iter.remove();
			}
		}
		return list;
	}
	
	/** Removes from given list the first element that matches given predicate. 
	 * @return true if an element was removed, false otherwise. */
	public static <E> boolean removeElement(List<? extends E> list, Predicate<E> predicate) {
		for (Iterator<? extends E> iter = list.iterator(); iter.hasNext(); ) {
			E obj = iter.next();
			if(predicate.evaluate(obj)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
	/** Sorts given list and returns it. */
	public static <E extends Comparable<? super E>, L extends List<E>> L sort(L list) {
		Collections.sort(list);
		return list;
	}
	
	@SuppressWarnings("unused")
	private static void sort__genericsTestCompile() {
		List<? extends Integer> list1 = null;
		ArrayList<? extends Integer> list2 = null;
		list1 = sort(list1);
		list2 = sort(list2);
	}
	public static <E, T extends List<? super E>> T addAll(T coll, Collection<E> other) {
		coll.addAll(other);
		return coll;
	}
	
}