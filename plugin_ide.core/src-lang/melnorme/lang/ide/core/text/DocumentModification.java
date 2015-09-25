/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class DocumentModification {
	
	protected final int offset;
	protected final int length;
	protected final String text;
	
	public DocumentModification(int offset, int length, String text) {
		this.offset = offset;
		this.length = length;
		this.text = text;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLength() {
		return length;
	}
	
	public String getText() {
		return text;
	}
	
	public void apply(IDocument document) throws BadLocationException {
		document.replace(offset, length, text);
	}
	
}