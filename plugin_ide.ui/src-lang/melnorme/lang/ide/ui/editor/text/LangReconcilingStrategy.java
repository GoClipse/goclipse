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
package melnorme.lang.ide.ui.editor.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.core.engine.EngineClient;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.ui.texteditor.ITextEditor;

@Deprecated
public class LangReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {
	
	protected final ITextEditor editor;
	protected final EngineClient engineClient;
	
	protected IDocument document;
	
	public LangReconcilingStrategy(ITextEditor editor, EngineClient engineClient) {
		this.editor = assertNotNull(editor);
		this.engineClient = assertNotNull(engineClient);
	}
	
	@Override
	public void setDocument(IDocument document) {
		this.document = assertNotNull(document);
	}
	
	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
	}
	
	@Override
	public void initialReconcile() {
		doReconcile();
	}
	
	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(subRegion);
	}
	
	@Override
	public void reconcile(IRegion partition) {
		doReconcile();
	}
	
	protected void doReconcile() {
		if(document == null) {
			throw assertFail();
		}
		
//		Object modelKey = AbstractLangStructureEditor.getStructureModelKeyFromEditorInput(editor.getEditorInput());
//		
//		log.println("Reconcile: " + modelKey + "");
//		
//		engineClient.notifyDocumentChange(modelKey, document.get());
	}
	
}