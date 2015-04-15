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

import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.utilbox.misc.HashcodeUtil;


public class FindDefinitionResult {
	
	protected final String errorMessage;
	protected final String infoMessage;
	protected final SourceLineColumnRange location;
	
	public FindDefinitionResult(String infoMessage, SourceLineColumnRange location) {
		this.errorMessage = null;
		this.infoMessage = infoMessage;
		this.location = location;
	}
	
	public FindDefinitionResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.infoMessage = null;
		this.location = null;
	}
	
	protected boolean isError() {
		return errorMessage != null;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String getInfoMessage() {
		return infoMessage;
	}
	
	public SourceLineColumnRange getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		if(errorMessage != null) {
			return "ResolveResult["+errorMessage+"]";
		}
		
		return "ResolveResult[" + 
			(infoMessage != null ? infoMessage +"," : "") +
			location.toString() + 
			"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof FindDefinitionResult)) return false;
		
		FindDefinitionResult other = (FindDefinitionResult) obj;
		
		return 
				areEqual(errorMessage, other.errorMessage) &&
				areEqual(infoMessage, other.infoMessage) &&
				areEqual(location, other.location);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(errorMessage, infoMessage, location);
	}
	
}