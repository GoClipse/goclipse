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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureUpdateTask;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.utils.CoreExecutors;
import melnorme.lang.ide.core.utils.DefaultBufferListener;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.ICommonExecutor;
import melnorme.utilbox.concurrency.CompletableResult.CompletableLatch;
import melnorme.utilbox.core.fntypes.CallableX;
import melnorme.utilbox.misc.Location;

/**
 * Manager for all model reconciliations after text file buffer changes,
 * whether is document modifications, or file saves.
 */
public class DocumentReconcileManager extends AbstractAgentManager {
	
	protected final ITextFileBufferManager fbm;
	protected final ProjectReconcileManager projectReconciler;
	
	public DocumentReconcileManager() {
		this(FileBuffers.getTextFileBufferManager(), LangCore.getBuildManager());
	}
	
	public DocumentReconcileManager(ITextFileBufferManager fbm, BuildManager buildMgr) {
		this.fbm = assertNotNull(fbm);
		this.projectReconciler = assertNotNull(new ProjectReconcileManager(this.executor, buildMgr));
	}
	
	@Override
	protected ICommonExecutor init_executor() {
		return CoreExecutors.newExecutorTaskAgent(DocumentReconcileManager.class);
	}
	
	@Override
	protected void dispose_post() {
		executor.shutdownNowAndCancelAll();
	}
	
	protected BuildManager getBuildManager() {
		return projectReconciler.buildMgr;
	}
	
	/* -----------------  ----------------- */
	
	public DocumentReconcileConnection connectDocument(IDocument document, StructureInfo structureInfo) {
		Location location = structureInfo.getLocation();
		ITextFileBuffer textFileBuffer = location == null ? null : ResourceUtils.getTextFileBuffer(fbm, location);
		
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

		protected CompletableLatch fileSaveLatch = new CompletableLatch();
		
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
			fileSaveLatch.setCancelledResult();
		}
		
		@Override
		protected StructureUpdateTask doDocumentChanged() {
			StructureUpdateTask structureUpdateTask = structureInfo.queueSourceUpdateTask(document.get());
			
			fileSaveLatch.setCancelledResult();
			fileSaveLatch = new CompletableLatch();
			
			if(project != null) {
				projectReconciler.invalidateProjectModel(project, structureUpdateTask, fileSaveLatch);
			}
			return structureUpdateTask;
		}
		
		public class DirtyBufferListener extends DefaultBufferListener {
			
			@Override
			public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
				if(buffer == textFileBuffer) {
					if(!isDirty) {
						handleDocumentSaved();
					}
				}
			}
			
			/** Determine if document save was invoked under a Build action.
			 * Uses a hack to determine this, unfortunately there is currently no other way to figure this out.
			 * TODO: submit a bug report
			 */
			public boolean isUnderBuildAction() {
				StackTraceElement[] stackTrace = new Exception().getStackTrace();
				for (StackTraceElement stackTraceElement : stackTrace) {
					if(!stackTraceElement.getMethodName().equals("run")) {
						continue;
					}
					String className = stackTraceElement.getClassName();
					if(className.endsWith(".BuildAction") || className.endsWith(".GlobalBuildAction")) {
						return true;
					}
				}
				return false;
			}
			
			protected void handleDocumentSaved() {
				if(!getBuildManager().autoBuildsEnablement().isEnabled() || isUnderBuildAction()) {
					// Cancel the current reconciliation task, because there is no point to it
					// A normal, full build will be performed instead.
					projectReconciler.cancelPendingReconciliation(project);
					fileSaveLatch.setCompleted();
					return;
				}
				// Mark the file save as completed
				fileSaveLatch.setCompleted();
				
				StructureUpdateTask structureUpdateTask = structureInfo.documentSaved(document);
				
				if(structureUpdateTask != null && project != null) {
					projectReconciler.invalidateProjectModel(project, structureUpdateTask, fileSaveLatch);
				}
			}
			
		}
		
	}
	
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