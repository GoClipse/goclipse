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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.IStructureModelListener;
import melnorme.lang.ide.core.engine.LocationKey;
import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureModelRegistration;
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
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.misc.Location;

/**
 * Extension to {@link AbstractLangEditor} with functionality to hande {@link StructureElement}s
 */
public abstract class AbstractLangStructureEditor extends AbstractLangEditor {
	
	protected final SourceModelManager sourceModelMgr = LangCore.getSourceModelManager();
	protected final LangOutlinePage outlinePage = addOwned(init_createOutlinePage());
	
	public AbstractLangStructureEditor() {
		super();
	}
	
	@Override
	protected AbstractLangSourceViewerConfiguration createSourceViewerConfiguration() {
		ColorManager2 colorManager = LangUIPlugin.getInstance().getColorManager();
		return new LangSourceViewerConfiguration(getPreferenceStore(), colorManager, this, 
			EditorSettings_Actual.getStylingPreferences());
	}
	
	/* -----------------  ----------------- */
	
	protected Location editorLocation;
	protected StructureModelRegistration modelRegistration; 
	
	{	
		owned.bind(this::disconnectUpdates); 
	}
	
	@Override
	protected void internalDoSetInput(IEditorInput input) {
		super.internalDoSetInput(input);
		
		// Disconnect updates for previous input
		disconnectUpdates();
		
		LocationKey editorKey = getStructureModelKeyFromEditorInput(input);
		editorLocation = editorKey.getLocation();
		
		IDocument doc = getDocumentProvider().getDocument(input);
		modelRegistration = sourceModelMgr.connectStructureUpdates(editorKey, doc, structureInfoListener);
		
		// Send initial update
		handleEditorStructureUpdated(modelRegistration.structureInfo);
	}
	
	protected void disconnectUpdates() {
		if(modelRegistration != null) {
			modelRegistration.dispose();
			modelRegistration = null;
		}
	}
	
	public static LocationKey getStructureModelKeyFromEditorInput(IEditorInput input) {
		try {
			// Try to adapt as Location
			Location location = EditorUtils.getLocationFromEditorInput(input);
			return new LocationKey(location);
		} catch(CommonException e) {
			// Is input thread-safe? We assume so since IEditorInput is supposed to be immutable.
			return new LocationKey(input, input.toString());
		}
	}
	
	protected final Field<Result<SourceFileStructure, CommonException>> structureResultField = new Field<>();
	
	public Field<Result<SourceFileStructure, CommonException>> getStructureField() {
		return structureResultField;
	}
	
	public Result<SourceFileStructure, CommonException> getSourceStructureResult() {
		return getStructureField().getFieldValue();
	}
	
	public SourceFileStructure getSourceStructure() throws CommonException {
		return getSourceStructureResult().get();
	}
	
	protected final Field<StructureElement> selectedElementField = new Field<>();
	
	public Field<StructureElement> getSelectedElementField() {
		return selectedElementField;
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
	
	protected final IStructureModelListener structureInfoListener = new IStructureModelListener() {
		
		@Override
		public void dataChanged(StructureInfo lockedStructureInfo) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					// editor input might have changed, so check this update still applies to editor binding
					
					if(modelRegistration == null || lockedStructureInfo != modelRegistration.structureInfo) {
						return;
					}
					handleEditorStructureUpdated(modelRegistration.structureInfo);
				}
			});
		}
	};
	
	protected void handleEditorStructureUpdated(StructureInfo structureInfo) {
		assertTrue(Display.getCurrent() != null);
		
		structureResultField.setFieldValue(structureInfo.getStoredData());
		
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
			
			SourceFileStructure structure;
			try {
				structure = getSourceStructure();
			} catch(CommonException e) {
				return;
			}
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
		owned.bind(() -> editorSelectionListener.uninstall(getSourceViewer_()));
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
		SourceRange nameSR = element.getNameSourceRange2();
		if(nameSR != null) {
			editor.selectAndReveal(nameSR.getOffset(), nameSR.getLength());
		}
	}
	
}