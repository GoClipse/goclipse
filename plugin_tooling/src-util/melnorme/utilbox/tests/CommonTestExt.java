/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import melnorme.utilbox.core.CoreUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;

/**
 * Some extended functionality to {@link CommonTestUtils}. 
 * May be not be well designed or of interest to most tests. 
 */
public class CommonTestExt extends CommonTest {
	
	public CommonTestExt() {
		super();
	}
	
	/* -------- iteration/checkers -------- */
	
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
	
	public static final Location IGNORE_PATH = PathUtil.DEFAULT_ROOT_LOC.resolve_fromValid("###NO_CHECK###");
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
	
	protected static <V> void checkSetContains(Set<V> set, V expectedValue) {
		assertTrue(set.remove(expectedValue));
	};
	
	/* ----------------- string/parsing ----------------- */
	
	public static String findMatch(String input, String regex) {
		return findMatch(input, regex, 0);
	}
	
	public static String findMatch(String input, String regex, int resultGroup) {
		Matcher matcher = Pattern.compile(regex, Pattern.UNIX_LINES).matcher(input);
		boolean hasFound = matcher.find();
		if(!hasFound) {
			return null;
		}
		return matcher.group(resultGroup);
	}
	
}