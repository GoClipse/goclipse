/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;


import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

public abstract class AbstractLangEditor extends TextEditor {
	
	public AbstractLangEditor() {
		super();
	}
	
	@Override
	protected void initializeEditor() {
		super.initializeEditor();
	}
	
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		
		ISourceViewer sourceViewer = getSourceViewer();
		
		if (!(sourceViewer instanceof ISourceViewerExtension2)) {
			setPreferenceStore(createCombinedPreferenceStore(input));
		} else {
			ISourceViewerExtension2 sourceViewerExt2 = (ISourceViewerExtension2) sourceViewer;
			
			getSourceViewerDecorationSupport(sourceViewer).uninstall();
			sourceViewerExt2.unconfigure();

			setPreferenceStore(createCombinedPreferenceStore(input));
			
			sourceViewer.configure(getSourceViewerConfiguration());
			getSourceViewerDecorationSupport(sourceViewer).install(getPreferenceStore());
		}
		
		internalDoSetInput(input);
	}
	
	@Override
	protected void setPreferenceStore(IPreferenceStore store) {
		super.setPreferenceStore(store);
		setSourceViewerConfiguration(createSourceViewerConfiguration());
	}
	
	@SuppressWarnings("unused")
	protected void internalDoSetInput(IEditorInput input) {
	}
	
	protected abstract TextSourceViewerConfiguration createSourceViewerConfiguration();
	
	protected IPreferenceStore createCombinedPreferenceStore(IEditorInput input) {
		List<IPreferenceStore> stores = new ArrayList<IPreferenceStore>(4);
		
		// TODO: add project pref scope 
		@SuppressWarnings("unused")
		IProject project = EditorUtils.getAssociatedProject(input);
		
		stores.add(LangUIPlugin.getInstance().getPreferenceStore());
		stores.add(LangUIPlugin.getInstance().getCorePreferenceStore());
		stores.add(EditorsUI.getPreferenceStore());
		
		return new ChainedPreferenceStore(ArrayUtil.createFrom(stores, IPreferenceStore.class));
	}
	
}