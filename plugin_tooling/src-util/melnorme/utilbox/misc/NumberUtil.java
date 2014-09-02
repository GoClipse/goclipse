/*******************************************************************************
 * Copyright (c) 2007, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;


public class NumberUtil {
	
	/** @return if given number is contained in range [min, max] (inclusive). */
	public static boolean isInRange(int min, int number, int max) {
		assertTrue(min <= max);
		return min <= number && number <= max; 
	}
	
	/** @return if given number is contained in range ]min, max[ (exclusive). */
	public static boolean isInsideRange(int min, int number, int max) {
		assertTrue(min <= max);
		return min < number && number < max; 
	}
	
	/** Caps given number betwen given min and max, inclusive. */
	public static int capBetween(int min, int number, int max) {
		assertTrue(min <= max);
		return Math.min(max, Math.max(min, number));
	}

	/** @return the number closest to zero, from given a and b */
	public static int nearestToZero(int a, int b) {
		if(Math.abs(a) < Math.abs(b))
			return a;
		else 
			return b;
	}

	/** @return the number farther to zero, from given a and b */
	public static int fartherFromZero(int a, int b) {
		if(Math.abs(a) > Math.abs(b))
			return a;
		else 
			return b;
	}
	
	/** @return {@link Integer#parseInt(String)}, but with a checked exception. */
	public static int parseInt(String string) throws NumberFormatException_ {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException_(nfe);
		}
	}
	
	/** Checked analogue/wrapper for {@link NumberFormatException} */
	public static class NumberFormatException_ extends Exception {
		
		private static final long serialVersionUID = 1L;
		
		public NumberFormatException_(NumberFormatException exc) {
			super(exc);
		}
		
	}
	
}