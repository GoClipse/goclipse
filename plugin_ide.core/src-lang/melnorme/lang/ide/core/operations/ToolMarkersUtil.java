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
package melnorme.lang.ide.core.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.tooling.ops.SourceLineColumnRange;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ToolMarkersUtil {
	
	protected boolean readWordForCharEnd;
	
	public ToolMarkersUtil() {
		this(false);
	}
	public ToolMarkersUtil(boolean readWordForCharEnd) {
		this.readWordForCharEnd = readWordForCharEnd;
	}
	
	public void addErrorMarkers(Iterable<ToolSourceMessage> buildErrors, Location rootPath) 
			throws CoreException {
		
		for (ToolSourceMessage buildError : buildErrors) {
			Location loc = rootPath.resolve(buildError.getFilePath()); // Absolute paths will remain unchanged.
			
			IFile[] files = ResourceUtils.getWorkspaceRoot().findFilesForLocationURI(loc.toUri());
			for (IFile file : files) {
				addErrorMarker(file, buildError, LangCore_Actual.BUILD_PROBLEM_ID);
			}
		}
		
	}
	
	public void addErrorMarker(IResource resource, ToolSourceMessage buildmessage, String markerType)
			throws CoreException {
		if(!resource.exists())
			return;
		
		if(buildmessage.getMessageKind() == StatusLevel.OK) {
			return; // Don't add message as a marker.
		}
		
		// TODO: check if marker already exists?
		IMarker marker = resource.createMarker(markerType);
		
		marker.setAttribute(IMarker.SEVERITY, severityFrom(buildmessage.getMessageKind()));
		marker.setAttribute(IMarker.MESSAGE, buildmessage.getMessage());
		
		if(!(resource instanceof IFile)) {
			return;
		}
		
		IFile file = (IFile) resource;
		
		int line = buildmessage.getFileLineNumber();
		if(line >= 0) {
			marker.setAttribute(IMarker.LINE_NUMBER, line);
		}
		
		SourceLineColumnRange range = buildmessage.range;
		
		SourceRange messageSR;
		
		ITextFileBufferManager fileBufferManager = FileBuffers.getTextFileBufferManager();
		fileBufferManager.connect(file.getFullPath(), LocationKind.IFILE, null);
		
		try {
			ITextFileBuffer tfb = fileBufferManager.getTextFileBuffer(file.getFullPath(), LocationKind.IFILE);
			messageSR = getMessageRangeUsingDocInfo(range, tfb.getDocument());
		} finally {
			fileBufferManager.disconnect(file.getFullPath(), LocationKind.IFILE, null);
		}
		
		if(messageSR != null) {
			try {
				marker.setAttribute(IMarker.CHAR_START, messageSR.getStartPos());
				marker.setAttribute(IMarker.CHAR_END, messageSR.getEndPos());
			} catch (CoreException ce) {
				LangCore.logStatus(ce);
			}
		}
		
	}
	
	protected SourceRange getMessageRangeUsingDocInfo(SourceLineColumnRange range, IDocument doc) {
		
		int charStart;
		int charEnd;
		
		int startLine;
		int startColumn;
		
		try {
			try {
				startLine = range.getValidLineIndex();
				startColumn = range.getValidColumnIndex();
				
				charStart = doc.getLineOffset(startLine) + startColumn;
			} catch (CommonException ce) {
				return null;
			}
			
			int endLine;
			int endColumn;
			try {
				endLine = range.getValidEndLineIndex();
				endColumn = range.getValidEndColumnIndex();
				
				charEnd = doc.getLineOffset(endLine) + endColumn;
				
			} catch (CommonException e) {
				charEnd = getCharEnd(charStart, doc);
			}
		} catch(BadLocationException e ) {
			return null;
		}
		
		return SourceRange.srStartToEnd(charStart, charEnd);
	}
	
	protected int getCharEnd(int charStart, IDocument doc) {
		if(!readWordForCharEnd) {
			return charStart + 1;
		}
		
		int ix = charStart;
		try {
			ix++;
			char ch = doc.getChar(ix-1);
			
			if(Character.isDigit(ch)) {
				
				while(Character.isDigit(doc.getChar(ix))) {
					ix++;
				}
				
			} if(Character.isAlphabetic(ch)) {
				
				while(Character.isJavaIdentifierPart(doc.getChar(ix))) {
					ix++;
				}
				
			}
		} catch(BadLocationException e) {
			
		}
		
		return ix;
	}
	
	public static int severityFrom(StatusLevel statusLevel) {
		switch (statusLevel) {
		case ERROR: return IMarker.SEVERITY_ERROR;
		case WARNING: return IMarker.SEVERITY_WARNING;
		case INFO: return IMarker.SEVERITY_INFO;
		case OK: return IMarker.SEVERITY_INFO; // Shouldn't happen
		}
		throw assertFail();
	}
	
}