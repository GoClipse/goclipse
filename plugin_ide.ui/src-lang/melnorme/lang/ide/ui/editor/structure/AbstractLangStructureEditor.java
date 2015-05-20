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
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.EngineClient;
import melnorme.lang.ide.core.engine.IStructureModelListener;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.runtime.CoreException;
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
		
		engineClient.getStructureManager().addListener(new StructureModelListener());
	}
	
	public EngineClient getEngineClient() {
		return engineClient;
	}
	
	protected Location editorLocation;
	protected volatile Object editorKey;
	
	protected class StructureModelListener implements IStructureModelListener {
		@Override
		public void structureChanged(StructureInfo lockedStructureInfo, final SourceFileStructure structure) {
			final Object key = lockedStructureInfo.getKey();
			
			// Note: editorKey may be null at this stage.
			
			if(!areEqual(key, editorKey))
				return;
			
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					handleEditorStructureUpdated(key, structure);
				}
			});
		}
	}
	
	@Override
	protected void internalDoSetInput(IEditorInput input) {
		super.internalDoSetInput(input);
		
		editorKey = getStructureModelKeyFromEditorInput(input);
		editorLocation = (Location) (editorKey instanceof Location ? editorKey : null);
		
		// From this moment onwards, the structure model listener will give updates to new key only
		
		// Send initial update
		SourceFileStructure storedStructure = engineClient.getStructureManager().getStoredStructure(editorKey);
		handleEditorStructureUpdated(editorKey, storedStructure);
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
	
	
	protected void handleEditorStructureUpdated(Object key, SourceFileStructure structure) {
		assertTrue(Display.getCurrent() != null);
		
		// editorKey might have changed, so re-check
		if(!areEqual(key, editorKey)) {
			return;
		}
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