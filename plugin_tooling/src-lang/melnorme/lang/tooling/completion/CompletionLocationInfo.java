/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.completion;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

public class CompletionLocationInfo {
	
	public final int offset; // The location where completion was invoked
	public final String searchPrefix;
	public final int namePrefixLen;
	public final int rplLen;
	
	public CompletionLocationInfo(int offset) {
		this(offset, "", 0);
	}
	
	public CompletionLocationInfo(int offset, String searchPrefix, int rplLen) {
		assertTrue(rplLen >= 0);
		assertTrue(offset >= 0);
		this.offset = offset;
		this.searchPrefix = searchPrefix;
		this.namePrefixLen = searchPrefix.length();
		this.rplLen = rplLen;
	}
	
}