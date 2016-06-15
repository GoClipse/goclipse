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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Optional;

import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.parser.SourceLinesInfo;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

/**
 * Parameters for a source operation. This data from this class is immutable.
 */
public class SourceOpContext {
	
	public static final String MSG_NoFileLocationForThisOperation = "No file location for this operation";
	
	protected final Optional<Location> fileLocation;
	protected final String source;
	protected final int offset;
	protected final SourceRange selection;
	protected final boolean isDirty;
	
	public SourceOpContext(Optional<Location> fileLocation, int offset, String source, boolean isDirty) {
		this(fileLocation, offset, new SourceRange(offset, 0),source, isDirty);
	}
	
	public SourceOpContext(Optional<Location> fileLocation, int offset, SourceRange selection, String source, 
			boolean isDirty) {
		this.fileLocation = fileLocation;
		this.offset = offset;
		this.selection = assertNotNull(selection);
		this.source = assertNotNull(source);
		this.isDirty = isDirty;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public SourceRange getSelection() {
		return selection;
	}
	
	public Optional<Location> getOptionalFileLocation() {
		return fileLocation;
	}
	
	public Location getFileLocation() throws CommonException {
		if(fileLocation.isPresent()) {
			return fileLocation.get();
		} else {
			throw new CommonException(MSG_NoFileLocationForThisOperation);
		}
	}
	
	public String getSource() {
		return source;
	}
	
	public SourceRange getOperationRange() {
		return selection;
	}
	
	public boolean isDocumentDirty() {
		return isDirty;
	}
	
	private SourceLinesInfo sourceLinesInfo;
	
	public synchronized SourceLinesInfo getSourceLinesInfo() {
		if(sourceLinesInfo == null) {
			this.sourceLinesInfo = new SourceLinesInfo(source);
		}
		return sourceLinesInfo;
	}
	
	public int getInvocationLine_0() throws CommonException {
		return getSourceLinesInfo().getLineForOffset(getOffset());
	}
	
	public int getInvocationColumn_0() throws CommonException {
		return getSourceLinesInfo().getColumnForOffset(getOffset());
	}
	
}