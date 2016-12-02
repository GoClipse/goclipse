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
package melnorme.lang.tooling.toolchain.ops;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.HashcodeUtil;

public class ToolMessageData {
		
	public String pathString;
	public String lineString;
	public String columnString;
	public String endLineString;
	public String endColumnString;
	
	public String messageTypeString;
	
	public String sourceBeforeMessageText;
	public String messageText;
	
	// Copy constructor
	public ToolMessageData(ToolMessageData init) {
		this.pathString = init.pathString;
		this.lineString = init.lineString;
		this.columnString = init.columnString;
		this.endLineString = init.endLineString;
		this.endColumnString = init.endColumnString;
		
		this.messageTypeString = init.messageTypeString;
		
		this.sourceBeforeMessageText = init.sourceBeforeMessageText;
		this.messageText = init.messageText;
	}
	
	// Default constructor
	public ToolMessageData() {
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof ToolMessageData)) return false;
		
		ToolMessageData other = (ToolMessageData) obj;
		
		return equalsOther(other);
	}
	
	public boolean equalsOther(ToolMessageData other) {
		return 
			areEqual(pathString, other.pathString) &&
			areEqual(lineString, other.lineString) &&
			areEqual(columnString, other.columnString) &&
			areEqual(endLineString, other.endLineString) &&
			areEqual(endColumnString, other.endColumnString) &&
			areEqual(messageTypeString, other.messageTypeString) &&
			areEqual(sourceBeforeMessageText, other.sourceBeforeMessageText) &&
			areEqual(messageText, other.messageText)
		;
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(
			pathString,
			lineString,
			columnString,
			endLineString,
			endColumnString,
			
			messageTypeString,
			
			sourceBeforeMessageText,
			messageText
		);
	}
	
}