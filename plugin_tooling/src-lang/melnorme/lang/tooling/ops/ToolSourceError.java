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
import static melnorme.utilbox.misc.HashcodeUtil.getHashCode;
import melnorme.utilbox.misc.HashcodeUtil;

public class ToolSourceError {
	
	public final SourceLineColumnLocation location;
	public final String errorMessage;
	
	public ToolSourceError(SourceLineColumnLocation location, String errorMessage) {
		this.location = assertNotNull(location);
		this.errorMessage = assertNotNull(errorMessage);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof ToolSourceError)) return false;
		
		ToolSourceError other = (ToolSourceError) obj;
		
		return areEqual(location, other.location) && areEqual(errorMessage, other.errorMessage);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combineHashCodes(getHashCode(location), getHashCode(errorMessage));
	}
	
	@Override
	public String toString() {
		return location.path + ":" + location.line + ":" + location.column + ": " + errorMessage;
	}
	
}