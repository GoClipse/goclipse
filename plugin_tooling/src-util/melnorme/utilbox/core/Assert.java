/*******************************************************************************
 * Copyright (c) 2010 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.core;

import static melnorme.utilbox.core.CoreUtil.areEqual;


/**
 * Contains utility methods for assertion contract checking .
 * 
 * Each method in this class, such as 'isTrue', has an identical method
 * with the 'assert' prefix, (ie, assertTrue). These later methods are named this way so as to be
 * use with Java's static imports, and are actually the preferred usage. 
 * They are designed to be placed as a favorite in JDT's Content Assist preferences.
 * 
 * This class can be used without OSGi running.
 */
public class Assert {
	
	protected static final AssertHandler assertHandler; // Poor mans dependency injection
	
	static {
		String assertHandlerStr = System.getProperty(Assert.class.getName() + ".handler");
		if(assertHandlerStr == null) {
			assertHandler = new AssertHandler();
		} else if(assertHandlerStr.equals("disable") || assertHandlerStr.equals("null")) {
			assertHandler = null; // No op handler
		} else {
			try {
				final Class<?> klass = Class.forName(assertHandlerStr);
				Object handler = klass.newInstance();
				if(handler instanceof Runnable) {
					final Runnable runnableHandler = (Runnable) handler;
					assertHandler = new AssertHandler() {
						@Override 
						protected void handleAssert(String message) {
							runnableHandler.run();
						};
					};
				} else {
					assertHandler = CoreUtil.downCast(handler);
				}
			} catch(Exception e) {
				throw new RuntimeException(e); // Yes, let our classloading fail.
			}
		}
	}
	
	/** Default implementation of the assert handler. */
	public static class AssertHandler {
		protected void handleAssert(String message) {
			throw new AssertFailedException(message);
		}
	}
	
	/** 
	 * The class thrown by the default assertion failure handler.
	 * Clients should not expect this to be thrown.
	 */
	@SuppressWarnings("serial")
	protected static class AssertFailedException extends RuntimeException {
		
		public AssertFailedException(String message) {
			super(message);
		}
		
		@Override
		public String toString() {
			String message = getLocalizedMessage();
			return AssertFailedException.class.getSimpleName() 
				+ ((message == null) ? "" : (": " + message));  //$NON-NLS-1$
		}
		
	}
	
	
	protected static void checkAssertion(boolean condition, String message) {
		if(assertHandler != null && condition == false) {
			assertHandler.handleAssert(message);  // USEFUL TIP: place Breakpoint here
		}
	}
	
	
	/** Asserts if given condition is true or not. If it is not, call assert handler.
	 * Default handler behavior is to throw an {@link AssertFailedException} with given message.
	 */
	public static void isTrue(boolean condition, String message) {
		checkAssertion(condition, message);
	}
	/** Like {@link Assert#isTrue(boolean, String)} with no message */
	public static void isTrue(boolean expression) {
		checkAssertion(expression, null);
	}
	
	
	/** Asserts that the given object is not null, with given message */
	public static void isNotNull(Object object, String message) {
		checkAssertion(!(object == null), message);
	}
	/** Like {@link Assert#isNotNull(Object, String)} with no message. */
	public static void isNotNull(Object object) {
		checkAssertion(!(object == null), null);
	}
	
	
	/** Asserts that given object1 equals object2. */
	public static void equals(Object object1, Object object2) {
		checkAssertion(object1.equals(object2), null);
	}
	
	
	/** Causes an inconditional assertion failure, with given message. 
	 * Will always call the assertion handler, and return an RuntimeException if nothing is thrown. */
	public static RuntimeException fail(String message) {
		checkAssertion(false, message);
		return new AssertFailedException(message);
	}
	/** Like {@link Assert#fail(String)} with no message. */
	public static RuntimeException fail() {
		checkAssertion(false, null);
		return new AssertFailedException(null);
	}
	
	
	/** Like {@link Assert#fail(String)}, but specifically signals unreachable code */
	public static RuntimeException unreachable() {
		return fail("Unreachable code."); //$NON-NLS-1$
	}
	
	
	/** A namespace class for holding assert methods to use as static imports. */
	public static class AssertNamespace {
		/** Asserts if given condition is true or not. If it is not, call assert handler.
		 * Default handler behavior is to throw an {@link AssertFailedException} with given message.
		 */
		public static void assertTrue(boolean condition, String message) {
			checkAssertion(condition, message);
		}
		/** Like {@link Assert#isTrue(boolean, String)} with no message */
		public static void assertTrue(boolean expression) {
			checkAssertion(expression, null);
		}
		
		
		/** Asserts that the given object is not null, with given message */
		public static void assertNotNull(Object object, String message) {
			checkAssertion(!(object == null), message);
		}
		/** Like {@link Assert#isNotNull(Object, String)} with no message. */
		public static <T> T assertNotNull(T object) {
			checkAssertion(!(object == null), null);
			return object;
		}
		
		
		/** Asserts that given object1 equals object2. */
		public static void assertEquals(Object object1, Object object2) {
			checkAssertion(object1.equals(object2), null);
		}
		
		public static void assertAreEqual(Object object1, Object object2) {
			checkAssertion(areEqual(object1, object2), null);
		}
		
		
		/** Causes an inconditional assertion failure, with given message. 
		 * Will always call the assertion handler, and return an RuntimeException if nothing is thrown. */
		public static RuntimeException assertFail(String message) {
			checkAssertion(false, message);
			return new AssertFailedException(message);
		}
		/** Like {@link Assert#fail(String)} with no message. */
		public static RuntimeException assertFail() {
			checkAssertion(false, null);
			return new AssertFailedException(null);
		}
		
		
		/** Like {@link Assert#fail(String)}, but specifically signals unreachable code */
		public static RuntimeException assertUnreachable() {
			return fail("Unreachable code."); //$NON-NLS-1$
		}
	}
	
}
