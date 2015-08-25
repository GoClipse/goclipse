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

import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.HashSet2;
import melnorme.utilbox.collections.Indexable;

/**
 * Utils for creation, query, and modification of Collection classes.
 */
public class CollectionUtil {
	
	@SuppressWarnings("rawtypes")
	public static final Indexable EMPTY_INDEXABLE = new ArrayList2<>();
	
	public static <T> Indexable<T> nullToEmpty(Indexable<T> indexable) {
		return Indexable.nullToEmpty(indexable);
	}
	
	/** @return a new {@link ArrayList} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> ArrayList2<E> createArrayList(Iterable<? extends E> iterable) {
		return new ArrayList2<>(nullToEmpty(iterable));
	}
	/** @return a new {@link LinkedList} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> LinkedList<E> createLinkedList(Iterable<? extends E> iterable) {
		return addAll2(new LinkedList<E>(), nullToEmpty(iterable));
	}
	/** @return a new {@link HashSet} from given collection 
	 * (a null collection is considered as if it's an empty one). */
	public static <E> HashSet2<E> createHashSet(Iterable<? extends E> iterable) {
		return new HashSet2<>(nullToEmpty(iterable));
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
	
	/* ----------------- query ----------------- */
	
	public static <T> int indexOfSame(Iterable<T> iterable, T obj) {
		int ix = 0;
		Iterator<? extends T> iterator = iterable.iterator();
		while(iterator.hasNext()) {
			T element = iterator.next();
			if(element == obj)
				return ix;
			ix++;
		}
		return -1;
	}
	
	public static <T> int indexOf(Iterable<T> iterable, T obj) {
		int ix = 0;
		Iterator<? extends T> iterator = iterable.iterator();
		while(iterator.hasNext()) {
			T element = iterator.next();
			if(areEqual(element, obj))
				return ix;
			ix++;
		}
		return -1;
	}
	
	
	/** @return whether given coll contains given obj 
	 * (obj must be the same as the one contained, not just equal). 
	 */
	public static boolean containsSame(Iterable<?> coll, Object obj) {
		for(Iterator<?> iterator = coll.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			if(element == obj)
				return true;
		}
		return false;
	}
	
	
	/** @return whether given coll contains all elements of other, using a naive/basic algorithm. */
	public static boolean containsAll(Collection<?> coll, Collection<?> other) {
		for(Object otherElement : other) {
			if(!coll.contains(otherElement))
				return false;
		}
		return true;
	}
	
	/* ----------------- retrieval ----------------- */
	
	/**
	 * @return the first element of given coll, if collection is non-null and non-empty. Otherwise null. 
	 */
	public static <T> T getSingleElementOrNull(Iterable<T> coll) {
		if(coll == null)
			return null;
		Iterator<T> iterator = coll.iterator();
		T firstElement = iterator.next();
		return iterator.hasNext() ? null : firstElement;
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
	
	public static <ELEM, COLL extends Collection<? super ELEM>> COLL addAll2(COLL dest, Iterable<? extends ELEM> source) {
		return addAllFromIterator(dest, source.iterator());
	}
	
	public static <ELEM, COLL extends Collection<? super ELEM>> COLL addAllFromIterator(COLL dest, 
			Iterator<? extends ELEM> source) {
		while(source.hasNext()) {
			dest.add(source.next());
		}
		return dest;
	}
	
	public static <ELEM, COLL extends Collection<? super ELEM>, SourceELEM> COLL addAll(COLL dest, 
			Iterable<? extends SourceELEM> source, Function<SourceELEM, ELEM> mapper) {
		for(SourceELEM elem : source) {
			dest.add(mapper.apply(elem));
		}
		return dest;
	}
	
	/** Remove from given list all elements that match given predicate. */
	public static <E, L extends List<E>> L filter(L list, Predicate<E> predicate) {
		for (Iterator<? extends E> iter = list.iterator(); iter.hasNext(); ) {
			E obj = iter.next();
			if(predicate.test(obj)) {
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
			if(predicate.test(obj)) {
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
	
	/* ----------------- functional API ----------------- */
	// We might want to modify this to use Java 8 Streams
	
	public static <E, R> ArrayList2<R> map(
			Iterable<E> coll, Function<? super E, ? extends R> evalFunction) {
		return mapTo(coll, evalFunction, new ArrayList2<R>());
	}
	
	public static <E, R, DEST extends Collection<R>> DEST mapTo(
			Iterable<E> coll, Function<? super E, ? extends R> evalFunction, DEST destCollection) {
		for(E collElement : coll) {
			R mappeElem = evalFunction.apply(collElement);
			destCollection.add(mappeElem);
		}
		
		return destCollection;
	}
	
	public static boolean listEquals(Indexable<?> coll1, Indexable<?> coll2) {
		return Indexable.indexableEquals(coll1, coll2);
	}
	
	public static boolean listEquals(List<?> coll1, List<?> coll2) {
		if(coll1.size() != coll2.size()) {
			return false;
		}
		
		return iterationEquals(coll1.iterator(), coll2.iterator());
	}
	
	public static boolean iterationEquals(Iterator<?> iter1, Iterator<?> iter2) {
		while(iter1.hasNext() && iter2.hasNext()) {
	        Object o1 = iter1.next();
	        Object o2 = iter2.next();
	        if(!areEqual(o1, o2)) {
				return false;
			}
	    }
	    return !(iter1.hasNext() || iter2.hasNext());
	}
	
}