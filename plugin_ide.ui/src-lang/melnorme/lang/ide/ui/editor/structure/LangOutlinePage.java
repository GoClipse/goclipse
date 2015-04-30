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
package melnorme.lang.ide.ui.editor.structure;


import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.views.StructureElementLabelProvider;
import melnorme.lang.tooling.structure.IStructureElement;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;

public class LangOutlinePage extends AbstractContentOutlinePage implements IAdaptable, IDisposable {
	
	protected final StructureModelManager modelManager = StructureModelManager.getDefault();
	protected final AbstractLangStructureEditor editor;
	
	protected volatile Location inputLocation;
	
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
		getTreeViewer().setLabelProvider(new StructureElementLabelProvider());
		
		customizeCreateControl();
		
		modelManager.addListener(modelListener);
		
		updateInputFromEditor();
	}
	
	protected void customizeCreateControl() {
	}
	
	@Override
	public void dispose() {
		modelManager.removeListener(modelListener);
		
		super.dispose();
	}
	
	// Note: this can be called multiple times per Control lifetime
	public void updateInputFromEditor() {
		try {
			inputLocation = EditorUtils.getLocationFromEditorInput(editor.getEditorInput());
		} catch(CoreException e) {
			inputLocation = null;
		}
		
		updateTreeViewer();
	}
	
	protected void updateTreeViewer() {
		if(getTreeViewer() == null) {
			return;
		}
		
		if(inputLocation == null) {
			getTreeViewer().setInput(null);
			return;
		}
		
		SourceFileStructure structure = modelManager.getStructure(inputLocation);
		getTreeViewer().setInput(structure);
		getTreeViewer().refresh();
	}
	
	protected final IStructureModelListener modelListener = new IStructureModelListener() {
		@Override
		public void structureChanged(Location location, final SourceFileStructure sourceFileStructure) {
			
			if(!areEqual(location, inputLocation)) {
				return;
			}
			
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					if(SWTUtil.isOkToUse(getControl())) {
						updateTreeViewer();
					}
				}
			});
		}
	};
	
	public IStructureElement getStructureElementFor(ISelection selection) {
		/*FIXME: needs testing*/
		
		if(selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) selection;
			return editor.getStructureElementAt(textSelection.getOffset());
		} 
		else if(selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if(structuredSelection.getFirstElement() instanceof IStructureElement) {
				return (IStructureElement) structuredSelection.getFirstElement();
			}
		}
		return null;
	}
	
	/* ----------------- Show in target ----------------- */
	
	@Override
	public Object getAdapter(Class key) {
		if (key == IShowInTarget.class) {
			return getShowInTarget();
		}
		
		return null;
	}
	
	protected IShowInTarget getShowInTarget() {
		return new IShowInTarget() {
			@Override
			public boolean show(ShowInContext context) {
				IStructureElement structureElement = getStructureElementFor(context.getSelection());
				
				if(structureElement != null) {
					setSelection(new StructuredSelection(structureElement));
					return true;
				}
				
				return false;
			}

		};
	}
	
}