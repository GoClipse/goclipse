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
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ISynchronizable;

import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureUpdateTask;
import melnorme.lang.ide.core.utils.CoreExecutors;
import melnorme.lang.ide.core.utils.DefaultBufferListener;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.ICommonExecutor;
import melnorme.utilbox.concurrency.ResultFuture.LatchFuture;
import melnorme.utilbox.core.fntypes.CallableX;

/**
 * Manager for all model reconciliations after text file buffer changes,
 * whether is document modifications, or file saves.
 */
public class DocumentReconcileManager extends AbstractAgentManager {
	
	protected final ITextFileBufferManager fbm;
	
	public DocumentReconcileManager() {
		this(FileBuffers.getTextFileBufferManager());
	}
	
	public DocumentReconcileManager(ITextFileBufferManager fbm) {
		this.fbm = assertNotNull(fbm);
	}
	
	@Override
	protected ICommonExecutor init_executor() {
		return CoreExecutors.newExecutorTaskAgent(DocumentReconcileManager.class);
	}
	
	/* -----------------  ----------------- */
	
	public DocumentReconcileConnection connectDocument(IDocument document, StructureInfo structureInfo) {
		ITextFileBuffer textFileBuffer = ResourceUtils.getTextFileBuffer(fbm, structureInfo.getLocation());
		
		if(textFileBuffer != null) {
			return new TextReconcileConnection(document, structureInfo, textFileBuffer);
		}
		return new DocumentReconcileConnection(document, structureInfo);
	}
	
	public class DocumentReconcileConnection {
		
		protected final IDocument document;
		protected final StructureInfo structureInfo;

		public DocumentReconcileConnection(IDocument document, StructureInfo structureInfo) {
			this.document = assertNotNull(document);
			this.structureInfo = assertNotNull(structureInfo);
			
			document.addDocumentListener(docListener);
		}
		
		public void disconnect() {
			document.removeDocumentListener(docListener);
		}
		
		protected final IDocumentListener docListener = new IDocumentListener() {
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
			@Override
			public void documentChanged(DocumentEvent event) {
				assertTrue(document == event.fDocument);
				doDocumentChanged();
			}
		};
		
		protected StructureUpdateTask doDocumentChanged() {
			return structureInfo.queueSourceUpdateTask(document.get());
		}
		
	}
	
	public static IProject getProject(IFileBuffer fileBuffer) {
		/* TODO : improve the API to get a project, or ignore project altogheter */
		
		IFile file= FileBuffers.getWorkspaceFileAtLocation(fileBuffer.getLocation(), true);
		if (file == null) {
			return null;
		}
		
		return file.getProject();
	}
	
	public class TextReconcileConnection extends DocumentReconcileConnection {
		
		protected final IProject project; // Can be null
		
		protected final ITextFileBuffer textFileBuffer;
		protected final DirtyBufferListener fbListener;

		protected LatchFuture fileSaveFuture = new LatchFuture();
		
		public TextReconcileConnection(IDocument document, StructureInfo structureInfo, ITextFileBuffer textFileBuffer) {
			super(document, structureInfo);
			
			this.textFileBuffer = assertNotNull(textFileBuffer);
			
			fbListener = new DirtyBufferListener();
			fbm.addFileBufferListener(fbListener);
			
			project = getProject(textFileBuffer);
		}
		
		@Override
		public void disconnect() {
			super.disconnect();
			fbm.removeFileBufferListener(fbListener);
			fileSaveFuture.cancel();
		}
		
		@Override
		protected StructureUpdateTask doDocumentChanged() {
			StructureUpdateTask structureUpdateTask = structureInfo.queueSourceUpdateTask(document.get());
			
			fileSaveFuture.cancel();
			fileSaveFuture = new LatchFuture();
			
			if(project != null) {
				projectReconciler.invalidateProjectModel(project, structureUpdateTask, fileSaveFuture);
			}
			return structureUpdateTask;
		}
		
		public class DirtyBufferListener extends DefaultBufferListener {
			
			@Override
			public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
				if(buffer == textFileBuffer) {
					if(!isDirty) {
						// Mark the file save as completed
						fileSaveFuture.setCompleted();
						handleDocumentSaved();
					}
				}
			}
			
			protected void handleDocumentSaved() {
				StructureUpdateTask structureUpdateTask = structureInfo.documentSaved(document);
				
				if(structureUpdateTask != null && project != null) {
					projectReconciler.invalidateProjectModel(project, structureUpdateTask, fileSaveFuture);
				}
			}
			
		}
		
	}
	
	protected final ProjectReconcileManager projectReconciler = new ProjectReconcileManager(this.executor);
	
	/* -----------------  ----------------- */
	
	public static <R> R runUnderDocumentLock(IDocument doc, CallableX<R, RuntimeException> runnable) {
		if(doc instanceof ISynchronizable) {
			ISynchronizable synchronizable = (ISynchronizable) doc;
			
			Object lockObject = synchronizable.getLockObject();
			if(lockObject != null) {
				synchronized(lockObject) {
					return runnable.call();
				}
			}
		}
		
		return runnable.call();
	}
	
}