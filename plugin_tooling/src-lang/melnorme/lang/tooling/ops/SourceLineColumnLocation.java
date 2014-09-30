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
package melnorme.lang.tooling.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import melnorme.utilbox.misc.HashcodeUtil;

public class SourceLineColumnLocation {
	
	public final Path path;
	public final int line; // 1-based index
	public final int column; // 1-based index
	
	public SourceLineColumnLocation(Path path, int line, int column) {
		this.path = assertNotNull(path);
		this.line = line;
		this.column = column;
	}
	
	public int getLineIndex() {
		return line - 1;
	}
	
	public int getColumnIndex() {
		return column - 1;
	}
	
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combineHashCodes(path.hashCode(), column, line);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		SourceLineColumnLocation other = (SourceLineColumnLocation) obj;
		return 
				column == other.column &&
				line == other.line &&
				areEqual(path, other.path)
		;
	}
	
}