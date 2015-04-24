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
package melnorme.lang.ide.ui.editor.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.structure.StructureModelManager;
import melnorme.lang.utils.M_WorkerThread;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.SimpleLogger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.ui.texteditor.ITextEditor;

public class LangReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {
	
	protected static final SimpleLogger log = new SimpleLogger("LangReconcilingStrategy"); 
	
	protected final StructureModelManager modelManager = StructureModelManager.getDefault();
	protected final ITextEditor editor;
	
	protected IDocument document;
	
	public LangReconcilingStrategy(ITextEditor editor) {
		this.editor = assertNotNull(editor);
	}
	
	@Override
	public void setDocument(IDocument document) {
		this.document = document;
		log.println("set Document :");
	}
	
	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
	}
	
	@Override
	public void initialReconcile() {
	}
	
	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(subRegion);
	}
	
	@Override
	public void reconcile(IRegion partition) {
		Location location;
		try {
			location = EditorUtils.getLocationFromEditorInput(editor.getEditorInput());
		} catch (CoreException e) {
			LangUIPlugin.logInternalError(e);
			return;
		}
		
		log.println("Reconcile [" + location +  "] @ " + partition);
		
		modelManager.rebuild(location, document.get(), new M_WorkerThread());
	}
	
}