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
import java.util.List;

import melnorme.utilbox.core.fntypes.Predicate;

/**
 * Utils for creation, query, and modification of Collection classes.
 */
public class CollectionUtil {
	
	/** @return given coll if it's not null, or an immutable empty collection otherwise. */
	public static <U> Collection<U> nullToEmpty(Collection<U> coll) {
		return coll == null ? Collections.EMPTY_LIST : coll;
	}
	
	/** Creates a {@link ArrayList} from given collection (collection can be null). */
	public static <T> Collection<T> createArrayList(Collection<? extends T> collection) {
		return new ArrayList<T>(collection == null ? Collections.EMPTY_LIST : collection);
	}
	
	/** Creates a {@link HashSet} from given collection (collection can be null). */
	public static <T> HashSet<T> createHashSet(Collection<T> collection) {
		return new HashSet<T>(collection == null ? Collections.EMPTY_LIST : collection);
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