/*******************************************************************************
 * Copyright (c) 2011, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import melnorme.utilbox.core.Assert;
import melnorme.utilbox.core.CoreUtil;
import melnorme.utilbox.core.fntypes.Function;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.misc.StringUtil;

/**
 * A base class for common, miscellaneous test utils. 
 */
public class CommonTestUtils {
	
	public static boolean TRUE() {
		return true;
	}
	
	public static void assertEquals(Object obj1, Object obj2) {
		Assert.equals(obj1, obj2);
	}
	
	public static void assertAreEqual(Object obj1, Object obj2) {
		assertTrue(CoreUtil.areEqual(obj1, obj2));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T assertCast(Object object, Class<T> klass) {
		assertTrue(object == null || klass.isInstance(object));
		return (T) object;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T assertInstance(Object object, Class<T> klass) {
		assertTrue(klass.isInstance(object));
		return (T) object;
	}
	
	/** Assert that the given arrays are equal according to Arrays.equals().
	 *  (equal content-wise, order relevant) */
	public static void assertEqualArrays(Object[] arr1, Object[] arr2) {
		assertTrue(Arrays.equals(arr1, arr2));
	}
	
	public static <T> void assertContains(T[] array, T obj) {
		assertTrue(ArrayUtil.contains(array, obj));
	}
	
	public static void assertEqualSet(Set<?> result, Set<?> expected) {
		boolean equals = result.equals(expected);
		if(equals) {
			return;
		}
		HashSet<?> resultExtra = removeAllCopy(result, expected);
		HashSet<?> expectedExtra = removeAllCopy(expected, result);
		assertTrue(equals,
				"Obtained result set not equal to expected set. \n" +
				"--- Extra elements in result set ("+resultExtra.size()+") : --- \n" +
				StringUtil.collToString(resultExtra, "\n") + "\n" +
				"--- Extra elements in expected set ("+expectedExtra.size()+") : --- \n" +
				StringUtil.collToString(expectedExtra, "\n") + "\n" +
				"== -- =="
		);
	}
	
	public static void assertExceptionContains(Exception exception, String string) {
		if(string == null) {
			assertTrue(exception == null);
		} else {
			assertTrue(exception.toString().contains(string));
		}
	}
	
	public static void assertExceptionMsgStart(Exception exception, String string) {
		if(string == null) {
			assertTrue(exception == null);
		} else {
			assertNotNull(exception);
			String message = exception.getMessage();
			assertNotNull(message);
			assertTrue(message.startsWith(string));
		}
	}
	
	/* -------------------------- */
	
	@SafeVarargs
	public static <T> T[] array(T... elems) {
		return elems;
	}
	
	@SafeVarargs
	public static <T> List<T> list(T... elems) {
		return Arrays.asList(elems);
	}
	
	@SafeVarargs
	public static <T> HashSet<T> hashSet(T... elems) {
		return new HashSet<T>(Arrays.asList(elems));
	}
	
	public static <T> Set<T> unmodifiable(Set<T> set) {
		return Collections.unmodifiableSet(set);
	}
	
	public static <T> List<T> unmodifiable(List<T> set) {
		return Collections.unmodifiableList(set);
	}
	
	public static <T> Collection<T> unmodifiable(Collection<T> set) {
		return Collections.unmodifiableCollection(set);
	}
	
	/** ---- **/
	
	
	public static <T> HashSet<T> retainAllCopy(Set<T> set, Collection<?> retainColl) {
		HashSet<T> setCopy = CollectionUtil.createHashSet(set);
		setCopy.retainAll(retainColl);
		return setCopy;
	}
	
	public static <T> HashSet<T> removeAllCopy(Set<T> set, Collection<?> removeColl) {
		HashSet<T> setCopy = CollectionUtil.createHashSet(set);
		setCopy.removeAll(removeColl);
		return setCopy;
	}
	
	public static <T> String[] strmap(Collection<T> coll, Function<? super T, String> evalFunction) {
		return ArrayUtil.map(coll, evalFunction, String.class);
	}
	
	public static <T, RE, C extends Collection<RE>> C mapOut(
		Collection<T> coll, Function<? super T, ? extends RE> evalFunction, C outColl
	) {
		for(T elem : coll) {
			outColl.add(evalFunction.evaluate(elem));
		}
		return outColl;
	}
	
	public static <T> T[] removeLast(T[] array, int count) {
		return ArrayUtil.removeLast(array, count);
	}
	
	/* -------- misc -------- */
	
	public static <T extends Exception> void throwIf(boolean condition, String message) throws RuntimeException {
		if(condition) {
			throw new RuntimeException(message);
		}
	}
	
	public static String safeToString(Object obj) {
		return obj == null ? null : obj.toString();
	}
	
	/* -------- iteration -------- */
	
	public static interface Visitor<T> {
		void visit(T obj);
	}
	
	@SafeVarargs
	public static <T, PRED extends Visitor<T>> void visitContainer(Collection<T> coll, PRED... predicates) {
		Iterator<T> iterator = coll.iterator();
		assertTrue(coll.size() == predicates.length);
		for (int i = 0; iterator.hasNext(); i++) {
			T next = iterator.next();
			predicates[i].visit(next);
		}
	}
	
	@SafeVarargs
	public static <T, PRED extends Visitor<T>> void visitContainer(T[] coll, PRED... predicates) {
		assertTrue(coll.length == predicates.length);
		for (int i = 0; i < coll.length; i++) {
			T next = coll[i];
			predicates[i].visit(next);
		}
	}
	
	public static final Path IGNORE_PATH = Paths.get("###NO_CHECK###");
	public static final String IGNORE_STR = "###NO_CHECK###";
	public static final Object[] IGNORE_ARR = new Object[0];
	public static final String[] IGNORE_ARR_STR = new String[0];
	
	public interface Checker<T> {
		
		void check(T obj);
		
	}
	
	/** Helper class to check result values against expected ones. */
	public static abstract class CommonChecker {
		
		public void checkAreEqual(Object obj, Object expected) {
			if(expected == IGNORE_PATH || expected == IGNORE_STR)
				return;
			assertTrue(CoreUtil.areEqual(obj, expected));
		}
		
		public void checkAreEqualArray(Object[] obj, Object[] expected) {
			if(isIgnoreArray(expected))
				return;
			assertTrue(CoreUtil.areEqualArrays(obj, expected));
		}
		
		protected boolean isIgnoreArray(Object[] expected){
			return expected == IGNORE_ARR || expected == IGNORE_ARR_STR;
		}
		
		protected Object[] ignoreIfNull(Object[] expected) {
			return expected == null ? IGNORE_ARR : expected;
		}
		
	}
	
}