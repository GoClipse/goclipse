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
package melnorme.lang.tooling.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.misc.HashcodeUtil.getHashCode;

import java.nio.file.Path;

import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.status.Severity;

public class ToolSourceMessage {
	
	public final Path path;
	public final SourceLineColumnRange range;
	public final Severity severity;
	public final String message;
	
	public ToolSourceMessage(Path path, SourceLineColumnRange range, Severity severity, String message) {
		this.path = assertNotNull(path);
		this.range = assertNotNull(range);
		this.message = assertNotNull(message);
		this.severity = assertNotNull(severity);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof ToolSourceMessage)) return false;
		
		ToolSourceMessage other = (ToolSourceMessage) obj;
		
		return 
			areEqual(path, other.path) &&
			areEqual(range, other.range) && 
			areEqual(severity, other.severity) && 
			areEqual(message, other.message);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combineHashCodes(getHashCode(range), getHashCode(message));
	}
	
	@Override
	public String toString() {
		return range + " " + severity + ": "+ message;
	}
	
	/* -----------------  ----------------- */ 
	
	public Path getFilePath() {
		return path;
	}
	
	public int getFileLineNumber() {
		return range.line;
	}
	
	public int getFileColumnNumber() {
		return range.column;
	}
	
	public Severity getSeverity() {
		return severity;
	}
	
	public String getMessage() {
		return message;
	}
	
}