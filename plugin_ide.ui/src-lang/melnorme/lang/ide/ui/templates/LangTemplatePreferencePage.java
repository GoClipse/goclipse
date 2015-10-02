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
package melnorme.lang.ide.ui.templates;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

import melnorme.lang.ide.core.text.LangDocumentPartitionerSetup;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.LangSourceViewer;
import melnorme.util.swt.jface.text.ColorManager2;

public abstract class LangTemplatePreferencePage extends TemplatePreferencePage implements IWorkbenchPreferencePage {
	
	public LangTemplatePreferencePage() {
		super();
		setPreferenceStore(LangUIPlugin.getInstance().getPreferenceStore());
		setTemplateStore(LangUIPlugin.getTemplateRegistry().getTemplateStore());
		setContextTypeRegistry(LangUIPlugin.getTemplateRegistry().getContextTypeRegistry());
		
		setDescription(LangUIMessages.TemplatePreferencePage_message);
	}
	
	@Override
	public void setTitle(String title) {
		super.setTitle(LangUIMessages.TemplatePreferencePage_title);
	}
	
	public static ColorManager2 getColorManager() {
		return LangUIPlugin.getInstance().getColorManager();
	}
	
	@Override
	public boolean performOk() {
		boolean ok= super.performOk();
		LangUIPlugin.flushInstanceScope();
		return ok;
	}
	
	@Override
	protected boolean isShowFormatterSetting() {
		return false;
	}
	@Override
	protected String getFormatterPreferenceKey() {
		return super.getFormatterPreferenceKey();
	}
	
	@Override
	protected SourceViewer createViewer(Composite parent) {
		SourceViewer viewer = new LangSourceViewer(parent, null, null, false, 
			SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		
		viewer.configure(createPreviewerSourceViewerConfiguration());
		viewer.setEditable(false);
		viewer.setDocument(createViewerDocument());
		return viewer;
	}
	
	protected IDocument createViewerDocument() {
		IDocument document = new Document();
		LangDocumentPartitionerSetup.getInstance().setup(document);
		return document;
	}
	
	protected SourceViewerConfiguration createPreviewerSourceViewerConfiguration() {
		return EditorSettings_Actual.createSimpleSourceViewerConfiguration(getPreferenceStore(), getColorManager());
	}
	
	// Note: Mostly copied from parent, in the future we might need to modify this code.
	@Override
	protected void updateViewerInput() {
		IStructuredSelection selection= (IStructuredSelection) getTableViewer().getSelection();
		
		if(selection.size() == 1 && selection.getFirstElement() instanceof TemplatePersistenceData) {
			TemplatePersistenceData data= (TemplatePersistenceData) selection.getFirstElement();
			Template template= data.getTemplate();
			getViewer().getDocument().set(template.getPattern());
		} else {
			getViewer().getDocument().set("");
		}
	}
	
	/* ----------------- EditTemplateDialog ----------------- */
	
	@Override
	protected Template editTemplate(Template template, boolean edit, boolean isNameModifiable) {
		EditTemplateDialog dialog = new LangEditTemplateDialog(getShell(), template, edit, isNameModifiable,
			getContextTypeRegistry());
		if(dialog.open() == Window.OK) {
			return dialog.getTemplate();
		}
		
		return null;
	}
	
	
	public static class LangEditTemplateDialog extends EditTemplateDialog {
		
		public LangEditTemplateDialog(Shell parent, Template template, boolean edit, boolean isNameModifiable,
				ContextTypeRegistry registry) {
			super(parent, template, edit, isNameModifiable, registry);
		}
		
		@Override
		protected SourceViewer createViewer(Composite parent) {
			LangSourceViewer viewer = new LangSourceViewer(parent, null, null, false,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			
			final IContentAssistProcessor templateProcessor = getTemplateProcessor();
			
			IDocument document = new Document();
			LangDocumentPartitionerSetup.getInstance().setup(document);
			
			IPreferenceStore store = LangUIPlugin.getDefault().getCombinedPreferenceStore();
			SourceViewerConfiguration configuration = EditorSettings_Actual
					.createTemplateEditorSourceViewerConfiguration(store, templateProcessor);
			viewer.configure(configuration);
			viewer.setEditable(true);
			viewer.setDocument(document);
			
			return viewer;
		}
		
	}
	
}