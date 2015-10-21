/*******************************************************************************
 * Copyright (c) 2007 Bruno Medeiros and other Contributors.
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
import melnorme.utilbox.core.CommonException;


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
	
	/* ----------------- Parsing helpers (they use checked exceptions) ----------------- */
	
	public static int parseInt(String integerString) throws CommonException {
		try {
			return Integer.parseInt(integerString);
		} catch (NumberFormatException e) {
			throw new CommonException("Invalid integer: `" + integerString + "`");
		}
	}
	
	public static int parseInt(String integerString, String errorMessage) throws CommonException {
		try {
			return Integer.parseInt(integerString);
		} catch (NumberFormatException e) {
			throw new CommonException(errorMessage, null);
		}
	}
	
	public static int parsePositiveInt(String integerString) throws CommonException {
		int integer = parseInt(integerString);
		if(integer < 0) {
			throw new CommonException("Integer is not positive: " + integerString);
		}
		return integer;
	}
	
	public static boolean isDigit(char ch, int radix) {
		char maxNumberDigit = (char) Math.min('0' + (radix-1), '9');
		
		if(ch >= '0' && ch <= maxNumberDigit) {
			return true;
		}
		
		if(radix > 10) {
			
			if(
				(ch >= 'a' && ch < 'a' + (radix-10) ) ||
				(ch >= 'A' && ch < 'A' + (radix-10) )
			) {
				return true;
			}
	
		}
		
		return false;
	}
	
}