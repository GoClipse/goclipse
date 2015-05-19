/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.engine;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureUpdateTask;
import melnorme.lang.ide.core.utils.DefaultBufferListener;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.SimpleLogger;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

public abstract class EngineClient {
	
	public static SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	protected final StructureModelManager structureManager = init_createStructureModelManager();
	protected final IFileBufferListener fileBufferListener = init_createTextBuffersListener();
	protected final ProblemMarkerUpdater problemUpdater = init_ProblemUpdater();
	
	public EngineClient() {
		FileBuffers.getTextFileBufferManager().addFileBufferListener(fileBufferListener);
		problemUpdater.install(structureManager);
	}
	
	public StructureModelManager getStructureManager() {
		return structureManager;
	}
	
	protected StructureModelManager init_createStructureModelManager() {
		return new StructureModelManager();
	}
	
	protected ProblemMarkerUpdater init_ProblemUpdater() {
		return new ProblemMarkerUpdater();
	}
	
	public void shutdown() {
		problemUpdater.dispose();
		FileBuffers.getTextFileBufferManager().removeFileBufferListener(fileBufferListener);
		structureManager.dispose();
	}
	
	protected TextFileBuffersListener init_createTextBuffersListener() {
		return new TextFileBuffersListener() {
			
			@Override
			protected void bufferCreated(ITextFileBuffer textFileBuffer, Location fileLoc) {
				super.bufferCreated(textFileBuffer, fileLoc);
				
				// Initial update.
				handleDocumentChange(fileLoc, textFileBuffer);
			}
			
			@Override
			protected void documentChanged(ITextFileBuffer textFileBuffer, Location fileLoc, DocumentEvent event) {
				handleDocumentChange(fileLoc, textFileBuffer);
			}
			
			@Override
			protected void bufferDisposed(ITextFileBuffer textFileBuffer, Location fileLoc) {
				handleBufferDisposed(textFileBuffer, fileLoc);
			}
			
		};
	}
	
	protected void handleDocumentChange(Location fileLoc, ITextFileBuffer textFileBuffer) {
		IDocument document = textFileBuffer.getDocument();
		boolean isDocumentDirty = textFileBuffer.isDirty();
		
		StructureInfo structureInfo = structureManager.getStructureInfo(fileLoc);
		structureInfo.queueUpdateTask(createUpdateTask(structureInfo, fileLoc, document, isDocumentDirty));
	}
	
	/**
	 * Create an update task for the given structureInfo.
	 * 
	 * @param isDirty whether the source is consistent with the underlying file or not.
	 * @param source the source for the given document
	 */
	protected abstract StructureUpdateTask createUpdateTask(StructureInfo structureInfo, Location fileLocation, 
			IDocument document, boolean isDirty);
	
	
	protected void handleBufferDisposed(ITextFileBuffer buffer, Location fileLoc) {
		log.println("bufferDisposed: " + buffer.getLocation());
		
		// For correctness this requires that there no pending document listeners being notified
		// and no further document changes will occur.
		
		StructureInfo structureInfo = structureManager.getStructureInfo(fileLoc);
		structureInfo.queueUpdateTask(createDisposeTask(structureInfo, fileLoc));
	}
	
	protected abstract StructureUpdateTask createDisposeTask(StructureInfo structureInfo, Location fileLocation);
	
	/* -----------------  ----------------- */
	
	public void awaitUpdatedWorkingCopy(Object modelKey, IProgressMonitor pm) throws OperationCancellation {
		// TODO: this could be update to await until server responds (meaning that it's working copy is updated), 
		// no need to actually wait for structure to be parsed. 
		StructureInfo structureInfo = getStructureManager().getStructureInfo(modelKey);
		structureInfo.getCurrentStructure(pm);
	}
	
	/* ----------------- util ----------------- */
	
	protected Location getLocation(Object key) {
		if(key instanceof Location) {
			return (Location) key;
		}
		return null;
	}
	
}

class TextFileBuffersListener extends DefaultBufferListener {
	
	@Override
	public void bufferCreated(IFileBuffer buffer) {
		if(buffer instanceof ITextFileBuffer) {
			ITextFileBuffer textFileBuffer = (ITextFileBuffer) buffer;
			
			try {
				Location fileLoc = ResourceUtils.getFileBufferLocation(buffer);
				bufferCreated(textFileBuffer, fileLoc);
			} catch(CoreException e) {
				LangCore.logInternalError(e);
			}
		}
	}
	
	@Override
	public void bufferDisposed(IFileBuffer buffer) {
		if(buffer instanceof ITextFileBuffer) {
			ITextFileBuffer textFileBuffer = (ITextFileBuffer) buffer;
			
			try {
				Location fileLoc = ResourceUtils.getFileBufferLocation(buffer);
				bufferDisposed(textFileBuffer, fileLoc);
			} catch(CoreException e) {
				LangCore.logInternalError(e);
			}
		}
	}
	
	
	protected void bufferCreated(final ITextFileBuffer textFileBuffer, final Location fileLoc) {
		IDocument doc = textFileBuffer.getDocument();
		
		doc.addDocumentListener(new IDocumentListener() {
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
			
			@Override
			public void documentChanged(DocumentEvent event) {
				TextFileBuffersListener.this.documentChanged(textFileBuffer, fileLoc, event);
			}
		});
	}
	
	@SuppressWarnings("unused")
	protected void documentChanged(ITextFileBuffer textFileBuffer, Location fileLoc, DocumentEvent event) {
	}
	
	@SuppressWarnings("unused")
	protected void bufferDisposed(ITextFileBuffer textFileBuffer, Location fileLoc) {
	}
	
}