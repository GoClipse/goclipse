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

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.HashcodeUtil;

public class SourceLineColumnRange {
	
	public final Path path;
	public final int line; // 1-based index
	public final int column; // 1-based index
	public final int endLine; // 1-based index
	public final int endColumn; // 1-based index
	
	public SourceLineColumnRange(Path path, int line_1, int column_1, int endLine, int endColumn) {
		this.path = assertNotNull(path);
		this.line = line_1;
		this.column = column_1;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}
	
	public SourceLineColumnRange(Path path, int line_1, int column_1) {
		this(path, line_1, column_1, -1, -1);
	}
	
	public int getLineIndex() {
		return toIndex(line);
	}
	
	public int getColumnIndex() {
		return toIndex(column);
	}
	
	protected static int toIndex(int oneBasedPosition) {
		return oneBasedPosition == -1 ? -1 : oneBasedPosition - 1;
	}
	
	protected static int toValidIndex(int position) throws CommonException {
		if(position <=0 ) {
			throw new CommonException("Invalid position.");
		}
		return position - 1;
	}
	
	public int getValidLineIndex() throws CommonException {
		return toValidIndex(line);
	}
	
	public int getValidColumnIndex() throws CommonException {
		return toValidIndex(column);
	}
	
	public int getValidEndLineIndex() throws CommonException {
		return toValidIndex(endLine);
	}
	
	public int getValidEndColumnIndex() throws CommonException {
		return toValidIndex(endColumn);
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
		SourceLineColumnRange other = (SourceLineColumnRange) obj;
		return 
				line == other.line &&
				column == other.column &&
				endLine == other.endLine &&
				endColumn == other.endColumn &&
				areEqual(path, other.path)
		;
	}
	
	@Override
	public String toString() {
		String startStr = line  + (column == -1 ? "" : ":" + column);
		String endStr = endLine == - 1 ? "" : 
			(" " + endLine + (endColumn == -1 ? "" : ":" + endColumn));
		return "[" + startStr + endStr + "]";
	}
	
}