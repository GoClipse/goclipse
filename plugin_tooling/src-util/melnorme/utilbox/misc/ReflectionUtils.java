/*******************************************************************************
 * Copyright (c) 2010 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Miscellaneous very simple utility methods for using reflection.
 * Some of these methods have very low type safety, and as such may not be suitable for production code.
 */
public class ReflectionUtils {
	
	/** Creates a new instance of class with given klassName. 
	 * Returns null if the instance could not be created */
	public static Object newInstanceSafe(final String klassName){
		final Class<?> klass = ReflectionUtils.loadClassSafe(klassName);
		return klass == null ? null : ReflectionUtils.newInstanceSafe(klass);
	}
	
	/** Creates a new instance of given klass. 
	 * Returns null if the instance could not be created. */
	public static Object newInstanceSafe(final Class<?> klass) {
		try {
			return klass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	
	
	/** Loads a class with given klassName. 
	 * Returns null if the class could not be loaded. */
	public static Class<?> loadClassSafe(final String klassName) {
		try {
			return Class.forName(klassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/* ---------------------------------------------------------------- */
	
	/** Same as {@link Class#getMethod(String, Class...)} but unchecks the exceptions. */
	public static Method uncheckedGetMethod(Class<?> klass, String methodName, Class<?>... parameterTypes) {
		try {
			return klass.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} catch (SecurityException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} 
	}
	
	/** Same as {@link Method#invoke(Object, Object...)} but unchecks the exceptions. */
	public static <T> T uncheckedInvoke(Object obj, Method method, Object... args) {
		try {
			@SuppressWarnings("unchecked")
			T result = (T) method.invoke(obj, args);
			return result;
		} catch (IllegalArgumentException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} catch (IllegalAccessException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} catch (InvocationTargetException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e.getTargetException());
		}
	}
	
	/** Invoke method with given methodName on given obj receiver, using given args */
	public static <T> T invokeMethod(Object obj, String methodName, Object... args) {
		try {
			Class<?>[] paramTypes = new Class<?>[args.length];
			for(int i = 0; i < paramTypes.length; i++) {
				paramTypes[i] = args[i].getClass();
			}
			Method method = getAvailableMethod(obj.getClass(), methodName, paramTypes);
			@SuppressWarnings("unchecked")
			T result = (T) method.invoke(obj, args);
			return result;
		} catch (IllegalArgumentException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} catch (IllegalAccessException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} catch (InvocationTargetException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e.getTargetException());
		}
	}
	
	/** Reads the method with given methodName and given parameterTypes in given klass. */
	public static Method getAvailableMethod(Class<?> klass, String methodName, Class<?>... paramTypes) {
		try {
			return klass.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			return getDeclaredMethodInHierarchy(klass, methodName, paramTypes);
		}
	}
	
	private static Method getDeclaredMethodInHierarchy(Class<?> klass, String methodName, Class<?>... paramTypes) {
		try {
			Method field = klass.getDeclaredMethod(methodName, paramTypes); 
			field.setAccessible(true);
			return field;
		} catch (NoSuchMethodException e) {
			klass = klass.getSuperclass();
			if(klass == null) {
				return null;
			} else {
				return getAvailableMethod(klass, methodName);
			}
		}
	}
	
	/* ---------------------------------------------------------------- */
	
	/** Reads the field with given fieldName in given object. */
	public static <T> Object readField(T object, String fieldName) throws NoSuchFieldException {
		return readAvailableField(object.getClass(), object, fieldName);
	}
	
	/** Reads the static field with given fieldName in given klass. */
	public static Object readStaticField(Class<?> klass, String fieldName) throws NoSuchFieldException {
		return readAvailableField(klass, null, fieldName);
	}
	
	private static <T> Object readAvailableField(Class<?> klass, T object, String fieldName) throws NoSuchFieldException {
		Field field = getAvailableField(klass, fieldName);
		if (field == null) {
			throw new NoSuchFieldException();
		}
		
		try {
			return field.get(object);
		} catch (IllegalArgumentException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} catch (IllegalAccessException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	/** Write the field with given fieldName in given object to given value. */
	public static <T> void writeField(Object object, String fieldName, Object value) throws NoSuchFieldException {
		writeAvailableField(object.getClass(), object, fieldName, value);
	}
	
	/** Write the static field with given fieldName in given klass to given value. */
	public static void writeStaticField(Class<?> klass, String fieldName, Object value) throws NoSuchFieldException {
		writeAvailableField(klass, null, fieldName, value);
	}
	
	private static <T> void writeAvailableField(Class<?> klass, T object, String fieldName, T value) 
			throws NoSuchFieldException {
		Field field = getAvailableField(klass, fieldName);
		if (field == null) 
			throw new NoSuchFieldException();

		try {
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} catch (IllegalAccessException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	private static Field getAvailableField(Class<?> klass, String fieldName) {
		try {
			return klass.getField(fieldName);
		} catch (NoSuchFieldException e) {
			return getDeclaredFieldInHierarchy(klass, fieldName);
		}
	}
	
	private static Field getDeclaredFieldInHierarchy(Class<?> klass, String fieldName) {
		try {
			Field field = klass.getDeclaredField(fieldName); 
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			klass = klass.getSuperclass();
			if(klass == null) {
				return null;
			} else {
				return getAvailableField(klass, fieldName);
			}
		}
	}
}
