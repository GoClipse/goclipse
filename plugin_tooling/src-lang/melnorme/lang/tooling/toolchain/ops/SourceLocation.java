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
package melnorme.lang.tooling.toolchain.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.Location;

public class SourceLocation {
	
	// XXX: need to refactor this, and SourceLineColumnRange to a more generic range 
	protected final Location fileLocation;
	protected final SourceLineColumnRange sourceRange;
	
	public SourceLocation(Location fileLocation, SourceLineColumnRange sourceRange) {
		this.fileLocation = assertNotNull(fileLocation);
		this.sourceRange = assertNotNull(sourceRange);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof SourceLocation)) return false;
		
		SourceLocation other = (SourceLocation) obj;
		
		return 
				areEqual(fileLocation, other.fileLocation) &&
				areEqual(sourceRange, other.sourceRange);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(fileLocation, sourceRange);
	}
	
	@Override
	public String toString() {
		return "SourceLocation[" + 
			fileLocation + " @ " +
			sourceRange.toString() + 
		"]";
	}
	
	/* -----------------  ----------------- */
	
	public SourceLineColumnRange getSourceRange() {
		return sourceRange;
	}
	
	public Location getFileLocation() {
		return fileLocation;
	}
	
}