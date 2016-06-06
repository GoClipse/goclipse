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
package melnorme.lang.ide.core.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class DocumentSourceOpContext extends SourceOpContext {
	
	protected final boolean isDirty;
	protected final int line_0;
	protected final int col_0;
	
	public DocumentSourceOpContext(Location fileLocation, int offset, IDocument document, boolean isDirty) {
		super(fileLocation, offset, document.get());
		
		this.isDirty = isDirty;
		
		int line_0;
		int col_0;
		
		try {
			line_0 = document.getLineOfOffset(offset);
			col_0 = offset - document.getLineInformation(line_0).getOffset();
		} catch(BadLocationException e) {
			line_0 = -1;
			col_0 = -1;
		}
		this.line_0 = line_0;
		this.col_0 = col_0;
	}
	
	public boolean isDocumentDirty() {
		return isDirty;
	}
	
	public int getInvocationLine_0() throws CommonException {
		if(line_0 == -1) {
			throw new CommonException("Could not determine line position.");
		}
		return line_0;
	}
	
	public int getInvocationColumn_0() throws CommonException {
		if(col_0 == -1) {
			throw new CommonException("Could not determine column position.");
		}
		return col_0;
	}
	
}