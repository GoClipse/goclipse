/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.HashcodeUtil;

public class LineColumnPosition {
	
	public final int line;
	public final int column;
	
	public LineColumnPosition(int line_0, int column_0) {
		this.line = line_0;
		this.column = column_0;
		assertTrue(line_0 >= 0);
		assertTrue(column_0 >= 0);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof LineColumnPosition)) return false;
		
		LineColumnPosition other = (LineColumnPosition) obj;
		
		return 
				areEqual(line, other.line) &&
				areEqual(column, other.column);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(line, column);
	}
	
}