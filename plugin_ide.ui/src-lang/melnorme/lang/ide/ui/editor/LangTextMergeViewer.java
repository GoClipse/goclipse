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
package melnorme.lang.ide.ui.editor;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.ICompareContainer;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.contexts.IContextService;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.text.LangSourceViewerConfiguration;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class LangTextMergeViewer extends TextMergeViewer {
	
	public LangTextMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);
	}
	
	@Override
	protected SourceViewer createSourceViewer(Composite parent, int textOrientation) {
		int styles = textOrientation | SWT.H_SCROLL | SWT.V_SCROLL;
		return new LangSourceViewer(parent, new CompositeRuler(), styles);
	}
	
	@Override
	protected IDocumentPartitioner getDocumentPartitioner() {
		return TextSettings_Actual.createDocumentSetupHelper().createDocumentPartitioner();
	}
	
	@Override
	protected String getDocumentPartitioning() {
		return TextSettings_Actual.PARTITIONING_ID;
	}
	
	@Override
	public String getTitle() {
		return LangCore_Actual.NAME_OF_LANGUAGE + LangEditorMessages.MSG__SOURCE_COMPARE;
	}
	
	public IPreferenceStore getPreferenceStore() {
		return LangUIPlugin.getDefault().getCombinedPreferenceStore();
	}
	
	@Override
	protected void createControls(Composite composite) {
		super.createControls(composite);
		ICompareContainer container = getCompareConfiguration().getContainer();
		
		IWorkbenchPart workbenchPart= container.getWorkbenchPart();
		if (workbenchPart != null) {
			IContextService service= workbenchPart.getSite().getService(IContextService.class);
			if (service != null) {
				service.activateContext(EditorSettings_Actual.EDITOR_CONTEXT_ID);
			}
		}
		// TODO: activate Lang editor commands
//		IHandlerService handlerSvc = container.getServiceLocator().getService(IHandlerService.class);
//		handlerSvc.activateHandler(EditorCommandIds.OpenDef_ID, new OpenDefinitionHandler2());
	}
	
	/* -----------------  ----------------- */
	
	// Necessary hack to determine which of the 3 textViewers we are working with
	private int sourceViewerNumber = 0;
	
	@Override
	protected void configureTextViewer(TextViewer textViewer) {
		if(textViewer instanceof SourceViewer) {
			SourceViewer sourceViewer = (SourceViewer) textViewer;
			sourceViewer.configure(getSourceViewerConfiguration(sourceViewer));
		} else {
			super.configureTextViewer(textViewer);
		}
		sourceViewerNumber = (sourceViewerNumber + 1) % 3;
	}
	
	protected SourceViewerConfiguration getSourceViewerConfiguration(ISourceViewer sourceViewer) {
		
		SourceViewerSourceBuffer sourceBuffer;
		if(sourceViewerNumber == 0) {
			// Ancestor viewer
			sourceBuffer = new SourceViewerSourceBuffer(sourceViewer);
		} else if(sourceViewerNumber == 1) {
			// Left viewer
			sourceBuffer = new SourceViewerSourceBuffer(sourceViewer) {
				@Override
				public Location getLocation_orNull() {
					return EditorUtils.getLocationOrNull(getEditorInput(sourceViewer));
				}
				@Override
				public boolean isDirty() {
					return isLeftDirty();
				}
			};
		} else {
			// Right viewer
			sourceBuffer = new SourceViewerSourceBuffer(sourceViewer) {
				@Override
				public Location getLocation_orNull() {
					return EditorUtils.getLocationOrNull(getEditorInput(sourceViewer));
				}
			};
		}
		
		return new LangSourceViewerConfiguration(getPreferenceStore(), sourceBuffer, null);
	}
	
	public static class SourceViewerSourceBuffer implements ISourceBufferExt {
		
		protected final ISourceViewer sourceViewer;
		
		public SourceViewerSourceBuffer(ISourceViewer sourceViewer) {
			this.sourceViewer = sourceViewer;
		}
		
		@Override
		public Location getLocation_orNull() {
			return null;
		}
		
		@Override
		public IDocument getDocument() {
			return assertNotNull(sourceViewer.getDocument());
		}
		
		@Override
		public boolean isDirty() {
			return true;
		}
		
		@Override
		public void doTrySaveBuffer() throws CommonException {
			throw new CommonException("Cannot save document for this source buffer");
		}
		
		@Override
		public ISourceBuffer getReadOnlyView() {
			return this; // This buffer is already readOnly
		}
		
	}
	
}