/*******************************************************************************
 * Copyright (c) 2014, Bruno Medeiros and other Contributors.
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
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.Location;


public class FindDefinitionResult {
	
	protected final Location fileLocation;
	protected final SourceLineColumnRange sourceRange;
	protected final String infoMessage;
	
	public FindDefinitionResult(Location fileLocation, SourceLineColumnRange sourceRange, String infoMessage) {
		this.fileLocation = assertNotNull(fileLocation);
		this.infoMessage = infoMessage;
		this.sourceRange = assertNotNull(sourceRange);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof FindDefinitionResult)) return false;
		
		FindDefinitionResult other = (FindDefinitionResult) obj;
		
		return 
				areEqual(fileLocation, other.fileLocation) &&
				areEqual(infoMessage, other.infoMessage) &&
				areEqual(sourceRange, other.sourceRange);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(infoMessage, sourceRange);
	}
	
	@Override
	public String toString() {
		return "ResolveResult[" + 
			(infoMessage != null ? infoMessage +"," : "") +
			fileLocation + " " +
			sourceRange.toString() + 
		"]";
	}
	
	/* -----------------  ----------------- */
	
	public String getInfoMessage() {
		return infoMessage;
	}
	
	public SourceLineColumnRange getSourceRange() {
		return sourceRange;
	}
	
	public Location getFileLocation() {
		return fileLocation;
	}
	
}