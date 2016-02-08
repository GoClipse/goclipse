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
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IDocumentListener;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.CoreExecutors;
import melnorme.lang.ide.core.utils.DefaultBufferListener;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.misc.ReflectionUtils;
import melnorme.utilbox.misc.StreamUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.ownership.LifecycleObject;

public class AutoSaveManager extends LifecycleObject {
	
	protected final ExecutorService executor = CoreExecutors.newExecutorTaskAgent(getClass());
	
	public AutoSaveManager() {
		this(true);
	}
	
	public AutoSaveManager(boolean initialize) {
		if(initialize) {
			initialize();
		}
	}
	
	public void initialize() {
		ITextFileBufferManager fbm = FileBuffers.getTextFileBufferManager();
		fbm.addFileBufferListener(fbmListener);
		
		// setup the FBM dispose code:
		owned.add(() -> fbm.removeFileBufferListener(fbmListener));
	}
	
	protected final DefaultBufferListener fbmListener = new DefaultBufferListener() {
		@Override
		public void bufferCreated(IFileBuffer buffer) {
			if(buffer instanceof ITextFileBuffer) {
				ITextFileBuffer textFileBuffer = (ITextFileBuffer) buffer;
				IDocument doc = textFileBuffer.getDocument();
				IDocumentListener docListener = new DocumentAutoSaveListener(textFileBuffer);
				doc.addDocumentListener(docListener);
			}
		}
		
	};
	
	protected class DocumentAutoSaveListener implements IDocumentListener {
		
		protected final ITextFileBuffer textFileBuffer;
		protected volatile UpdaterTask currentUpdateTask;
		
		public DocumentAutoSaveListener(ITextFileBuffer textFileBuffer) {
			this.textFileBuffer = assertNotNull(textFileBuffer);
		}
		
		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}
		
		@Override
		public void documentChanged(DocumentEvent event) {
			IDocument document = event.fDocument;
			
			long synchronizationStamp = IDocumentExtension4.UNKNOWN_MODIFICATION_STAMP;
			if (document instanceof IDocumentExtension4) {
				synchronizationStamp = ((IDocumentExtension4)document).getModificationStamp();
			}
			
			// Usually synchronized block should not be necessary, given #documentChanged is triggered
			// under document lock. But just in case the document has no lock.
			synchronized (this) {
				String contents = document.get();
				
				if(currentUpdateTask != null) {
					currentUpdateTask.cancel();
				}
				UpdaterTask newUpdaterTask = new UpdaterTask(textFileBuffer, contents, synchronizationStamp);
				executor.submit(newUpdaterTask);
			}
			
		}
		
	}
	
	protected class UpdaterTask implements Runnable {
		
		protected final ITextFileBuffer textFileBuffer;
		protected final ByteArrayInputStream contentsStream;
		protected final long synchronizationStamp;
		protected final IProgressMonitor pm = new NullProgressMonitor();
		
		public UpdaterTask(ITextFileBuffer textFileBuffer, String contents, long synchronizationStamp) {
			this.textFileBuffer = textFileBuffer;
			/* FIXME:  hasBOM and UTF BOM in stream */
			this.contentsStream = new ByteArrayInputStream(contents.getBytes(StringUtil.UTF8));
			this.synchronizationStamp = synchronizationStamp;
		}
		
		public void cancel() {
			pm.setCanceled(true);
		}
		
		@Override
		public void run() {
			try {
				//System.out.println("  commitTextFileBuffer: " + contents.length());
				
				//textFileBuffer.commit(pm, false);
				doSoftBufferCommit();
				
				//System.out.println("finished update");
			} catch(CoreException e) {
				LangCore.logStatus(e);
			}
		}
		
		/* ----------------- Do a soft commit. ----------------- */
		// The code below should ideally be subsumed by new Platform Text API
		
		protected void doSoftBufferCommit() throws CoreException {
			// The textFileBuffer is either a ResourceTextFileBuffer (file inside the workspace) 
			// or a FileStoreTextFileBuffer (external file)
			
			IFile file = ResourceUtils.getWorkspaceRoot().getFile(textFileBuffer.getLocation());
			
			if(file != null) {
				// Then it's a ResourceTextFileBuffer
				saveResourceBuffer(file);
			} else {
				// Then it's a FileStoreTextFileBuffer
				saveFileStoreBuffer();
			}
		}
		
		protected void saveResourceBuffer(IFile file) throws CoreException {
			
			ResourceUtils.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					if(pm.isCanceled()) {
						return;
					}
					commitBufferToFile(file, pm);
				}
			}, file, IWorkspace.AVOID_UPDATE, pm);
		}
		
		protected void commitBufferToFile(IFile file, IProgressMonitor pm) throws CoreException {
//			new ResourceTextFileBuffer().commitFileBufferContent(pm, false);
			
			file.setContents(contentsStream, false, false, pm);
			if(synchronizationStamp != -1) {
				file.revertModificationStamp(synchronizationStamp);
				updateSynchronizationField(synchronizationStamp);
			} else {
				updateSynchronizationField(file.getModificationStamp());
			}
		}
		
		protected void updateSynchronizationField(long synchronizationStamp) {
			try {
				
				//textFileBuffer.fSynchronizationStamp = synchronizationStamp;
				ReflectionUtils.writeField(textFileBuffer, "fSynchronizationStamp", synchronizationStamp);
				
			} catch(NoSuchFieldException e) {
				throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
			}
		}
		
		protected void saveFileStoreBuffer() throws CoreException {
			
//			new FileStoreTextFileBuffer().commitFileBufferContent(pm, false);
			
			IFileStore fileStore = textFileBuffer.getFileStore();
			
			fileStore.getParent().mkdir(EFS.NONE, null);
			OutputStream out= fileStore.openOutputStream(EFS.NONE, null);
			
			try {
				StreamUtil.copyStream(contentsStream, out, true);
			} catch(IOException x) {
				throw LangCore.createCoreException(x.getLocalizedMessage(), x);
			}
			
			// set synchronization stamp to know whether the file synchronizer must become active
			updateSynchronizationField(fileStore.fetchInfo().getLastModified());
			
		}
		
	}
}