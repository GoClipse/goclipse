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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.EngineClient;
import melnorme.lang.ide.core.engine.IStructureModelListener;
import melnorme.lang.ide.core.engine.StructureModelManager;
import melnorme.lang.ide.core.engine.StructureModelManager.MDocumentSynchedAcess;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.LangSourceViewerConfiguration;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.part.IShowInTargetList;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Extension to {@link AbstractLangEditor} with functionality to hande {@link StructureElement}s
 */
public abstract class AbstractLangStructureEditor extends AbstractLangEditor {
	
	protected final EngineClient engineClient = LangCore.getEngineClient();
	protected final LangOutlinePage outlinePage = addOwned(init_createOutlinePage());
	
	public AbstractLangStructureEditor() {
		super();
	}
	
	public EngineClient getEngineClient() {
		return engineClient;
	}
	
	@Override
	protected AbstractLangSourceViewerConfiguration createSourceViewerConfiguration() {
		ColorManager2 colorManager = LangUIPlugin.getInstance().getColorManager();
		return new LangSourceViewerConfiguration(getPreferenceStore(), colorManager, this, 
			EditorSettings_Actual.getStylingPreferences());
	}
	
	/* -----------------  ----------------- */
	
	protected final StructureInfoBinding structureInfoBinding = new StructureInfoBinding();
	
	protected Location editorLocation;
	protected volatile StructureInfo editorStructureInfo;
	
	@Override
	protected void internalDoSetInput(IEditorInput input) {
		super.internalDoSetInput(input);
		
		if(editorStructureInfo != null) {
			// Disconnect from previous input
			structureInfoBinding.endStructureUpdates();
			editorStructureInfo = null;
		}
		
		if(input == null) {
			// I don't think this case is possible, but guard against it just in case
			LangCore.logError("input is null.");
			return;
		}
		
		Object editorKey = getStructureModelKeyFromEditorInput(input);
		editorLocation = (Location) (editorKey instanceof Location ? editorKey : null);
		
		IDocument document = getDocumentProvider().getDocument(input);
		structureInfoBinding.beginStructureUpdates(editorKey, document);
		putOwned(structureInfoBinding);
	}
	
	public static Object getStructureModelKeyFromEditorInput(IEditorInput input) {
		try {
			// Try to adapt as Location
			return EditorUtils.getLocationFromEditorInput(input);
		} catch(CoreException e) {
			// Is input thread-safe? We assume so since IEditorInput is supposed to be immutable.
			return input;
		}
	}
	
	protected class StructureInfoBinding implements IStructureModelListener, IDisposable {
		
		protected final StructureModelManager structureMgr = engineClient;
		
		public void beginStructureUpdates(Object editorKey, IDocument doc) {
			editorStructureInfo = structureMgr.connectStructureUpdates(editorKey, doc, this);
			assertNotNull(editorStructureInfo);
			// Send initial update
			handleEditorStructureUpdated();
		}
		
		public void endStructureUpdates() {
			structureMgr.disconnectStructureUpdates2(editorStructureInfo, this, new MDocumentSynchedAcess() {});
		}
		
		@Override
		public void dispose() {
			endStructureUpdates();
		}
		
		@Override
		public void structureChanged(StructureInfo lockedStructureInfo, final SourceFileStructure structure) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					// editor input might have changed, so re-check this update applies to editor structure info
					if(lockedStructureInfo != editorStructureInfo) {
						return;
					}
					handleEditorStructureUpdated();
				}
			});
		}
		
	}
	
	protected final DomainField<SourceFileStructure> structureField = new DomainField<>();
	protected final DomainField<StructureElement> selectedElementField = new DomainField<>();
	
	public DomainField<SourceFileStructure> getStructureField() {
		return structureField;
	}
	
	public DomainField<StructureElement> getSelectedElementField() {
		return selectedElementField;
	}
	
	public SourceFileStructure getSourceStructure() {
		return structureField.getFieldValue();
	}
	
	public StructureElement getSelectedElement() {
		return selectedElementField.getFieldValue();
	}
	
	public StructuredSelection getSelectedElementAsStructureSelection() {
		StructureElement selectedElement = getSelectedElement();
		if(selectedElement == null) {
			return StructuredSelection.EMPTY;
		}
		return new StructuredSelection(selectedElement);
	}
	
	
	protected void handleEditorStructureUpdated() {
		assertTrue(Display.getCurrent() != null);
		
		SourceFileStructure structure = editorStructureInfo.getStoredStructure();
		if(structure == null) {
			return; // Ignore
		}
		
		structureField.setFieldValue(structure);
		
		setSelectedElementField();
	}
	
	/* ----------------- Selection ----------------- */
	
	protected class EditorSelectionChangedListener extends AbstractSelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			setSelectedElementField();
		}
	}
	
	protected void setSelectedElementField() {
		ISelectionProvider selectionProvider = getSelectionProvider();
		if(selectionProvider == null) {
			return; // Can happen during dispose 
		}
		
		ISelection selection = selectionProvider.getSelection();
		if(selection instanceof TextSelection) {
			TextSelection textSelection = (TextSelection) selection;
			int caretOffset = textSelection.getOffset();
			
			SourceFileStructure structure = getSourceStructure();
			if(structure != null) {
				StructureElement selectedElement = structure.getStructureElementAt(caretOffset);
				selectedElementField.setFieldValue(selectedElement);
			}
		}
	}
	
	protected final EditorSelectionChangedListener editorSelectionListener = new EditorSelectionChangedListener();
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		editorSelectionListener.install(assertNotNull(getSourceViewer_()));
	}
	
	@Override
	public void dispose() {
		editorSelectionListener.uninstall(getSourceViewer_());
		super.dispose();
	}
	
	/* ----------------- Outline ----------------- */
	
	protected LangOutlinePage init_createOutlinePage() {
		return new LangOutlinePage(this);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public Object getAdapter(Class requestedClass) {
		if (IContentOutlinePage.class.equals(requestedClass)) {
			return outlinePage;
		}
		if(requestedClass == IShowInTargetList.class) {
			return new IShowInTargetList() {
				@Override
				public String[] getShowInTargetIds() {
					return array(IPageLayout.ID_OUTLINE);
				}
			};
		}
		return super.getAdapter(requestedClass);
	}
	
	/* -----------------  ----------------- */
	
	public void setElementSelection(StructureElement element) {
		if(getSelectedElementField().isNotifyingListeners()) {
			return; // Ignore
		}
		setElementSelection(this, element);
		markInNavigationHistory();
	}
	
	public static void setElementSelection(ITextEditor editor, StructureElement element) {
		SourceRange nameSR = element.getNameSourceRange();
		if(nameSR != null) {
			editor.selectAndReveal(nameSR.getOffset(), nameSR.getLength());
		}
	}
	
}