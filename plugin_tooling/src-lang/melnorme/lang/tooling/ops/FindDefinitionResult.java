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

import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.utilbox.misc.HashcodeUtil;


public class FindDefinitionResult {
	
	protected final String infoMessage;
	protected final SourceLineColumnRange location;
	
	public FindDefinitionResult(String infoMessage, SourceLineColumnRange location) {
		this.infoMessage = infoMessage;
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof FindDefinitionResult)) return false;
		
		FindDefinitionResult other = (FindDefinitionResult) obj;
		
		return 
				areEqual(infoMessage, other.infoMessage) &&
				areEqual(location, other.location);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(infoMessage, location);
	}
	
	@Override
	public String toString() {
		return "ResolveResult[" + 
			(infoMessage != null ? infoMessage +"," : "") +
			location.toString() + 
			"]";
	}
	
	/* -----------------  ----------------- */
	
	public String getInfoMessage() {
		return infoMessage;
	}
	
	public SourceLineColumnRange getLocation() {
		return location;
	}
	
}