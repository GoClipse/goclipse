/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

public class HashcodeUtil {
	
	/** Combines two hash codes to make a new one. */
	public static int combineHashCodes(int hashCode1, int hashCode2) {
		return hashCode1 * 31 + hashCode2;
	}
	
	/** Combines 3 hash codes to make a new one. */
	public static int combineHashCodes(int numA, int numB, int numC) {
		final int prime = 31;
		int result = 1;
		result = prime * result + numA;
		result = prime * result + numB;
		result = prime * result + numC;
		return result;
	}
	
	/** Combines multiple hash codes to make a new one. */
	public static int combineHashCodes(int... nums) {
		final int prime = 31;
		int result = 1;
		for (int num : nums) {
			result = prime * result + num;
		}
		return result;
	}
	
}