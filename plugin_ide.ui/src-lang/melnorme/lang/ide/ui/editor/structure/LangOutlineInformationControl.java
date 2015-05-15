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
package melnorme.lang.ide.ui.editor.structure;


import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.text.SimpleLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.views.AbstractFilteredTreePopupControl;
import melnorme.lang.ide.ui.views.StructureElementLabelProvider;
import melnorme.lang.tooling.structure.ISourceFileStructure;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import _org.eclipse.jdt.internal.ui.util.StringMatcher;

public abstract class LangOutlineInformationControl extends AbstractFilteredTreePopupControl {
	
	public LangOutlineInformationControl(Shell parent, int shellStyle, int treeStyle) {
		super(parent, shellStyle, treeStyle);
	}
	
	protected abstract IPreferenceStore getPreferenceStore();
	
	@Override
	protected String getDialogSettingsId() {
		return LangUIPlugin.PLUGIN_ID + ".QuickOutline";
	}
	
	@Override
	protected void createTreeViewer(Composite parent, int treeStyle) {
		super.createTreeViewer(parent, treeStyle);
		
		treeViewer.setLabelProvider(new StructureElementLabelProvider());
		treeViewer.setContentProvider(new StructureElementContentProvider());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void setInput(Object information) {
		ISourceFileStructure structure = null;
		
		if(information instanceof ISourceFileStructure) {
			structure = (ISourceFileStructure) information;
		}
		
		if(structure != null) {
			super.doSetInput(structure, null);
		} else {
			LangUIPlugin.logInternalError(new Exception("Could not determine structure from input."));
			super.setInput(null);
		}
		
	}
	
	@Override
	protected void gotoSelectedElement() {
		Object selectedElement = getSelectedElement();
		
		if(selectedElement != null) {
			try {
				dispose();
				EditorStructureUtil.openInEditorAndReveal(selectedElement);
			} catch (CoreException ce) {
				LangUIPlugin.logStatus(ce);
			}
		}
	}
	
	/* -----------------  ----------------- */
	
	protected StringMatcher stringMatcher;
	
	@Override
	protected void setFilteringString(String pattern) {
		if(pattern.length() > 0) {
			boolean ignoreCase = pattern.toLowerCase().equals(pattern);
			stringMatcher = new StringMatcher(pattern + "*", ignoreCase, false);
		}
		
		super.setFilteringString(pattern);
	}
	
	@Override
	protected boolean matchNameDirectlyFilteredIn(String matchName) {
		return stringMatcher.match(matchName);
	}
	
	/* -----------------  IInformationControlCreator  ----------------- */
	
	public static class OutlineInformationControlCreator implements IInformationControlCreator {
		
		protected final SimpleLangSourceViewerConfiguration svc;
		
		public OutlineInformationControlCreator(SimpleLangSourceViewerConfiguration svc) {
			this.svc = svc;
		}
		
		@Override
		public IInformationControl createInformationControl(Shell parent) {
			int shellStyle = SWT.RESIZE;
			int treeStyle = SWT.V_SCROLL | SWT.H_SCROLL;
			return new LangOutlineInformationControl(parent, shellStyle, treeStyle) {
				@Override
				protected IPreferenceStore getPreferenceStore() {
					return svc.getPreferenceStore();
				}
			};
		}
	}
	
}