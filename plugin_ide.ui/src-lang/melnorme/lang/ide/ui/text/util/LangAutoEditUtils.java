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
package melnorme.lang.ide.ui.text.util;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

public class LangAutoEditUtils {
	
	public static String stringNTimes(String string, int count) {
		assertTrue(count >=0);
		StringBuffer result = new StringBuffer(string.length() * count);
		for (int i = 0; i < count; i++) {
			result.append(string);
		}
		return result.toString();
	}
	
}