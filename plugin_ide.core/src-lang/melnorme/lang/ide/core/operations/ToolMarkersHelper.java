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

import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.ops.SourceLineColumnRange;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.FileUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

public class ToolMarkersHelper {
	
	protected boolean readWordForCharEnd;
	
	public ToolMarkersHelper() {
		this(false);
	}
	public ToolMarkersHelper(boolean readWordForCharEnd) {
		this.readWordForCharEnd = readWordForCharEnd;
	}
	
	protected final HashMap2<Path, Document> documents = new HashMap2<>();
	
	public void addErrorMarkers(Iterable<ToolSourceMessage> buildErrors, Location rootPath, IProgressMonitor pm) 
			throws CoreException {
		
		documents.clear();
		
		ResourceUtils.getWorkspace().run(new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				for(ToolSourceMessage buildError : buildErrors) {
					if(pm.isCanceled()) {
						return;
					}
					addErrorMarkers(buildError, rootPath);
				}
			}
		}, ResourceUtils.getWorkspaceRoot(), IWorkspace.AVOID_UPDATE, pm);
		
	}
	
	public void addErrorMarkers(ToolSourceMessage toolMessage, Location rootPath) throws CoreException {
		Location loc = rootPath.resolve(toolMessage.getFilePath()); // Absolute paths will remain unchanged.
		
		IFile[] files = ResourceUtils.getWorkspaceRoot().findFilesForLocationURI(loc.toUri());
		for(IFile file : files) {
			addErrorMarker(file, toolMessage, getMarkerType());
		}
	}
	
	protected String getMarkerType() {
		return LangCore_Actual.BUILD_PROBLEM_ID;
	}
	
	public void addErrorMarker(IResource resource, ToolSourceMessage toolMessage, String markerType)
			throws CoreException {
		if(!resource.exists())
			return;
		
		// TODO: check if marker already exists?
		IMarker marker = resource.createMarker(markerType);
		
		marker.setAttribute(IMarker.SEVERITY, markerSeverityFrom(toolMessage.getSeverity()));
		marker.setAttribute(IMarker.MESSAGE, toolMessage.getMessage());
		
		if(!(resource instanceof IFile)) {
			return;
		}
		
		IFile file = (IFile) resource;
		
		int line = toolMessage.getFileLineNumber();
		if(line >= 0) {
			marker.setAttribute(IMarker.LINE_NUMBER, line);
		}
		
		SourceLineColumnRange range = toolMessage.range;
		
		SourceRange messageSR;
		
		try {
			Document doc = getDocumentForLocation(file);
			messageSR = getMessageRangeUsingDocInfo(range, doc);
		} catch(IOException e) {
			return;
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
	
	protected Document getDocumentForLocation(IFile file) throws IOException {
		Path filePath = file.getLocation().toFile().toPath();
		if(documents.containsKey(filePath)) {
			return documents.get(filePath);
		}
		String fileContents = FileUtil.readStringFromFile(filePath, StringUtil.UTF8);
		Document document = new Document(fileContents);
		documents.put(filePath, document);
		return document;
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
	
	public static int markerSeverityFrom(Severity severity) {
		switch (severity) {
		case ERROR: return IMarker.SEVERITY_ERROR;
		case WARNING: return IMarker.SEVERITY_WARNING;
		case INFO: return IMarker.SEVERITY_INFO;
		}
		throw assertFail();
	}
	
}