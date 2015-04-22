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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.misc.CollectionUtil.createHashSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.Assert;
import melnorme.utilbox.core.CoreUtil;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.FileUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.PathUtil;
import melnorme.utilbox.misc.StreamUtil;
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
	
	public static void assertEquals(List<?> list1, List<?> list2) {
		assertAreEqualLists(list1, list2);
	}
	
	/** Helper for interactive debugging: 
	 * Does the same check as {@link List#equals(Object)}, but fails right on the spot where it's not equal. */
	public static void assertAreEqualLists(List<?> list1, List<?> list2) {
		if(list1 == null && list2 == null)
			return;
		
		if(list1.size() != list2.size()) {
			assertFail();
		}
		for (int ix = 0; ix < list1.size(); ix++) {
			Object obj1 = list1.get(ix);
			Object obj2 = list2.get(ix);
			assertAreEqual(obj1, obj2);
		}
	}
	
	public static void assertAreEqualLists(Indexable<?> list1, Indexable<?> list2) {
		if(list1 == null && list2 == null)
			return;
		
		if(list1.size() != list2.size()) {
			assertFail();
		}
		for (int ix = 0; ix < list1.size(); ix++) {
			Object obj1 = list1.get(ix);
			Object obj2 = list2.get(ix);
			assertAreEqual(obj1, obj2);
		}
	}
	
	public static void assertEqualSet(Set<?> result, Set<?> expected) {
		boolean equals = result.equals(expected);
		if(equals) {
			return;
		}
		HashSet<?> resultExtra = removeAllCopy(result, expected);
		HashSet<?> expectedMissing = removeAllCopy(expected, result);
		assertTrue(equals,
				"Obtained result set not equal to expected set. \n" +
				"--- Extra elements in result set ("+resultExtra.size()+") : --- \n" +
				StringUtil.collToString(resultExtra, "\n") + "\n" +
				"--- Extra elements in expected set ("+expectedMissing.size()+") : --- \n" +
				StringUtil.collToString(expectedMissing, "\n") + "\n" +
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
	
	public static <T> void assertStringContains(String string, CharSequence expectedContains) {
		assertTrue(string != null && string.contains(expectedContains));
	}
	
	/* ---- */
	
	public static <T> HashSet<T> removeAllCopy(Set<T> set, Collection<?> removeColl) {
		return removeAll(createHashSet(set), removeColl);
	}
	
	public static <E, T extends Set<E>> T removeAll(T set, Collection<?> removeColl) {
		set.removeAll(removeColl);
		return set;
	}
	
	/* ----------------- util constructors ----------------- */
	
	@SafeVarargs
	public static <T> T[] array(T... elems) {
		return elems;
	}
	
	@SafeVarargs
	public static String[] strings(String... elems) {
		return elems;
	}
	
	
	@SafeVarargs
	public static <T> ArrayList2<T> list(T... elems) {
		return new ArrayList2<>(elems);
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
	
	public static String safeToString(Object obj) {
		return obj == null ? null : obj.toString();
	}
	
	/* ----------------- path utils ----------------- */
	
	public static Path path(String pathString) {
		return PathUtil.createPathOrNull(pathString);
	}
	
	public static Location loc(String pathString) {
		Path createValidPath = PathUtil.createValidPath(pathString);
		return loc(createValidPath);
	}
	public static Location loc(Path absolutePath) {
		return Location.fromAbsolutePath(absolutePath);
	}
	
	public static Location loc(Location baseLoc, String pathString) {
		return baseLoc.resolve(PathUtil.createValidPath(pathString));
	}
	
	public static Location workingDirLoc(String relativePath) {
		return TestsWorkingDir.getWorkingDir(relativePath);
	}
	
	/* -------------  Resources stuff   ------------ */
	
	public static final Charset DEFAULT_TESTDATA_ENCODING = StringUtil.UTF8;
	
	public static String readStringFromFile(Path path) {
		return readStringFromFile(Location.create_fromValid(path));
	}
	public static String readStringFromFile(File file) {
		return readStringFromFile(Location.create_fromValid(file.toPath()));
	}
	public static String readStringFromFile(Location loc) {
		try {
			return FileUtil.readStringFromFile(loc.toFile(), DEFAULT_TESTDATA_ENCODING);
		} catch (IOException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}	
	
	public static void writeStringToFile(Path file, String string) {
		writeStringToFile(Location.create_fromValid(file), string);
	}
	public static void writeStringToFile(File file, String string) {
		writeStringToFile(Location.create_fromValid(file.toPath()), string);
	}
	public static void writeStringToFile(Location file, String string) {
		try {
			StreamUtil.writeStringToStream(string, new FileOutputStream(file.toFile()), DEFAULT_TESTDATA_ENCODING);
		} catch (IOException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	public static void appendStringToFile(File file, String string) {
		try {
			StreamUtil.writeStringToStream(string, new FileOutputStream(file, true), DEFAULT_TESTDATA_ENCODING);
		} catch (IOException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	public static String getClassResourceAsString(Class<?> klass, String resourceName) {
		return MiscUtil.getClassResourceAsString(klass, resourceName);
	}
	
}