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

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.HashSet2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.fntypes.Predicate;

/**
 * Utils for creation, query, and modification of Collection classes.
 */
public class CollectionUtil {
	
	@SuppressWarnings("rawtypes")
	public static final Indexable EMPTY_INDEXABLE = new ArrayList2<>();
	
	public static <T> Indexable<T> nullToEmpty(Indexable<T> indexable) {
		@SuppressWarnings("unchecked")
		Indexable<T> result = indexable == null ? EMPTY_INDEXABLE : indexable;
		return result;
	}
	
	public static <T extends Collection<E>, E> T addAll(T collection, Iterable<? extends E> iterable) {
		if(iterable != null) {
			for (E elem : iterable) {
				collection.add(elem);
			}
		}
		return collection;
	}
	
	/** @return a new {@link ArrayList} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> ArrayList2<E> createArrayList(Iterable<? extends E> iterable) {
		return addAll(new ArrayList2<E>(), iterable);
	}
	/** @return a new {@link LinkedList} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> LinkedList<E> createLinkedList(Iterable<? extends E> iterable) {
		return addAll(new LinkedList<E>(), iterable);
	}
	/** @return a new {@link HashSet} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> HashSet2<E> createHashSet(Iterable<? extends E> iterable) {
		return addAll(new HashSet2<E>(), iterable);
	}
	
	
	/** @return a new {@link ArrayList2} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <E> ArrayList2<E> createArrayList(E... array) {
		ArrayList2<E> newCollection = new ArrayList2<E>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
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
	/** @return a new {@link HashSet} from given array (a null array is considered like it's an empty one). */
	@SafeVarargs
	public static <E> HashSet2<E> createHashSet(E... array) {
		HashSet2<E> newCollection = new HashSet2<E>();
		if(array != null) {
			Collections.addAll(newCollection, array);
		}
		return newCollection;
	}
	
	/** @return given iterable if it's non-null, an empty immutable {@link Iterable} otherwise. */
	public static <E> Iterable<E> nullToEmpty(Iterable<E> iterable) {
		return iterable == null ? Collections.EMPTY_LIST : iterable;
	}
	
	/** @return given coll if it's non-null, an empty immutable {@link Collection} otherwise. */
	public static <E> Collection<E> nullToEmpty(Collection<E> coll) {
		return coll == null ? Collections.EMPTY_LIST : coll;
	}
	
	/** @return given list if it's non-null, an empty immutable {@link List} otherwise. */
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
	public static <E> ArrayList2<E> copyExcept(E[] orig, E excludedElem) {
		ArrayList2<E> rejectedElements = new ArrayList2<E>(orig.length);
		
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
	
	/** @return whether given coll contains all elements of other, using a naive/basic algorithm. */
	public static boolean containsAll(Collection<?> coll, Collection<?> other) {
		Iterator<?> citer = other.iterator();
		while (citer.hasNext())
			if (!coll.contains(citer.next()))
				return false;
		return true;
	}
	
}