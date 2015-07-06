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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.misc.StreamUtil.readAllBytesFromStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import java.util.function.Predicate;
import melnorme.utilbox.ownership.IDisposable;

public class MiscUtil extends PathUtil {
	
	/* ----------------- System ----------------- */
	
	public static final String OS_NAME = StringUtil.nullAsEmpty(System.getProperty("os.name"));
	
	public static final boolean OS_IS_WINDOWS = OS_NAME.startsWith("Windows");
	public static final boolean OS_IS_LINUX = OS_NAME.startsWith("Linux") || OS_NAME.startsWith("LINUX");
	public static final boolean OS_IS_MAC = OS_NAME.startsWith("Mac");
	
	
	public static String getSystemProperty(String propName, String defaultValue) {
		String propValue = System.getProperty(propName);
		if(propValue == null) {
			return defaultValue;
		}
		return propValue;
	}
	
	public static boolean getSystemProperty(String propName, boolean defaultValue) {
		String propValue = System.getProperty(propName);
		if(propValue == null) {
			return defaultValue;
		}
		return propValue != null;
	}
	
	/* -----------------  ----------------- */
	
	public static <T> Predicate<T> getNotNullPredicate() {
		return new NotNullPredicate<T>();
	}
	
	public static final class NotNullPredicate<T> implements Predicate<T> {
		@Override
		public boolean test(T obj) {
			return obj != null;
		}
	}
	
	public static <T> Predicate<T> getIsNullPredicate() {
		return new IsNullPredicate<T>();
	}
	
	public static final class IsNullPredicate<T> implements Predicate<T> {
		@Override
		public boolean test(T obj) {
			return obj == null;
		}
	}
	
	/** Loads given klass. */
	public static void loadClass(Class<?> klass) {
		try {
			// We use klass.getClassLoader(), in case klass cannot be loaded in the default (caller) classloader
			// that could happen in OSGi runtimes for example
			Class.forName(klass.getName(), true, klass.getClassLoader());
		} catch (ClassNotFoundException e) {
			assertFail();
		}
	}
	
	/** Combines two hash codes to make a new one. */
	public static int combineHashCodes(int hashCode1, int hashCode2) {
		return HashcodeUtil.combineHashCodes(hashCode1, hashCode2);
	}
	
	/** Returns the first element of objs array that is not null.
	 * At least one element must be non-null. */
	@SafeVarargs
	public static <T> T firstNonNull(T... objs) {
		for (int i = 0; i < objs.length; i++) {
			if(objs[i] != null)
				return objs[i];
		}
		assertFail();
		return null;
	}
	
	/** Convenience method for extracting the element of a single element collection . */
	public static <T> T getSingleElement(Collection<T> singletonDefunits) {
		assertTrue(singletonDefunits.size() == 1);
		return singletonDefunits.iterator().next();
	}
	
	/** Synchronizes on the given collection, and returns a copy suitable for iteration. */
	public static <T> Iterable<T> synchronizedCreateIterable(Collection<T> collection) {
		Iterable<T> iterable;
		synchronized (collection) {
			iterable = new ArrayList<T>(collection);
		}
		return iterable;
	}
	
	/** Sleeps current thread for given millis amount.
	 * If interrupted throws an unchecked exception. */
	public static void sleepUnchecked(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	public static <T> T nullToOther(T object, T altValue) {
		return object == null ? altValue : object;
	}
	
	/** @return true if given throwable is a Java unchecked throwable, false otherwise. */
	public static boolean isUncheckedException(Throwable throwable) {
		return throwable instanceof RuntimeException || throwable instanceof Error;
	}
	
	public static String getClassResourceAsString(Class<?> klass, String resourceName) {
		return getClassResourceAsString(klass, resourceName, StringUtil.UTF8);
	}
	
	public static String getClassResourceAsString(Class<?> klass, String resourceName, Charset charset) {
		try {
			InputStream resourceStream = klass.getResourceAsStream(resourceName);
			assertNotNull(resourceStream);
			return readAllBytesFromStream(resourceStream).toString(charset);
		} catch (IOException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	/** Dispose given disposable, and return null. Mean to be used for variables. */
	public static <T extends IDisposable> T dispose(T disposable) {
		if (disposable != null) {
			disposable.dispose();
		}
		return null;
	}
	
}