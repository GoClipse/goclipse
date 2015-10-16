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
package melnorme.lang.ide.ui.editor.structure;


import melnorme.lang.ide.ui.views.StructureElementLabelProvider;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.fields.IFieldValueListener;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;

public class LangOutlinePage extends AbstractContentOutlinePage implements IAdaptable, IDisposable {
	
	protected final AbstractLangStructureEditor editor;
	
	public LangOutlinePage(AbstractLangStructureEditor editor) {
		super();
		this.editor = editor;
	}
	
	/**
	 * Note: createControl/dispose can be called multiple times per receiver lifetime. 
	 * (it's a bit unusual/strange for a control to allow that, but it's helpfull if LangOutlinePage works like that)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		
		getTreeViewer().setContentProvider(new StructureElementContentProvider());
		getTreeViewer().setLabelProvider(StructureElementLabelProvider.createLangLabelProvider());
		
		customizeCreateControl();
		
		editor.getStructureField().registerListener(structureListener);
		editor.getSelectedElementField().registerListener(structureListener);
		
		updateTreeViewer();
	}
	
	protected void customizeCreateControl() {
	}
	
	@Override
	public void dispose() {
		editor.getSelectedElementField().removeListener(structureListener);
		editor.getStructureField().removeListener(structureListener);
		
		super.dispose();
	}
	
	protected final IFieldValueListener structureListener = new IFieldValueListener() {
		@Override
		public void fieldValueChanged() {
			updateTreeViewer();
		}
	};
	
	protected void updateTreeViewer() {
		if(getTreeViewer() == null) {
			return;
		}
		
		SourceFileStructure structure = editor.getSourceStructure();
		if(getTreeViewer().getInput() != structure) {
			getTreeViewer().setInput(structure);
			getTreeViewer().refresh();
		}
		updateSelectionFromEditor();
	}
	
	protected boolean updatingFromEditor = false;
	
	protected void updateSelectionFromEditor() {
		StructuredSelection newSelection = editor.getSelectedElementAsStructureSelection();
		try {
			updatingFromEditor = true;
			getTreeViewer().setSelection(newSelection, true);
		} finally {
			updatingFromEditor = false;
		}
	}
	
	@Override
	protected void treeViewerPostSelectionChanged(SelectionChangedEvent event) {
		Object firstElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
		if(firstElement instanceof StructureElement) {
			StructureElement structureElement = (StructureElement) firstElement;
			if(!updatingFromEditor) {
				editor.setElementSelection(structureElement);
			}
		}
		
		super.treeViewerPostSelectionChanged(event);
	}
	
	/* ----------------- Show in target ----------------- */
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if(adapter == IShowInTarget.class) {
			return (T) getShowInTarget();
		}
		
		return null;
	}
	
	protected IShowInTarget getShowInTarget() {
		return new IShowInTarget() {
			@Override
			public boolean show(ShowInContext context) {
				StructureElement structureElement = getStructureElementFor(context.getSelection());
				
				if(structureElement != null) {
					setSelection(new StructuredSelection(structureElement));
					return true;
				}
				
				return false;
			}

		};
	}
	
	public StructureElement getStructureElementFor(ISelection selection) {
		if(selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) selection;
			return GetUpdatedStructureUIOperation.getUpdatedStructureElementAt(editor, textSelection.getOffset());
		} 
		else if(selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if(structuredSelection.getFirstElement() instanceof StructureElement) {
				return (StructureElement) structuredSelection.getFirstElement();
			}
		}
		return null;
	}
	
}