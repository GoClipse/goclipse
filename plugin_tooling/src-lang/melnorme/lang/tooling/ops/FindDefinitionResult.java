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
	
}