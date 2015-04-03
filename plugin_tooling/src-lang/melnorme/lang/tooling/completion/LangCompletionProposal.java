/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.completion;


public class LangCompletionProposal {
	
	protected final int completionOffset;
	protected final String replaceString;
	protected final int replaceLength;
	protected final String label;
	
	protected LangCompletionProposal(int completionLocation, String replaceString, int replaceLength, String label) {
		this.completionOffset = completionLocation;
		this.replaceString = replaceString;
		this.replaceLength = replaceLength;
		this.label = label;
	}
	
	public Object getExtraInfo() {
		return null;
	}
	
	public String getReplaceString() {
		return replaceString;
	}
	
	public int getReplaceStart() {
		return completionOffset;
	}
	
	public int getReplaceLength() {
		return replaceLength;
	}
	
}